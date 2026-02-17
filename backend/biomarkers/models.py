from django.db import models

class BiomarkerPanel(models.Model):
    screening = models.OneToOneField('screenings.Screening', on_delete=models.CASCADE, related_name='biomarker_panel')
    
    # Glucose Profile
    glucose_fasting = models.FloatField(null=True, blank=True)
    glucose_pp = models.FloatField(null=True, blank=True)
    hba1c = models.FloatField(null=True, blank=True)
    
    # Lipid Profile
    hdl = models.FloatField(null=True, blank=True)
    ldl = models.FloatField(null=True, blank=True)
    triglycerides = models.FloatField(null=True, blank=True)
    cholesterol = models.FloatField(null=True, blank=True, help_text="Total Cholesterol")
    
    # Kidney
    creatinine = models.FloatField(null=True, blank=True)
    urea = models.FloatField(null=True, blank=True)
    
    # Liver
    alt = models.FloatField(null=True, blank=True)
    ast = models.FloatField(null=True, blank=True)
    
    # Others
    insulin = models.FloatField(null=True, blank=True)
    tsh = models.FloatField(null=True, blank=True)
    crp = models.FloatField(null=True, blank=True)
    esr = models.FloatField(null=True, blank=True)

    def __str__(self):
        return f"Biomarkers for Screening {self.screening.id}"
