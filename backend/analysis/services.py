class AIService:
    @staticmethod
    def calculate_risk(biomarker_panel):
        bp = biomarker_panel
        risk_score = 10
        flags = []
        abnormal = []
        
        # Fasting Glucose
        if bp.glucose_fasting:
            if bp.glucose_fasting > 126:
                risk_score += 40
                flags.append("Diabetes Range (Fasting)")
                abnormal.append("glucose_fasting")
            elif bp.glucose_fasting > 100:
                risk_score += 15
                flags.append("Prediabetes Range (Fasting)")
                abnormal.append("glucose_fasting")
        
        # PP Glucose
        if bp.glucose_pp and bp.glucose_pp > 200:
            risk_score += 30
            flags.append("High PP Glucose")
            abnormal.append("glucose_pp")
            
        # HbA1c
        if bp.hba1c:
            if bp.hba1c > 6.5:
                risk_score += 40
                flags.append("High HbA1c (Diabetes)")
                abnormal.append("hba1c")
            elif bp.hba1c > 5.7:
                risk_score += 15
                flags.append("Elevated HbA1c")
                abnormal.append("hba1c")
        
        # Lipid Profile
        if bp.hdl and bp.hdl < 40:
            risk_score += 10
            flags.append("Low HDL (Good Cholesterol)")
            abnormal.append("hdl")
        if bp.ldl and bp.ldl > 130:
            risk_score += 15
            flags.append("High LDL (Bad Cholesterol)")
            abnormal.append("ldl")
        if bp.triglycerides and bp.triglycerides > 150:
            risk_score += 10
            flags.append("High Triglycerides")
            abnormal.append("triglycerides")
            
        # Kidney/Liver (Simplified)
        if bp.creatinine and bp.creatinine > 1.2:
            flags.append("Elevated Creatinine")
            abnormal.append("creatinine")
        if bp.alt and bp.alt > 40:
            flags.append("Elevated ALT")
            abnormal.append("alt")

        return {
            'metabolic_score': min(risk_score, 100),
            'diabetes_risk_1yr': min(risk_score * 0.4, 100),
            'diabetes_risk_5yr': min(risk_score * 0.7, 100),
            'heart_risk': min(risk_score * 0.3, 100),
            'fatty_liver_risk': min(risk_score * 0.5, 100),
            'obesity_risk': min(risk_score * 0.2, 100),
            'insulin_resistance_score': min(risk_score * 0.6, 100),
            'syndrome_flags': flags,
            'abnormal_markers': abnormal
        }
