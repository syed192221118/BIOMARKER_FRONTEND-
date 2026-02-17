from .models.ai import AIResult, RiskFactor, Recommendation
from .models.screening import ScreeningBiomarker

def calculate_risk_score(screening):
    """
    Simulates a complex ML model for metabolic risk assessment.
    Returns: AIResult object (saved)
    """
    biomarkers = ScreeningBiomarker.objects.filter(screening=screening)
    
    # Base score
    risk_score = 10
    risk_factors = []
    recommendations = []
    
    # Extract values safely
    def get_val(symbol):
        bm = biomarkers.filter(biomarker__symbol=symbol).first()
        return bm.value if bm else None

    # Logic Rules (Expanded)
    fpg = get_val('FPG')
    if fpg and fpg > 100:
        risk_score += 20
        risk_factors.append(("High Fasting Glucose", 20, "Early sign of insulin resistance"))
        recommendations.append(("Diet", "Reduce sugar intake and intermittent fasting.", "High"))

    hba1c = get_val('HbA1c')
    if hba1c and hba1c > 5.7:
        risk_score += 25
        risk_factors.append(("Elevated HbA1c", 25, "Pre-diabetic range"))
        recommendations.append(("Medical", "Consult endocrinologist for further tests.", "High"))

    ldl = get_val('LDL')
    if ldl and ldl > 100:
        risk_score += 15
        risk_factors.append(("High LDL Cholesterol", 15, "Risk of atherosclerosis"))
        recommendations.append(("Lifestyle", "Increase cardio exercise to 30min daily.", "Medium"))

    tg = get_val('TG')
    hdl = get_val('HDL')
    if tg and hdl and (tg / hdl > 2.0):
         risk_score += 10
         risk_factors.append(("High TG/HDL Ratio", 10, "Indicator of metabolic syndrome"))

    # Additional Factors
    if screening.systolic_bp and screening.systolic_bp > 130:
        risk_score += 10
        risk_factors.append(("Hypertension", 10, "High Blood Pressure detected"))
        recommendations.append(("Lifestyle", "Reduce sodium intake.", "High"))

    if screening.smoking_status == 'Current':
        risk_score += 15
        risk_factors.append(("Smoking", 15, "Significant cardiovascular risk"))
        recommendations.append(("Lifestyle", "Consider smoking cessation program.", "High"))
    
    # BMI Calc
    if screening.height and screening.weight:
        height_m = screening.height / 100
        bmi = screening.weight / (height_m * height_m)
        if bmi > 30:
            risk_score += 10
            risk_factors.append(("Obesity", 10, f"BMI is {bmi:.1f}"))
            recommendations.append(("Diet", "Caloric deficit diet recommended.", "High"))

    # Cap score
    risk_score = min(risk_score, 99)
    risk_level = "High" if risk_score > 60 else "Moderate" if risk_score > 30 else "Low"

    # Create Result
    ai_result = AIResult.objects.create(
        screening=screening,
        metabolic_risk_score=risk_score,
        # risk_level is calculated on the fly or not stored

        diabetes_1yr_risk = risk_score * 0.5,
        diabetes_5yr_risk = risk_score * 0.8,
        heart_disease_risk = risk_score * 0.6 + (10 if screening.smoking_status == 'Current' else 0),
        fatty_liver_risk = risk_score * 0.4 + (10 if screening.alcohol_frequency == 'Daily' else 0),
        insulin_resistance_score = (fpg * 20 / 405) if fpg else 0, # rough HOMA-IR approx
        metabolic_syndrome_flag = (risk_score > 50)
    )

    # Save Factors & Recommendations
    for name, contribution, desc in risk_factors:
        RiskFactor.objects.create(
            ai_result=ai_result,
            risk_name=name,
            contribution_percentage=contribution,
            description=desc
        )

    for cat, desc, prio in recommendations:
        Recommendation.objects.create(
            ai_result=ai_result,
            category=cat,
            description=desc,
            priority=prio
        )
    
    # Update Screening Status
    screening.status = 'Completed'
    screening.save()

    return ai_result
