from rest_framework import serializers
from ..models.patient import Patient
from .user_serializer import UserSerializer
from .doctor_serializer import DoctorSerializer

class PatientSerializer(serializers.ModelSerializer):
    user = UserSerializer(read_only=True)
    doctor_details = DoctorSerializer(source='assigned_doctor', read_only=True)
    
    class Meta:
        model = Patient
        fields = '__all__'
        extra_kwargs = {'assigned_doctor': {'write_only': True}}
