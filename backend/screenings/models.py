from django.db import models
from django.conf import settings

class Screening(models.Model):
    STATUS_CHOICES = (
        ('Draft', 'Draft'),
        ('Pending_Analysis', 'Pending Analysis'),
        ('Completed', 'Completed'),
    )

    patient = models.ForeignKey('patients.PatientProfile', on_delete=models.CASCADE, related_name='screenings')
    doctor = models.ForeignKey('accounts.DoctorProfile', on_delete=models.SET_NULL, null=True, blank=True, related_name='reviewed_screenings')
    
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='Draft')
    
    # JSON Fields for dynamic data
    vitals = models.JSONField(default=dict, blank=True, help_text="BP, Heart Rate, etc.")
    symptoms = models.JSONField(default=list, blank=True, help_text="List of symptoms")
    
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"Screening {self.id} for {self.patient}"
