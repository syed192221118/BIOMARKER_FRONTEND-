from rest_framework import views, permissions, status
from rest_framework.response import Response
from django.utils import timezone
from datetime import timedelta
from django.db.models import Avg, Count
from ..models.patient import Patient
from ..models.screening import Screening
from ..models.ai import AIResult
from ..serializers.screening_serializer import ScreeningSerializer
from ..serializers.ai_serializer import AIResultSerializer

class DoctorDashboardView(views.APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request):
        if not hasattr(request.user, 'doctor_profile'):
            return Response({'error': 'User is not a doctor'}, status=status.HTTP_403_FORBIDDEN)
        
        doctor = request.user.doctor_profile
        today = timezone.now().date()
        week_start = today - timedelta(days=7)

        # Statistics
        total_patients = Patient.objects.filter(assigned_doctor=doctor).count()
        screenings_this_week = Screening.objects.filter(doctor=doctor, created_at__date__gte=week_start).count()
        pending_reviews = Screening.objects.filter(doctor=doctor, status='Submitted').count()
        
        # Recent Activities (Last 5 screenings)
        recent_screenings = Screening.objects.filter(doctor=doctor).order_by('-created_at')[:5]
        recent_serializer = ScreeningSerializer(recent_screenings, many=True)

        # High Risk Alerts (AI Results with high risk)
        high_risk_patients = AIResult.objects.filter(
            screening__doctor=doctor, 
            metabolic_risk_score__gt=60
        ).select_related('screening', 'screening__patient').order_by('-created_at')[:5]
        
        alerts = []
        for result in high_risk_patients:
            alerts.append({
                'patient_name': result.screening.patient.full_name,
                'risk_score': result.metabolic_risk_score,
                'date': result.created_at
            })

        return Response({
            'stats': {
                'total_patients': total_patients,
                'screenings_this_week': screenings_this_week,
                'pending_reviews': pending_reviews
            },
            'recent_activity': recent_serializer.data,
            'alerts': alerts
        })

class PatientDashboardView(views.APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request):
        if not hasattr(request.user, 'patient_profile'):
             return Response({'error': 'User is not a patient'}, status=status.HTTP_403_FORBIDDEN)
        
        patient = request.user.patient_profile
        
        # Latest Screening
        latest_screening = Screening.objects.filter(patient=patient).order_by('-created_at').first()
        latest_result = None
        if latest_screening and hasattr(latest_screening, 'ai_result'):
            latest_result = AIResultSerializer(latest_screening.ai_result).data
            
        # Trends (simplified - last 5 scores)
        history = AIResult.objects.filter(screening__patient=patient).order_by('created_at')[:5]
        trends = [{'date': h.created_at.date(), 'score': h.metabolic_risk_score} for h in history]

        return Response({
            'patient_name': patient.full_name,
            'latest_result': latest_result,
            'trends': trends,
            'tips': ["Drink water", "Walk 30 mins"] # Placeholder for dynamic tips
        })
