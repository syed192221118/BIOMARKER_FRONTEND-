from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from .models import Screening
from .serializers import ScreeningSerializer, BiomarkerPanelSerializer
from accounts.permissions import IsDoctor, IsPatient
from biomarkers.models import BiomarkerPanel
from biomarker_backend.utils import audit_log
from analysis.services import AIService

class ScreeningViewSet(viewsets.ModelViewSet):
    serializer_class = ScreeningSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
            # Doctor sees all? Or assigned?
            return Screening.objects.all()
        elif hasattr(user, 'patient_profile'):
            return Screening.objects.filter(patient__user=user)
        return Screening.objects.none()

    def perform_create(self, serializer):
        user = self.request.user
        doctor = None
        if user.role == 'doctor' and hasattr(user, 'doctor_profile'):
            doctor = user.doctor_profile
        
        serializer.save(doctor=doctor)

    @action(detail=True, methods=['post'])
    def add_vitals(self, request, pk=None):
        screening = self.get_object()
        screening.vitals.update(request.data)
        screening.save()
        return Response({"status": "Vitals updated", "vitals": screening.vitals})

    @action(detail=True, methods=['post'])
    def add_symptoms(self, request, pk=None):
        screening = self.get_object()
        symptoms = request.data.get('symptoms', [])
        if isinstance(symptoms, list):
            screening.symptoms = list(set(screening.symptoms + symptoms))
            screening.save()
            return Response({"status": "Symptoms updated", "symptoms": screening.symptoms})
        return Response({"error": "Symptoms must be a list"}, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['post'])
    def add_biomarkers(self, request, pk=None):
        screening = self.get_object()
        # Handle update if biomarkers already exist
        if hasattr(screening, 'biomarker_panel'):
            serializer = BiomarkerPanelSerializer(screening.biomarker_panel, data=request.data, partial=True)
        else:
            serializer = BiomarkerPanelSerializer(data=request.data)
        
        if serializer.is_valid():
            serializer.save(screening=screening)
            
            # Trigger Automated Analysis and Reporting
            AIService.run_full_analysis(screening)
            
            audit_log(request.user, "ADD_BIOMARKERS", f"Screening ID: {screening.id}")
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['get'])
    def summary(self, request, pk=None):
        screening = self.get_object()
        return Response({
            "id": screening.id,
            "patient": str(screening.patient),
            "status": screening.status,
            "has_vitals": bool(screening.vitals),
            "has_symptoms": bool(screening.symptoms),
            "has_biomarkers": hasattr(screening, 'biomarker_panel'),
            "created_at": screening.created_at
        })
