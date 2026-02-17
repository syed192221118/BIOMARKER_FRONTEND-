from django.db import models
from django.conf import settings

class PatientProfile(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='patient_profile')
    
    # Demographics
    age = models.IntegerField(null=True)
    gender = models.CharField(max_length=10, choices=(('Male', 'Male'), ('Female', 'Female'), ('Other', 'Other')), null=True)
    height = models.FloatField(help_text="Height in cm", null=True)
    weight = models.FloatField(help_text="Weight in kg", null=True)
    
    # Medical Data (JSON for flexibility)
    lifestyle_data = models.JSONField(default=dict, blank=True, help_text="Smoking, Alcohol, Exercise")
    family_history = models.JSONField(default=list, blank=True, help_text="List of conditions")
    conditions = models.JSONField(default=list, blank=True, help_text="Existing conditions")
    
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"Patient: {self.user.username}"
