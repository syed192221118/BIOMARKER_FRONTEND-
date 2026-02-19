from reportlab.pdfgen import canvas
from django.core.files.base import ContentFile
import io
from .models import Report

def generate_medical_report(screening):
    """
    Generates a PDF report for a completed screening and saves it to the Report model.
    """
    buffer = io.BytesIO()
    p = canvas.Canvas(buffer)
    
    # Header
    p.setFont("Helvetica-Bold", 16)
    p.drawString(100, 800, "Medical Assessment Report")
    
    p.setFont("Helvetica", 12)
    p.drawString(100, 770, f"Patient: {screening.patient}")
    p.drawString(100, 755, f"Screening ID: {screening.id}")
    p.drawString(100, 740, f"Date: {screening.created_at.strftime('%Y-%m-%d %H:%M')}")
    p.drawString(100, 725, f"Status: {screening.status}")
    
    y = 690
    # AI Results
    if hasattr(screening, 'ai_result'):
        res = screening.ai_result
        p.setFont("Helvetica-Bold", 14)
        p.drawString(100, y, "AI Analysis Summary")
        y -= 20
        p.setFont("Helvetica", 12)
        p.drawString(100, y, f"Metabolic Score: {res.metabolic_score}/100")
        y -= 20
        p.drawString(100, y, f"Diabetes Risk (1yr): {res.diabetes_risk_1yr}%")
        y -= 20
        
        if res.syndrome_flags:
            p.drawString(100, y, "Key Insights:")
            y -= 15
            for flag in res.syndrome_flags:
                p.drawString(120, y, f"- {flag}")
                y -= 15
    
    p.showPage()
    p.save()
    
    buffer.seek(0)
    report_file = ContentFile(buffer.read(), name=f"report_{screening.id}.pdf")
    
    report = Report.objects.create(
        screening=screening,
        pdf_file=report_file,
        summary=f"Automated medical report for screening {screening.id}"
    )
    return report
