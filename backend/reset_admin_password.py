
import os
import django

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'biomarker_backend.settings')
django.setup()

from django.contrib.auth import get_user_model

User = get_user_model()

try:
    user = User.objects.get(email='admin@example.com')
    user.set_password('admin123')
    user.save()
    print("Password for 'admin@example.com' reset to 'admin123'.")
except User.DoesNotExist:
    print("User 'admin@example.com' not found.")
