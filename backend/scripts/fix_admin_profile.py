
import os
import django
import sys

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from django.contrib.auth import get_user_model
from api.models import Patient

User = get_user_model()

try:
    admin_user = User.objects.get(email='admin@example.com')
    print(f"Admin User Found: {admin_user.email} (Role: {admin_user.role})")
    
    try:
        profile = admin_user.patient_profile
        print(f"Admin has Patient Profile: {profile}")
    except Patient.DoesNotExist:
        print("Admin DOES NOT have a Patient Profile. This is the issue.")
        # Fix it for the user automatically?
        print("Creating Patient Profile for Admin...")
        Patient.objects.create(
            user=admin_user, 
            full_name="Admin User", 
            age=30, 
            gender="Other",
            height=170.0,
            weight=70.0
        )
        print("Patient Profile Created. Try app again.")

except User.DoesNotExist:
    print("Admin user not found.")
