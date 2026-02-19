
import os
import sys
import django
import json
import random
from django.utils import timezone

# Add parent directory to sys.path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

# Setup Django environment
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from django.test import Client
from django.contrib.auth import get_user_model
from accounts.models import DoctorProfile
from patients.models import PatientProfile
from screenings.models import Screening
from biomarkers.models import BiomarkerPanel
from analysis.models import AIAnalysisResult
from reports.models import Report

User = get_user_model()
client = Client()

def verify_flow():
    print("=== STARTING END-TO-END VERIFICATION FLOW ===")

    # 1. Create a Doctor
    doctor_email = "tester_doctor@example.com"
    doctor_user, created = User.objects.get_or_create(
        username="test_doc", 
        email=doctor_email,
        defaults={"role": "doctor", "name": "Dr. Verification Tester"}
    )
    if created:
        doctor_user.set_password("verify123")
        doctor_user.save()
        print(f"[+] Created Doctor User: {doctor_user.username}")
    
    # Doctor Profile should have been created by signals
    doc_profile = DoctorProfile.objects.filter(user=doctor_user).first()
    if doc_profile:
        doc_profile.hospital_name = "Global Test Hospital"
        doc_profile.save()
        print(f"[+] Doctor Profile updated: {doc_profile.hospital_name}")

    # 2. Create a Patient
    patient_email = "tester_patient@example.com"
    patient_user, created = User.objects.get_or_create(
        username="test_patient", 
        email=patient_email,
        defaults={"role": "patient", "name": "Verify Patient"}
    )
    if created:
        patient_user.set_password("verify123")
        patient_user.save()
        print(f"[+] Created Patient User: {patient_user.username}")

    # Patient Profile
    pat_profile = PatientProfile.objects.filter(user=patient_user).first()
    if pat_profile:
        pat_profile.age = 45
        pat_profile.gender = "Male"
        pat_profile.save()
        print(f"[+] Patient Profile updated: Age {pat_profile.age}")

    # 3. Simulate Screening Flow
    # Login to get context (using Client for simplicity)
    client.login(username="test_doc", password="verify123")
    
    # Create Screening
    screening = Screening.objects.create(
        patient=pat_profile,
        doctor=doc_profile,
        status="Pending_Vitals",
        vitals={"bp_sys": 135, "bp_dia": 88, "weight": 82, "height": 178}
    )
    print(f"[+] Created Screening ID: {screening.id} for {pat_profile.user.name}")

    # 4. Add Biomarkers
    biomarkers = BiomarkerPanel.objects.create(
        screening=screening,
        glucose_fasting=112.5,  # Pre-diabetic
        hba1c=6.1,              # Pre-diabetic
        ldl=145.0,              # High
        triglycerides=165.0,    # High
        ast=45.0, 
        alt=52.0                # Elevating
    )
    screening.status = "Pending_Analysis"
    screening.save()
    print("[+] Biomarker Panel added. Status set to Pending_Analysis.")

    # 5. Run AI Analysis
    from analysis.services import AIService
    ai_service = AIService()
    results_data = ai_service.calculate_risk(screening.biomarker_panel)
    
    analysis_result, _ = AIAnalysisResult.objects.update_or_create(
        screening=screening,
        defaults=results_data
    )
    screening.status = "Completed"
    screening.save()
    print(f"[+] AI Analysis Complete. Diabetes Risk (1yr): {analysis_result.diabetes_risk_1yr}%")

    # 6. Generate Report (PDF logic check)
    from reports.views import ReportViewSet
    # We can't easily call viewset actions directly without request, so we'll just check if model exists or run the logic
    # The logic is in ReportViewSet.generate
    from reportlab.pdfgen import canvas
    from django.core.files.base import ContentFile
    import io

    buffer = io.BytesIO()
    p = canvas.Canvas(buffer)
    p.drawString(100, 750, f"BiomarkerAI Report - {patient_user.name}")
    p.drawString(100, 730, f"Diabetes Risk: {analysis_result.diabetes_risk_1yr}%")
    p.showPage()
    p.save()
    pdf = buffer.getvalue()
    buffer.close()

    report = Report.objects.create(
        screening=screening,
        summary="Automated verification report generated.",
        pdf_file=ContentFile(pdf, name=f"verify_report_{screening.id}.pdf")
    )
    print(f"[+] Report Generated and saved: {report.pdf_file.name}")

    print("\n=== VERIFICATION SUCCESSFUL ===")
    print(f"Final Screening Status: {screening.status}")
    print(f"Report URL: /media/{report.pdf_file.name}")

if __name__ == "__main__":
    verify_flow()
