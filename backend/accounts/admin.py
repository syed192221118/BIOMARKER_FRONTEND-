from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from .models import User, DoctorProfile

@admin.register(User)
class UserAdmin(BaseUserAdmin):
    list_display = ('username', 'email', 'name', 'role', 'is_staff')
    fieldsets = BaseUserAdmin.fieldsets + (
        ('Custom Fields', {'fields': ('name', 'role', 'profile_photo')}),
    )
    add_fieldsets = BaseUserAdmin.add_fieldsets + (
        ('Custom Fields', {'fields': ('name', 'role', 'profile_photo')}),
    )

@admin.register(DoctorProfile)
class DoctorProfileAdmin(admin.ModelAdmin):
    list_display = ('user', 'hospital_name', 'specialization', 'license_number')
    search_fields = ('user__username', 'hospital_name', 'license_number')
