from django.db import models
from django.conf import settings

class Report(models.Model):
    screening = models.ForeignKey('screenings.Screening', on_delete=models.CASCADE, related_name='reports')
    pdf_file = models.FileField(upload_to='reports/')
    summary = models.TextField(blank=True)
    generated_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Report {self.id} for Screening {self.screening.id}"

