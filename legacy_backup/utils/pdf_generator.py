from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Table, TableStyle, Paragraph, Spacer
from reportlab.lib.styles import getSampleStyleSheet
from io import BytesIO
from ..models.screening import ScreeningBiomarker
from ..models.ai import RiskFactor, Recommendation

def generate_screening_report(screening):
    buffer = BytesIO()
    doc = SimpleDocTemplate(buffer, pagesize=letter)
    elements = []
    styles = getSampleStyleSheet()

    # Title
    elements.append(Paragraph(f"BiomarkerAI Health Report", styles['Title']))
    elements.append(Spacer(1, 12))

    # Patient Info
    p = screening.patient
    info_text = f"""
    <b>Patient Name:</b> {p.full_name}<br/>
    <b>Age:</b> {p.age} | <b>Gender:</b> {p.gender}<br/>
    <b>Date:</b> {screening.created_at.strftime('%Y-%m-%d')}<br/>
    <b>Doctor:</b> {screening.doctor.full_name if screening.doctor else 'Unassigned'}
    """
    elements.append(Paragraph(info_text, styles['Normal']))
    elements.append(Spacer(1, 12))

    # AI Summary
    if hasattr(screening, 'ai_result'):
        res = screening.ai_result
        score_text = f"<b>Metabolic Risk Score:</b> {res.metabolic_risk_score} / 100"
        elements.append(Paragraph(score_text, styles['Heading2']))
        
        # Risk Factors
        factors = RiskFactor.objects.filter(ai_result=res)
        if factors.exists():
            elements.append(Paragraph("Risk Factors Identified:", styles['Heading3']))
            for f in factors:
                elements.append(Paragraph(f"- {f.risk_name} ({f.contribution_percentage}%)", styles['Normal']))
        elements.append(Spacer(1, 12))

    # Biomarker Table
    data = [['Biomarker', 'Value', 'Unit', 'Status']]
    bms = ScreeningBiomarker.objects.filter(screening=screening)
    for index, b in enumerate(bms):
        status = "Abnormal" if b.is_abnormal else "Normal"
        row = [b.biomarker.symbol, str(b.value), b.biomarker.unit, status]
        data.append(row)

    t = Table(data)
    t.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
        ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
        ('GRID', (0, 0), (-1, -1), 1, colors.black),
    ]))
    elements.append(t)
    elements.append(Spacer(1, 24))

    # Recommendations
    if hasattr(screening, 'ai_result'):
        recs = Recommendation.objects.filter(ai_result=screening.ai_result)
        if recs.exists():
            elements.append(Paragraph("Recommendations:", styles['Heading2']))
            for r in recs:
                rec_text = f"<b>[{r.category}]</b> {r.description}"
                elements.append(Paragraph(rec_text, styles['Normal']))
                elements.append(Spacer(1, 6))

    doc.build(elements)
    buffer.seek(0)
    return buffer
