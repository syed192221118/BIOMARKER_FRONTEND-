import requests
import json

BASE_URL = "http://127.0.0.1:8000/api/auth/register/"

def test_doctor_signup():
    print(f"--- Simulating Doctor Registration ---")
    
    # Payload matching what DoctorSignUpScreen seems to be sending (with username)
    payload = {
        "username": "Dr House",
        "email": "house@example.com",
        "password": "password123",
        "role": "doctor"
    }
    
    # Note: If backend serializer ignores 'username', this should work.
    # If it crashes, we'll see HTML.
    
    print(f"Sending Payload: {json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(BASE_URL, json=payload)
        
        print(f"Status Code: {response.status_code}")
        if response.status_code != 201:
            print("Response content:")
            print(response.text[:500]) # Print first 500 chars to see if it's HTML
        else:
            print("Success!")
            print(response.json())

    except Exception as e:
        print(f"Connection Error: {e}")

if __name__ == "__main__":
    test_doctor_signup()
