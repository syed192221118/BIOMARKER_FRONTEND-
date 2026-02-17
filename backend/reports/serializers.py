from rest_framework import serializers
from .models import Report
from screenings.serializers import ScreeningSerializer

class ReportSerializer(serializers.ModelSerializer):
    screening_details = ScreeningSerializer(source='screening', read_only=True)
    
    class Meta:
        model = Report
        fields = ('id', 'screening', 'screening_details', 'pdf_file', 'summary', 'generated_at')
        read_only_fields = ('pdf_file', 'generated_at')
