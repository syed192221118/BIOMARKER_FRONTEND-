from rest_framework import serializers
from ..models.doctor import Doctor
from .user_serializer import UserSerializer

class DoctorSerializer(serializers.ModelSerializer):
    user = UserSerializer(read_only=True)
    
    class Meta:
        model = Doctor
        fields = '__all__'
