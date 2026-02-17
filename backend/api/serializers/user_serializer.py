from rest_framework import serializers
from django.contrib.auth import get_user_model
from rest_framework.validators import UniqueValidator

User = get_user_model()

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'email', 'role', 'is_verified', 'is_active', 'date_joined')
        read_only_fields = ('is_verified', 'is_active', 'date_joined')

class RegisterSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(
        required=True,
        validators=[UniqueValidator(queryset=User.objects.all())]
    )
    password = serializers.CharField(write_only=True, required=True, validators=[])
    role = serializers.ChoiceField(choices=User.ROLE_CHOICES, default='patient')

    full_name = serializers.CharField(required=False)
    specialization = serializers.CharField(required=False)

    class Meta:
        model = User
        fields = ('id', 'email', 'password', 'role', 'full_name', 'specialization')

    def create(self, validated_data):
        full_name = validated_data.pop('full_name', '')
        specialization = validated_data.pop('specialization', '')
        
        user = User.objects.create_user(
            email=validated_data['email'],
            password=validated_data['password'],
            role=validated_data['role']
        )
        
        # Create Profile based on Role
        if user.role == 'doctor':
            from ..models.doctor import Doctor
            Doctor.objects.create(
                user=user,
                full_name=full_name if full_name else "Doctor",
                specialization=specialization if specialization else "General",
                hospital_name="Unassigned",
                license_number=f"DOC{user.id}" # Placeholder
            )
        elif user.role == 'patient':
            from ..models.patient import Patient
            Patient.objects.create(
                user=user,
                full_name=full_name if full_name else "Patient",
                # demographics defaults?
            )
            
        return user
