import requests
import json

BASE_URL = "http://127.0.0.1:8000/api/auth/register/"

def simulate_app_signup():
    print(f"--- Simulating Android App Registration ---")
    
    # Exact payload structure from Android App
    payload = {
        "email": "mobile_user@example.com",
        "password": "app_password123",
        "role": "patient"
    }
    
    print(f"Sending Payload: {json.dumps(payload, indent=2)}")
    
    try:
        response = requests.post(BASE_URL, json=payload)
        
        if response.status_code == 201:
            print("\nSUCCESS! User registered.")
            print(f"Response: {response.json()}")
            print("\nCHECK ADMIN PANEL NOW:")
            print("1. Go to http://127.0.0.1:8000/admin/")
            print("2. Click on 'Users'")
            print("3. You should see 'mobile_user@example.com'")
        elif response.status_code == 400:
             print(f"\nFailed (400): {response.text}")
             if "unique" in response.text:
                 print("(User likely already exists from previous test)")
        else:
            print(f"\nFailed ({response.status_code}): {response.text}")

    except Exception as e:
        print(f"Connection Error: {e}")

if __name__ == "__main__":
    simulate_app_signup()
