import os
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.conf import settings
from .models import Report
from .serializers import ReportSerializer
from screenings.models import Screening
from reportlab.pdfgen import canvas
from django.core.files.base import ContentFile
import io

class ReportViewSet(viewsets.ModelViewSet):
    serializer_class = ReportSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if user.role == 'doctor':
            return Report.objects.all()
        return Report.objects.filter(screening__patient__user=user)

    @action(detail=False, methods=['post'], url_path=r'generate/(?P<screening_id>\d+)')
    def generate(self, request, screening_id=None):
        screening = Screening.objects.filter(id=screening_id).first()
        if not screening:
            return Response({"error": "Screening not found"}, status=404)
        
        if screening.status != 'Completed':
            return Response({"error": "Screening analysis not complete"}, status=400)
        
        # Simple PDF Generation
        buffer = io.BytesIO()
        p = canvas.Canvas(buffer)
        p.drawString(100, 800, f"Medical Report for {screening.patient}")
        p.drawString(100, 780, f"Status: {screening.status}")
        p.drawString(100, 760, f"Generated at: {screening.updated_at}")
        
        if hasattr(screening, 'ai_result'):
            res = screening.ai_result
            p.drawString(100, 740, f"Metabolic Score: {res.metabolic_score}")
            p.drawString(100, 720, f"Flags: {', '.join(res.syndrome_flags)}")
        
        p.showPage()
        p.save()
        
        buffer.seek(0)
        report_file = ContentFile(buffer.read(), name=f"report_{screening_id}.pdf")
        
        report = Report.objects.create(
            screening=screening,
            pdf_file=report_file,
            summary=f"Automated AI report for screening {screening_id}"
        )
        
        return Response(ReportSerializer(report).data, status=status.HTTP_201_CREATED)
