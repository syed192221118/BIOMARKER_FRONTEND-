from rest_framework import viewsets, permissions
from ..models.doctor import Doctor
from ..serializers.doctor_serializer import DoctorSerializer

class DoctorViewSet(viewsets.ModelViewSet):
    queryset = Doctor.objects.all()
    serializer_class = DoctorSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
             # Doctor can see their own profile
             return Doctor.objects.filter(user=user)
        # Patients might see basic info of their assigned doctor
        return Doctor.objects.all()
