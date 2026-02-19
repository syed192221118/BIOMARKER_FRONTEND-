from django.contrib import admin
from .models.user import User
from .models.doctor import Doctor
from .models.patient import Patient
from .models.screening import Screening, Biomarker, ScreeningBiomarker
from .models.ai import AIResult, RiskFactor, Recommendation

@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = ('email', 'role', 'is_active', 'is_staff')
    search_fields = ('email',)

@admin.register(Doctor)
class DoctorAdmin(admin.ModelAdmin):
    list_display = ('full_name', 'specialization', 'hospital_name')

@admin.register(Patient)
class PatientAdmin(admin.ModelAdmin):
    list_display = ('full_name', 'age', 'gender')

class ScreeningBiomarkerInline(admin.TabularInline):
    model = ScreeningBiomarker
    extra = 0

@admin.register(Screening)
class ScreeningAdmin(admin.ModelAdmin):
    list_display = ('patient', 'doctor', 'status', 'created_at')
    inlines = [ScreeningBiomarkerInline]

@admin.register(Biomarker)
class BiomarkerAdmin(admin.ModelAdmin):
    list_display = ('name', 'symbol', 'unit', 'min_normal', 'max_normal')

class RiskFactorInline(admin.TabularInline):
    model = RiskFactor
    extra = 0

class RecommendationInline(admin.TabularInline):
    model = Recommendation
    extra = 0

@admin.register(AIResult)
class AIResultAdmin(admin.ModelAdmin):
    list_display = ('screening', 'metabolic_risk_score', 'risk_level_display')
    inlines = [RiskFactorInline, RecommendationInline]
    
    def risk_level_display(self, obj):
        return f"{obj.metabolic_risk_score}/100"
