from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status, permissions
from django.shortcuts import get_object_or_404
from screenings.models import Screening
from .models import AIAnalysisResult
from .serializers import AIAnalysisResultSerializer

from .services import AIService
from biomarker_backend.utils import audit_log

class RunAnalysisView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request, screening_id):
        screening = get_object_or_404(Screening, id=screening_id)
        
        if not hasattr(screening, 'biomarker_panel'):
            return Response({"error": "No biomarkers found for this screening"}, status=status.HTTP_400_BAD_REQUEST)
        
        # Use automated AI Service flow
        result = AIService.run_full_analysis(screening)
        
        audit_log(request.user, "RUN_ANALYSIS", f"Screening ID: {screening.id}")
        
        return Response(AIAnalysisResultSerializer(result).data)

class PredictAnalysisView(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request):
        readings = request.data.get('readings', [])
        
        # Mapping biomarker IDs to field names
        mapping = {
            1: "glucose_fasting",
            2: "ldl",
            3: "hba1c",
            4: "triglycerides",
            5: "hdl",
            6: "cholesterol",
            7: "creatinine",
            8: "urea",
            9: "alt",
            10: "ast",
            11: "glucose_pp",
            12: "insulin",
            13: "tsh",
            14: "crp",
            15: "esr"
        }
        
        # All potential fields from BiomarkerPanel
        all_fields = [
            "glucose_fasting", "glucose_pp", "hba1c", 
            "hdl", "ldl", "triglycerides", "cholesterol",
            "creatinine", "urea", "alt", "ast",
            "insulin", "tsh", "crp", "esr"
        ]
        
        # Create a mock data object for the AI Service
        class MockPanel:
            def __init__(self, data):
                for field in all_fields:
                    setattr(self, field, data.get(field))

        panel_data = {}
        for item in readings:
            b_id = item.get('biomarker_id')
            val = item.get('value')
            if b_id in mapping:
                panel_data[mapping[b_id]] = val
        
        mock_panel = MockPanel(panel_data)
        results = AIService.calculate_risk(mock_panel)
        
        # Format response as expected by Android AnalysisResponse
        # riskLevel mapping based on metabolic_score
        score = results['metabolic_score']
        risk_level = "Low"
        if score > 70:
            risk_level = "High"
        elif score > 30:
            risk_level = "Moderate"
            
        insights = results['syndrome_flags']
        if not insights:
            insights = ["Your biomarker levels are within normal ranges."]
            
        return Response({
            "metabolic_score": score,
            "risk_level": risk_level,
            "insights": insights
        })
