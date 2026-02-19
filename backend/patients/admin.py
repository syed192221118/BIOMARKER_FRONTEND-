from django.contrib import admin
from django.utils.html import format_html
from .models import PatientProfile
from screenings.models import Screening

class ScreeningInline(admin.TabularInline):
    model = Screening
    extra = 0
    fields = ('status', 'get_metabolic_score', 'get_report_link', 'created_at')
    readonly_fields = ('status', 'get_metabolic_score', 'get_report_link', 'created_at')
    can_delete = False
    show_change_link = True

    def get_metabolic_score(self, obj):
        if hasattr(obj, 'ai_result'):
            return f"{obj.ai_result.metabolic_score}/100"
        return "N/A"
    get_metabolic_score.short_description = "AI Score"

    def get_report_link(self, obj):
        report = obj.reports.first()
        if report and report.pdf_file:
            return format_html('<a href="{}" target="_blank">View Report</a>', report.pdf_file.url)
        return "No Report"
    get_report_link.short_description = "Medical Report"

@admin.register(PatientProfile)
class PatientProfileAdmin(admin.ModelAdmin):
    list_display = ('user', 'age', 'gender', 'created_at')
    search_fields = ('user__username', 'user__name', 'user__email')
    list_filter = ('gender', 'created_at')
    inlines = [ScreeningInline]
