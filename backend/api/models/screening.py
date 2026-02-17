from django.db import models
from .patient import Patient
from .doctor import Doctor

class Biomarker(models.Model):
    name = models.CharField(max_length=100, unique=True)
    symbol = models.CharField(max_length=20, help_text="e.g. LDL, HbA1c")
    category = models.CharField(max_length=50, blank=True) # e.g. "Lipid", "Glucose"
    unit = models.CharField(max_length=20, blank=True)
    description = models.TextField(blank=True)
    min_normal = models.FloatField(null=True, blank=True)
    max_normal = models.FloatField(null=True, blank=True)

    def __str__(self):
        return f"{self.name} ({self.symbol})"

class Screening(models.Model):
    STATUS_CHOICES = (
        ('Draft', 'Draft'),
        ('Submitted', 'Submitted'),
        ('AI_Processing', 'AI Processing'),
        ('Completed', 'Completed'),
    )
    
    patient = models.ForeignKey(Patient, on_delete=models.CASCADE, related_name='screenings')
    doctor = models.ForeignKey(Doctor, on_delete=models.SET_NULL, null=True, blank=True, related_name='reviewed_screenings')
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='Draft')
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    # Vitals
    systolic_bp = models.IntegerField(null=True, blank=True)
    diastolic_bp = models.IntegerField(null=True, blank=True)
    heart_rate = models.IntegerField(null=True, blank=True)
    temperature = models.FloatField(null=True, blank=True)
    spo2 = models.IntegerField(null=True, blank=True)

    # Physical Snapshot
    height = models.FloatField(null=True, blank=True, help_text="Current height in cm")
    weight = models.FloatField(null=True, blank=True, help_text="Current weight in kg")

    # Lifestyle
    smoking_status = models.CharField(max_length=20, choices=(('Never', 'Never'), ('Former', 'Former'), ('Current', 'Current')), default='Never')
    alcohol_frequency = models.CharField(max_length=20, blank=True)
    exercise_frequency = models.CharField(max_length=20, blank=True)
    diet_type = models.CharField(max_length=50, blank=True)
    sleep_hours = models.FloatField(null=True, blank=True)

    # Symptoms
    symptoms = models.JSONField(default=list, blank=True)

    notes = models.TextField(blank=True)

    def __str__(self):
        return f"Screening for {self.patient.full_name} - {self.status}"

class ScreeningBiomarker(models.Model):
    screening = models.ForeignKey(Screening, on_delete=models.CASCADE, related_name='biomarkers')
    biomarker = models.ForeignKey(Biomarker, on_delete=models.CASCADE)
    value = models.FloatField()
    is_abnormal = models.BooleanField(default=False)

    class Meta:
        unique_together = ('screening', 'biomarker')

    def save(self, *args, **kwargs):
        # Auto-flag abnormal values
        if self.biomarker.min_normal is not None and self.biomarker.max_normal is not None:
             if self.value < self.biomarker.min_normal or self.value > self.biomarker.max_normal:
                 self.is_abnormal = True
             else:
                 self.is_abnormal = False
        super().save(*args, **kwargs)

    def __str__(self):
        return f"{self.biomarker.symbol}: {self.value}"
