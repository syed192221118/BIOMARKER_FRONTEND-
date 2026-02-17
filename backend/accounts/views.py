from rest_framework import generics, permissions, status, viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework_simplejwt.views import TokenObtainPairView
from django.contrib.auth import get_user_model
from .serializers import UserSerializer, DoctorProfileSerializer
from .models import DoctorProfile
from .permissions import IsDoctor

User = get_user_model()

class RegisterView(generics.CreateAPIView):
    queryset = User.objects.all()
    permission_classes = (permissions.AllowAny,)
    serializer_class = UserSerializer



class DoctorProfileViewSet(viewsets.ModelViewSet):
    queryset = DoctorProfile.objects.all()
    serializer_class = DoctorProfileSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return DoctorProfile.objects.all()

    @action(detail=False, methods=['get'])
    def profile(self, request):
        doctor = DoctorProfile.objects.filter(user=request.user).first()
        if not doctor:
            return Response({"error": "No doctor profile found"}, status=404)
        return Response([DoctorProfileSerializer(doctor).data])

class CurrentUserView(generics.RetrieveAPIView):
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_object(self):
        return self.request.user
