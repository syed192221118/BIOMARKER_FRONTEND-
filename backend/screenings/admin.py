from django.contrib import admin
from .models import Screening

@admin.register(Screening)
class ScreeningAdmin(admin.ModelAdmin):
    list_display = ('id', 'patient', 'doctor', 'status', 'created_at')
    list_filter = ('status', 'created_at')
    search_fields = ('patient__user__username', 'doctor__user__username')
