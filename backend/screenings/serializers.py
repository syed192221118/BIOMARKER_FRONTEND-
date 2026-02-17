from rest_framework import serializers
from .models import Screening
from biomarkers.models import BiomarkerPanel
from patients.serializers import PatientProfileSerializer

class BiomarkerPanelSerializer(serializers.ModelSerializer):
    class Meta:
        model = BiomarkerPanel
        fields = '__all__'
        read_only_fields = ('screening',)

class ScreeningSerializer(serializers.ModelSerializer):
    # Nested serializers for read; standard fields for write
    biomarker_panel = BiomarkerPanelSerializer(read_only=True)
    
    class Meta:
        model = Screening
        fields = '__all__'
        read_only_fields = ('doctor', 'created_at', 'updated_at')
