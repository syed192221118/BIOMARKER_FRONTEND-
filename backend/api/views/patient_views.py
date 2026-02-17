from rest_framework import viewsets, permissions
from ..models.patient import Patient
from ..serializers.patient_serializer import PatientSerializer

class PatientViewSet(viewsets.ModelViewSet):
    queryset = Patient.objects.all()
    serializer_class = PatientSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
            if hasattr(user, 'doctor_profile'):
                return Patient.objects.filter(assigned_doctor=user.doctor_profile)
            return Patient.objects.none()
        if user.role == 'patient':
             return Patient.objects.filter(user=user)
        return Patient.objects.none()

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)
