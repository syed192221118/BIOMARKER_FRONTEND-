from django.db import models

class AIAnalysisResult(models.Model):
    screening = models.OneToOneField('screenings.Screening', on_delete=models.CASCADE, related_name='ai_result')
    
    # Scores
    metabolic_score = models.IntegerField(default=0)
    diabetes_risk_1yr = models.FloatField(null=True)
    diabetes_risk_5yr = models.FloatField(null=True)
    heart_risk = models.FloatField(null=True)
    fatty_liver_risk = models.FloatField(null=True)
    obesity_risk = models.FloatField(null=True)
    insulin_resistance_score = models.FloatField(null=True)
    
    # Detailed Data
    syndrome_flags = models.JSONField(default=list, blank=True, help_text="Metabolic Syndrome criteria met")
    abnormal_markers = models.JSONField(default=list, blank=True, help_text="Biomarkers out of range")
    
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Analysis for Screening {self.screening.id}"
