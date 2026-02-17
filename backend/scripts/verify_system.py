import requests
import json

BASE_URL = "http://127.0.0.1:8000/api"

def test_flow():
    session = requests.Session()
    
    # 1. Register Patient
    print("\n--- Registering Patient ---")
    reg_data = {
        "email": "testpat@example.com",
        "password": "password123",
        "role": "patient"
    }
    try:
        res = session.post(f"{BASE_URL}/auth/register/", json=reg_data)
        if res.status_code == 201:
            print("Registered successfully")
        elif res.status_code == 400 and 'email' in res.json():
            print("User already exists")
        else:
             print(f"Failed: {res.text}")
             return
    except Exception as e:
        print(f"Connection failed: {e}")
        return

    # 2. Login
    print("\n--- Logging In ---")
    login_data = {"email": "testpat@example.com", "password": "password123"}
    res = session.post(f"{BASE_URL}/auth/login/", json=login_data)
    if res.status_code != 200:
        print(f"Login failed: {res.text}")
        return
    token = res.json()['access']
    headers = {"Authorization": f"Bearer {token}"}
    print("Logged in, got token")

    # 3. Create Patient Profile
    print("\n--- Creating Profile ---")
    # Check if profile exists
    res = session.get(f"{BASE_URL}/patients/", headers=headers)
    if len(res.json()) == 0:
        profile_data = {
            "full_name": "Test Patient",
            "age": 30,
            "gender": "Male",
            "height": 175,
            "weight": 75
        }
        res = session.post(f"{BASE_URL}/patients/", json=profile_data, headers=headers)
        print(f"Profile creation: {res.status_code}")
    else:
        print("Profile already exists")

    # 4. Get Biomarkers
    print("\n--- Fetching Biomarkers ---")
    res = session.get(f"{BASE_URL}/biomarkers/", headers=headers)
    biomarkers = res.json()
    print(f"Found {len(biomarkers)} biomarkers")
    fpg_id = next((b['id'] for b in biomarkers if b['symbol'] == 'FPG'), None)

    # 5. Create Screening
    print("\n--- Creating Screening ---")
    screening_data = {
        "biomarkers": [
            {"biomarker_id": fpg_id, "value": 110} # High Glucose
        ]
    }
    res = session.post(f"{BASE_URL}/screenings/", json=screening_data, headers=headers)
    if res.status_code != 201:
        print(f"Screening failed: {res.text}")
        return
    screening_id = res.json()['id']
    print(f"Created Screening ID: {screening_id}")

    # 6. Submit Screening (Trigger AI)
    print("\n--- Submitting Screening ---")
    res = session.post(f"{BASE_URL}/screenings/{screening_id}/submit/", headers=headers)
    print(f"Submission: {res.json()}")

    # 7. Check AI Result
    print("\n--- Checking AI Result ---")
    res = session.get(f"{BASE_URL}/ai-results/", headers=headers)
    results = res.json()
    my_result = next((r for r in results if r['screening'] == screening_id), None)
    if my_result:
        print(f"AI Score: {my_result['metabolic_risk_score']}")
    else:
        print("AI Result not found yet")

    # 8. Download Report PDF
    print("\n--- Downloading Report ---")
    res = session.get(f"{BASE_URL}/reports/{screening_id}/", headers=headers)
    if res.status_code == 200:
        with open(f"report_{screening_id}.pdf", "wb") as f:
            f.write(res.content)
        print(f"Report downloaded as report_{screening_id}.pdf")
    else:
        print(f"Download failed: {res.status_code}")

if __name__ == "__main__":
    test_flow()
