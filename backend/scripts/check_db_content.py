
import os
import django
import sys

# Add project root to sys.path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from api.models import Biomarker, Screening, AIResult

print("--- Check Biomarkers ---")
biomarkers = Biomarker.objects.all().values('id', 'name', 'symbol')
for b in biomarkers:
    print(b)

if not biomarkers.exists():
    print("NO BIOMARKERS FOUND! App requests with IDs 1 & 2 will fail (or perform strangely).")

print("\n--- Recent Screenings ---")
screenings = Screening.objects.order_by('-created_at')[:5]
for s in screenings:
    print(f"ID: {s.id}, Pat: {s.patient}, Status: {s.status}, Created: {s.created_at}")
    # Check AI Result
    try:
        print(f"  -> AI Result: {s.ai_result}")
    except:
        print("  -> NO AI RESULT")

print("\n--- Recent AI Results ---")
results = AIResult.objects.order_by('-created_at')[:5]
for r in results:
    print(f"ID: {r.id}, Screening: {r.screening.id}, Score: {r.metabolic_risk_score}")
