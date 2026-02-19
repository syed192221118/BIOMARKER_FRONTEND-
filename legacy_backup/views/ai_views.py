from rest_framework import viewsets, permissions
from ..models.ai import AIResult
from ..serializers.ai_serializer import AIResultSerializer

class AIResultViewSet(viewsets.ReadOnlyModelViewSet):
    serializer_class = AIResultSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
             return AIResult.objects.filter(screening__doctor__user=user)
        elif user.role == 'patient':
             return AIResult.objects.filter(screening__patient__user=user)
        return AIResult.objects.none()

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from ..models.screening import Screening, ScreeningBiomarker, Biomarker
from ..ai_engine import calculate_risk_score

class ProcessScreeningView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request):
        # Expected Payload: {"readings_input": [{"biomarker_id": 1, "value": 100.0}, ...]}
        readings = request.data.get('readings_input', [])
        
        if not readings:
            return Response({'error': 'No readings provided'}, status=status.HTTP_400_BAD_REQUEST)

        # 1. Determine Patient & Doctor
        patient = None
        doctor = None
        
        if request.user.role == 'patient':
            try:
                patient = request.user.patient_profile
            except:
                return Response({'error': 'Patient profile not found'}, status=status.HTTP_400_BAD_REQUEST)
        
        elif request.user.role == 'doctor':
             # Doctor is performing screening. 
             # Since app doesn't send patient info, assign to a "Guest/Walk-in" patient.
             try:
                 doctor = request.user.doctor_profile
             except:
                 # If admin is doctor but no profile?
                 return Response({'error': 'Doctor profile not found'}, status=status.HTTP_400_BAD_REQUEST)

             from django.contrib.auth import get_user_model
             from ..models.patient import Patient
             User = get_user_model()
             
             # Find or Create Guest User
             guest_email = "guest_patient@biomarkerai.com"
             user, created = User.objects.get_or_create(email=guest_email, defaults={
                 'role': 'patient',
                 'is_active': True 
             })
             if created:
                 user.set_password("guest123")
                 user.save()
                 
             # Find or Create Patient Profile
             patient, _ = Patient.objects.get_or_create(user=user, defaults={
                 'full_name': "Walk-in Patient",
                 'age': 30,
                 'gender': "Other",
                 'height': 170.0,
                 'weight': 70.0
             })

        if not patient:
             return Response({'error': 'Could not identify patient'}, status=status.HTTP_400_BAD_REQUEST)
            
        screening = Screening.objects.create(
            patient=patient,
            doctor=doctor,
            status='Submitted'
        )

        # 2. Save Biomarkers
        for reading in readings:
            bm_id = reading.get('biomarker_id')
            val = reading.get('value')
            if bm_id is not None and val is not None:
                ScreeningBiomarker.objects.create(
                    screening=screening,
                    biomarker_id=bm_id,
                    value=val
                )
        
        # 3. Trigger AI
        try:
            ai_result = calculate_risk_score(screening)
        except Exception as e:
            # If AI fails, we still have the screening, but maybe return error?
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        # 4. Construct Response (matching Android AnalysisResponse)
        # data class AnalysisResponse(
        #     @SerializedName("metabolic_score") val metabolicScore: Int,
        #     @SerializedName("risk_level") val riskLevel: String,
        #     val insights: List<String>,
        #     @SerializedName("record_id") val recordId: Int
        # )
        
        # Determine risk level string (simple logic based on score, or fetch from result if stored)
        score = ai_result.metabolic_risk_score
        risk_level = "High" if score > 60 else "Moderate" if score > 30 else "Low"
        
        insights = [rf.description for rf in ai_result.risk_factors.all()]
        # Add recommendations too?
        insights += [rec.description for rec in ai_result.recommendations.all()]

        return Response({
            'metabolic_score': score,
            'risk_level': risk_level,
            'insights': insights,
            'record_id': screening.id,
            'report_url': f"/api/reports/{screening.id}/"
        })
