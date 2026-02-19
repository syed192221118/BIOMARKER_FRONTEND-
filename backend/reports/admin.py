from django.contrib import admin
from .models import Report

@admin.register(Report)
class ReportAdmin(admin.ModelAdmin):
    list_display = ('id', 'screening', 'generated_at')
    search_fields = ('screening__patient__user__username',)
