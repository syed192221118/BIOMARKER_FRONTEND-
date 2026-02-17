from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, permissions
from django.shortcuts import get_object_or_404
from screenings.models import Screening
from .models import AIAnalysisResult
from .serializers import AIAnalysisResultSerializer

from .services import AIService

class RunAnalysisView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request, screening_id):
        screening = get_object_or_404(Screening, id=screening_id)
        
        if not hasattr(screening, 'biomarker_panel'):
            return Response({"error": "No biomarkers found for this screening"}, status=status.HTTP_400_BAD_REQUEST)
        
        # Use AI Service
        results_data = AIService.calculate_risk(screening.biomarker_panel)
        
        result, created = AIAnalysisResult.objects.update_or_create(
            screening=screening,
            defaults=results_data
        )
        
        screening.status = 'Completed'
        screening.save()
        
        return Response(AIAnalysisResultSerializer(result).data)
