from rest_framework import serializers
from .models import Screening
from biomarkers.models import BiomarkerPanel
from patients.serializers import PatientProfileSerializer

from patients.models import PatientProfile

class BiomarkerPanelSerializer(serializers.ModelSerializer):
    class Meta:
        model = BiomarkerPanel
        fields = '__all__'
        read_only_fields = ('screening',)

class ScreeningSerializer(serializers.ModelSerializer):
    # Nested serializers for read; standard fields for write
    biomarker_panel = BiomarkerPanelSerializer(read_only=True)
    patient_email = serializers.EmailField(write_only=True, required=False)
    patient = serializers.PrimaryKeyRelatedField(queryset=PatientProfile.objects.all(), required=False)
    
    class Meta:
        model = Screening
        fields = ['id', 'patient', 'patient_email', 'doctor', 'status', 'vitals', 'symptoms', 'biomarker_panel', 'created_at', 'updated_at']
        read_only_fields = ('doctor', 'created_at', 'updated_at')

    def validate(self, attrs):
        patient_email = attrs.get('patient_email')
        patient = attrs.get('patient')

        if not patient and not patient_email:
            raise serializers.ValidationError("Either patient ID or patient_email must be provided.")

        if not patient and patient_email:
            try:
                patient_profile = PatientProfile.objects.get(user__email=patient_email)
                attrs['patient'] = patient_profile
            except PatientProfile.DoesNotExist:
                raise serializers.ValidationError({"patient_email": "No patient found with this email address."})
        
        # Cleanup
        attrs.pop('patient_email', None)
        return attrs
