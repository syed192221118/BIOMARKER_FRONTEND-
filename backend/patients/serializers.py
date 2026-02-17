from rest_framework import serializers
from .models import PatientProfile
from accounts.serializers import UserSerializer

class PatientProfileSerializer(serializers.ModelSerializer):
    user = UserSerializer(read_only=True)
    class Meta:
        model = PatientProfile
        fields = '__all__'
