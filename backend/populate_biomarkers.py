import os
import django

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "biomarker_backend.settings")
django.setup()

from api.models import Biomarker

biomarkers = [
    {"name": "Glucose", "symbol": "GLU", "unit": "mg/dL", "min_normal": 70, "max_normal": 99},
    {"name": "LDL Cholesterol", "symbol": "LDL", "unit": "mg/dL", "max_normal": 100},
    {"name": "HbA1c", "symbol": "HbA1c", "unit": "%", "max_normal": 5.7},
]

for b in biomarkers:
    Biomarker.objects.get_or_create(symbol=b["symbol"], defaults=b)

print("Biomarkers populated")
