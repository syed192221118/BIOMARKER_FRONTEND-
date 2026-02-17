
import os
import django
import json
import requests

# Setup Django (for ORM access if needed, but we'll use requests for API test)
# Actually, let's use requests against the running server for a true integration test.
# Check if server is running? The user said they will run it.
# But for now I'll assume I can hit localhost:8000 or use Django test client if I want to run locally.
# Let's use Django Test Client to avoid needing the server running right this second, 
# although the user said "process it", implying I should just fix it. 
# But I want to verify.

import sys
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from django.test import Client
from django.contrib.auth import get_user_model
from api.models import Biomarker, Patient

User = get_user_model()
client = Client()

def run_test():
    print("Setting up test data...")
    email = "admin@example.com"
    password = "admin123"
    
    # Ensure admin has doctor profile if not already
    from api.models import Doctor
    user = User.objects.get(email=email)
    user.role = 'doctor' # Ensure role is doctor for this test
    user.save()
    
    if not hasattr(user, 'doctor_profile'):
        Doctor.objects.create(user=user, full_name="Dr. Admin", specialization="General", license_number="ADMIN001")

        
    # Ensure patient profile exists
    if not Patient.objects.filter(user=user).exists():
         Patient.objects.create(
            user=user, 
            full_name="Test App Patient", 
            age=30, 
            gender="Male",
            height=180.0,
            weight=75.0
        )
        
    # 2. Login to get token
    response = client.post('/api/auth/login/', {'email': email, 'password': password}, content_type='application/json')
    if response.status_code != 200:
        print(f"Login failed: {response.content}")
        return

    token = response.json()['access']
    headers = {'HTTP_AUTHORIZATION': f'Bearer {token}'}
    
    # 3. Ensure Biomarkers exist
    fpg, _ = Biomarker.objects.get_or_create(symbol='FPG', defaults={'name': 'Fasting Plasma Glucose', 'unit': 'mg/dL'})
    
    # 4. Simulate Android Payload
    payload = {
        "readings_input": [
            {"biomarker_id": fpg.id, "value": 110.0} # High glucose
        ]
    }
    
    print(f"Sending payload: {json.dumps(payload)}")
    
    # 5. POST to /api/process/
    response = client.post('/api/process/', payload, content_type='application/json', **headers)
    
    print(f"Status Code: {response.status_code}")
    print(f"Response Body: {json.dumps(response.json(), indent=2)}")
    
    if response.status_code == 200:
        print("SUCCESS: Endpoint is working!")
    else:
        print("FAILURE: Endpoint returned error.")

if __name__ == "__main__":
    run_test()
