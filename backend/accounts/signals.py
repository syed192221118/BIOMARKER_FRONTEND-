from django.db.models.signals import post_save
from django.dispatch import receiver
from .models import User, DoctorProfile
from patients.models import PatientProfile

@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        if instance.role == 'doctor':
            DoctorProfile.objects.get_or_create(user=instance)
        elif instance.role == 'patient':
            PatientProfile.objects.get_or_create(user=instance)

@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    if instance.role == 'doctor' and hasattr(instance, 'doctor_profile'):
        instance.doctor_profile.save()
    elif instance.role == 'patient' and hasattr(instance, 'patient_profile'):
        instance.patient_profile.save()
