from django.db import models
from .screening import Screening

class AIResult(models.Model):
    screening = models.OneToOneField(Screening, on_delete=models.CASCADE, related_name='ai_result')
    metabolic_risk_score = models.IntegerField(help_text="0-100")
    diabetes_1yr_risk = models.FloatField(help_text="Percentage 0-100", null=True)
    diabetes_5yr_risk = models.FloatField(help_text="Percentage 0-100", null=True)
    heart_disease_risk = models.FloatField(help_text="Percentage 0-100", null=True)
    fatty_liver_risk = models.FloatField(help_text="Percentage 0-100", null=True)
    insulin_resistance_score = models.FloatField(help_text="HOMA-IR or similar", null=True)
    metabolic_syndrome_flag = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"AI Result for Screening {self.screening.id}"

class RiskFactor(models.Model):
    ai_result = models.ForeignKey(AIResult, on_delete=models.CASCADE, related_name='risk_factors')
    risk_name = models.CharField(max_length=100)
    contribution_percentage = models.FloatField(help_text="0-100")
    description = models.TextField(blank=True)

    def __str__(self):
        return f"{self.risk_name} ({self.contribution_percentage}%)"

class Recommendation(models.Model):
    ai_result = models.ForeignKey(AIResult, on_delete=models.CASCADE, related_name='recommendations')
    category = models.CharField(max_length=50, help_text="Lifestyle, Medical, Diet")
    description = models.TextField()
    priority = models.CharField(max_length=20, choices=(('High', 'High'), ('Medium', 'Medium'), ('Low', 'Low')), default='Medium')

    def __str__(self):
        return f"{self.category}: {self.description[:50]}..."
