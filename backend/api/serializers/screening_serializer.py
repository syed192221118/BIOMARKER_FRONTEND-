from rest_framework import serializers
from ..models.screening import Screening, Biomarker, ScreeningBiomarker
from ..models.patient import Patient

class BiomarkerSerializer(serializers.ModelSerializer):
    class Meta:
        model = Biomarker
        fields = '__all__'

class ScreeningBiomarkerSerializer(serializers.ModelSerializer):
    biomarker_details = BiomarkerSerializer(source='biomarker', read_only=True)
    biomarker_id = serializers.PrimaryKeyRelatedField(
        queryset=Biomarker.objects.all(), source='biomarker', write_only=True
    )

    class Meta:
        model = ScreeningBiomarker
        fields = ('id', 'biomarker_id', 'biomarker_details', 'value', 'is_abnormal')

class ScreeningSerializer(serializers.ModelSerializer):
    biomarkers = ScreeningBiomarkerSerializer(many=True, required=False)
    patient_name = serializers.CharField(source='patient.full_name', read_only=True)
    
    class Meta:
        model = Screening
        fields = '__all__'
        read_only_fields = ('doctor', 'status', 'created_at', 'updated_at', 'patient')

    def create(self, validated_data):
        biomarkers_data = validated_data.pop('biomarkers', [])
        screening = Screening.objects.create(**validated_data)
        
        for bm_data in biomarkers_data:
            ScreeningBiomarker.objects.create(screening=screening, **bm_data)
            
        return screening
