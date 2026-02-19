from rest_framework import views, permissions, status
from django.http import HttpResponse, FileResponse
from ..models.screening import Screening
from ..utils.pdf_generator import generate_screening_report

class ReportView(views.APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, screening_id):
        try:
            screening = Screening.objects.get(id=screening_id)
        except Screening.DoesNotExist:
            return HttpResponse(status=404)

        # Check permission
        if request.user.role == 'patient' and screening.patient.user != request.user:
            return HttpResponse(status=403)
        if request.user.role == 'doctor' and screening.doctor and screening.doctor.user != request.user:
            # Allow doctors to view any screening or just assigned?
            # For now strict
            return HttpResponse(status=403)

        pdf_buffer = generate_screening_report(screening)
        response = HttpResponse(pdf_buffer, content_type='application/pdf')
        response['Content-Disposition'] = f'attachment; filename="report_{screening_id}.pdf"'
        return response
