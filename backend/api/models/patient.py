from django.db import models
from .user import User
from .doctor import Doctor

class Patient(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='patient_profile')
    full_name = models.CharField(max_length=255)
    age = models.IntegerField()
    gender = models.CharField(max_length=10, choices=(('Male', 'Male'), ('Female', 'Female'), ('Other', 'Other')))
    height = models.FloatField(help_text="Height in cm")
    weight = models.FloatField(help_text="Weight in kg")
    lifestyle_summary = models.TextField(blank=True, null=True)
    assigned_doctor = models.ForeignKey(Doctor, on_delete=models.SET_NULL, null=True, blank=True, related_name='patients')
    created_at = models.DateTimeField(auto_now_add=True)
    family_history = models.JSONField(default=dict, blank=True, help_text="List of conditions like Diabetes, Heart Disease")
    medical_conditions = models.JSONField(default=list, blank=True, help_text="List of existing conditions like Thyroid, PCOS")

    def __str__(self):
        return f"{self.full_name} (Patient)"
