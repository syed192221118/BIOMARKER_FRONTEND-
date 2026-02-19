from django.contrib import admin
from .models import AIAnalysisResult

@admin.register(AIAnalysisResult)
class AIAnalysisResultAdmin(admin.ModelAdmin):
    list_display = ('screening', 'metabolic_score', 'diabetes_risk_1yr', 'created_at')
    search_fields = ('screening__patient__user__username',)
