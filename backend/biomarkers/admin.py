from django.contrib import admin
from .models import BiomarkerPanel

@admin.register(BiomarkerPanel)
class BiomarkerPanelAdmin(admin.ModelAdmin):
    list_display = ('screening', 'glucose_fasting', 'hba1c', 'cholesterol')
    search_fields = ('screening__patient__user__username',)
