
import os
import django
import random
from datetime import timedelta
from django.utils import timezone

# Setup Django environment
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from api.models import User, Biomarker, HealthRecord, BiomarkerReading, AnalysisResult

def populate():
    print("Populating data...")

    # 1. Create Users
    # Doctor
    doctor, created = User.objects.get_or_create(username='dr_strange', email='dr.strange@hospital.com')
    if created:
        doctor.set_password('password')
        doctor.role = 'doctor'
        doctor.save()
        print(f"Created Doctor: {doctor.username}")
    else:
        print(f"Doctor {doctor.username} already exists")

    # Patient
    patient, created = User.objects.get_or_create(username='peter_parker', email='peter@dailybugle.com')
    if created:
        patient.set_password('password')
        patient.role = 'patient'
        patient.save()
        print(f"Created Patient: {patient.username}")
    else:
        print(f"Patient {patient.username} already exists")

    # 2. Create Biomarkers (Common ones)
    biomarkers_data = [
        {"name": "Glucose", "symbol": "GLU", "unit": "mg/dL", "min": 70, "max": 99},
        {"name": "Total Cholesterol", "symbol": "TC", "unit": "mg/dL", "min": 125, "max": 200},
        {"name": "Hemoglobin A1c", "symbol": "HbA1c", "unit": "%", "min": 4.0, "max": 5.6},
        {"name": "Triglycerides", "symbol": "TG", "unit": "mg/dL", "min": 0, "max": 150},
        {"name": "LDL Cholesterol", "symbol": "LDL", "unit": "mg/dL", "min": 0, "max": 100},
    ]

    biomarker_objs = {}
    for data in biomarkers_data:
        b, created = Biomarker.objects.get_or_create(
            name=data["name"],
            defaults={
                "symbol": data["symbol"],
                "unit": data["unit"],
                "min_normal": data["min"],
                "max_normal": data["max"]
            }
        )
        biomarker_objs[data["symbol"]] = b
        if created:
            print(f"Created Biomarker: {b.name}")

    # 3. Create Health Records and Analysis
    # Create a record for Peter Parker
    record, created = HealthRecord.objects.get_or_create(
        patient=patient,
        notes="Routine annual checkup. patient complains of slight fatigue."
    )
    
    if created:
        print(f"Created Health Record for {patient.username}")
        # Add readings
        readings = [
            ("GLU", 105.0), # Slightly high
            ("TC", 180.0),  # Normal
            ("HbA1c", 5.8), # Prediabetes
            ("TG", 160.0),  # High
        ]

        for symbol, value in readings:
            if symbol in biomarker_objs:
                BiomarkerReading.objects.create(
                    record=record,
                    biomarker=biomarker_objs[symbol],
                    value=value
                )
        print("Added readings to record.")

        # Create Analysis Result
        AnalysisResult.objects.create(
            record=record,
            metabolic_score=75,
            risk_level="Moderate",
            insights=[
                "Glucose levels are slightly elevated, indicating potential pre-diabetes.",
                "Triglycerides are high. Recommend dietary changes.",
                "Overall metabolic health is stable but requires monitoring."
            ]
        )
        print("Created Analysis Result.")
    else:
        print("Health Record already exists.")

    print("Population complete.")

if __name__ == '__main__':
    populate()
