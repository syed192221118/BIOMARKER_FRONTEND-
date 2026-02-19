
import os
import sys
import django
import json

# Add parent directory to sys.path
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

# Setup Django environment
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from django.test import Client
from django.contrib.auth import get_user_model

User = get_user_model()
client = Client()

def test_predict():
    print("=== TESTING PREDICT API ===")

    # 1. Login
    username = "test_doc"
    password = "verify123" # From previous verification
    
    # 2. Get JWT token
    login_response = client.post(
        '/api/auth/login/', 
        data=json.dumps({"username": username, "password": password}),
        content_type='application/json'
    )
    
    if login_response.status_code != 200:
        print(f"[-] Login failed: {login_response.content}")
        return
        
    token = login_response.json()['access']
    headers = {"HTTP_AUTHORIZATION": f"Bearer {token}"}

    # 3. Simulate AnalysisRequest from Android
    # Readings: 1 (Glucose) -> 145, 2 (LDL) -> 160
    payload = {
        "readings": [
            {"biomarker_id": 1, "value": 145.0},
            {"biomarker_id": 2, "value": 160.0}
        ]
    }
    
    print(f"[+] Sending payload: {json.dumps(payload)}")
    
    response = client.post(
        '/api/analysis/predict/', 
        data=json.dumps(payload),
        content_type='application/json',
        **headers
    )
    
    print(f"[+] Status Code: {response.status_code}")
    print(f"[+] Response: {json.dumps(response.json(), indent=2)}")
    
    if response.status_code == 200:
        data = response.json()
        print(f"[+] Metabolic Score: {data['metabolic_score']}")
        print(f"[+] Risk Level: {data['risk_level']}")
        print(f"[+] Insights: {data['insights']}")
        print("=== TEST PASSED ===")
    else:
        print("=== TEST FAILED ===")

if __name__ == "__main__":
    test_predict()
