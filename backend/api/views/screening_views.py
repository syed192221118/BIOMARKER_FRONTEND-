from rest_framework import viewsets, permissions, status
from rest_framework.response import Response
from rest_framework.decorators import action
from ..models.screening import Screening, Biomarker
from ..serializers.screening_serializer import ScreeningSerializer, BiomarkerSerializer
from ..ai_engine import calculate_risk_score

class BiomarkerViewSet(viewsets.ReadOnlyModelViewSet):
    queryset = Biomarker.objects.all()
    serializer_class = BiomarkerSerializer
    permission_classes = [permissions.IsAuthenticated]

class ScreeningViewSet(viewsets.ModelViewSet):
    serializer_class = ScreeningSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
             # Doctor sees screenings of assigned patients
             # Or all screenings if they are an admin/supervisor type (for now just assigned)
             return Screening.objects.filter(doctor__user=user)
        elif user.role == 'patient':
             return Screening.objects.filter(patient__user=user)
        return Screening.objects.none()

    def perform_create(self, serializer):
        # Assign patient automatically
        if self.request.user.role == 'patient':
            # Patient creating screening
            patient = self.request.user.patient_profile
            # Save the screening first
            serializer.save(patient=patient, status='Submitted')
            # Fetch the screening instance from serializer
            screening = serializer.instance
            # Trigger AI processing immediately
            try:
                calculate_risk_score(screening)
            except Exception as e:
                # Log error but don't fail the request completely? 
                # Or maybe fail? For now, we print
                print(f"AI Error: {e}")
        else:
            # Doctor creating screening?
            serializer.save()

    @action(detail=True, methods=['post'])
    def submit(self, request, pk=None):
        screening = self.get_object()
        if screening.status == 'Draft':
            screening.status = 'Submitted'
            screening.save()
            
            # Trigger AI Processing synchronously for now (simulation)
            try:
                calculate_risk_score(screening)
                # In production, use: process_screening.delay(screening.id)
            except Exception as e:
                return Response({'status': 'error', 'message': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
                
            return Response({'status': 'submitted', 'message': 'AI Analysis Completed'})
        return Response({'status': 'already submitted'}, status=status.HTTP_400_BAD_REQUEST)
