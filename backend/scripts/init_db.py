import os
import sys
import django

# Add backend to sys.path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "biomarker_backend.settings")
django.setup()

from api.models.screening import Biomarker
from api.models.user import User
from api.models.doctor import Doctor
from api.models.patient import Patient

def create_biomarkers():
    biomarkers = [
        # Glucose
        {"name": "Fasting Glucose", "symbol": "FPG", "category": "Glucose", "unit": "mg/dL", "min": 70, "max": 100},
        {"name": "Postprandial Glucose", "symbol": "PPG", "category": "Glucose", "unit": "mg/dL", "min": 70, "max": 140},
        {"name": "HbA1c", "symbol": "HbA1c", "category": "Glucose", "unit": "%", "min": 4.0, "max": 5.6},
        # Lipid
        {"name": "Total Cholesterol", "symbol": "TC", "category": "Lipid", "unit": "mg/dL", "min": 125, "max": 200},
        {"name": "LDL Cholesterol", "symbol": "LDL", "category": "Lipid", "unit": "mg/dL", "min": 0, "max": 100},
        {"name": "HDL Cholesterol", "symbol": "HDL", "category": "Lipid", "unit": "mg/dL", "min": 40, "max": 60},
        {"name": "Triglycerides", "symbol": "TG", "category": "Lipid", "unit": "mg/dL", "min": 0, "max": 150},
        # Kidney
        {"name": "Creatinine", "symbol": "Cr", "category": "Kidney", "unit": "mg/dL", "min": 0.6, "max": 1.2},
        {"name": "Urea", "symbol": "Urea", "category": "Kidney", "unit": "mg/dL", "min": 7, "max": 20},
         # Liver
        {"name": "ALT", "symbol": "ALT", "category": "Liver", "unit": "U/L", "min": 7, "max": 56},
        {"name": "AST", "symbol": "AST", "category": "Liver", "unit": "U/L", "min": 10, "max": 40},
        # Inflammation
        {"name": "C-Reactive Protein", "symbol": "CRP", "category": "Inflammation", "unit": "mg/dL", "min": 0, "max": 0.3},
        # Hormones
        {"name": "Insulin", "symbol": "Insulin", "category": "Hormones", "unit": "uIU/mL", "min": 2.6, "max": 24.9},
        {"name": "TSH", "symbol": "TSH", "category": "Hormones", "unit": "mIU/L", "min": 0.4, "max": 4.0},
    ]

    print("Creating Biomarkers...")
    for b in biomarkers:
        obj, created = Biomarker.objects.get_or_create(
            name=b["name"],
            defaults={
                "symbol": b["symbol"],
                "category": b["category"],
                "unit": b["unit"],
                "min_normal": b["min"],
                "max_normal": b["max"]
            }
        )
        if created:
            print(f"Created {b['symbol']}")
        else:
            print(f"Skipped {b['symbol']} (exists)")

def create_users():
    print("Creating Test Users...")
    # Doctor
    doc_email = "doctor@example.com"
    if not User.objects.filter(email=doc_email).exists():
        doc_user = User.objects.create_user(email=doc_email, password="password123", role='doctor', is_verified=True)
        Doctor.objects.create(
            user=doc_user,
            full_name="Dr. House",
            hospital_name="Princeton Plainsboro",
            specialization="Diagnostician",
            license_number="MD123456"
        )
        print("Created Doctor: doctor@example.com / password123")
    
    # Patient
    pat_email = "patient@example.com"
    if not User.objects.filter(email=pat_email).exists():
        pat_user = User.objects.create_user(email=pat_email, password="password123", role='patient', is_verified=True)
        Patient.objects.create(
            user=pat_user,
            full_name="John Doe",
            age=35,
            gender="Male",
            height=180,
            weight=80,
            assigned_doctor=Doctor.objects.get(user__email=doc_email)
        )
        print("Created Patient: patient@example.com / password123")

    # Superuser
    admin_email = "admin@example.com"
    if not User.objects.filter(email=admin_email).exists():
        User.objects.create_superuser(email=admin_email, password="password123", role='doctor')
        print("Created Admin: admin@example.com / password123")

if __name__ == "__main__":
    create_biomarkers()
    create_users()
    print("Done!")
