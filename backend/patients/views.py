from rest_framework import viewsets, permissions
from rest_framework.decorators import action
from rest_framework.response import Response
from .models import PatientProfile
from .serializers import PatientProfileSerializer
from accounts.permissions import IsDoctor, IsPatient
from screenings.models import Screening
from screenings.serializers import ScreeningSerializer

class PatientProfileViewSet(viewsets.ModelViewSet):
    serializer_class = PatientProfileSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
            return PatientProfile.objects.all()
        return PatientProfile.objects.filter(user=user)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    @action(detail=False, methods=['get'])
    def profile(self, request):
        patient = self.get_queryset().first()
        if not patient:
            return Response({"error": "No patient profile found"}, status=404)
        
        # Wrapped in list because Android ApiService expects List<PatientProfile>
        return Response([PatientProfileSerializer(patient).data])
