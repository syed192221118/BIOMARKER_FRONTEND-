from rest_framework import serializers
from ..models.ai import AIResult, RiskFactor, Recommendation

class RiskFactorSerializer(serializers.ModelSerializer):
    class Meta:
        model = RiskFactor
        fields = '__all__'

class RecommendationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Recommendation
        fields = '__all__'

class AIResultSerializer(serializers.ModelSerializer):
    risk_factors = RiskFactorSerializer(many=True, read_only=True)
    recommendations = RecommendationSerializer(many=True, read_only=True)
    
    class Meta:
        model = AIResult
        fields = '__all__'
