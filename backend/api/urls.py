from django.urls import path, include
from rest_framework.routers import DefaultRouter
from rest_framework_simplejwt.views import TokenRefreshView
from .views.auth_views import RegisterView, CustomTokenObtainPairView, UserMeView
from .views.doctor_views import DoctorViewSet
from .views.patient_views import PatientViewSet
from .views.screening_views import ScreeningViewSet, BiomarkerViewSet
from .views.ai_views import AIResultViewSet, ProcessScreeningView

router = DefaultRouter()
router.register(r'doctors', DoctorViewSet, basename='doctor')
router.register(r'patients', PatientViewSet, basename='patient')
router.register(r'screenings', ScreeningViewSet, basename='screening')
router.register(r'biomarkers', BiomarkerViewSet, basename='biomarker')
router.register(r'ai-results', AIResultViewSet, basename='ai-result')

from .views.report_views import ReportView

from .views.dashboard_views import DoctorDashboardView, PatientDashboardView
from .views.notification_views import NotificationViewSet

router.register(r'notifications', NotificationViewSet, basename='notification')

urlpatterns = [
    path('auth/register/', RegisterView.as_view(), name='register'),
    path('auth/login/', CustomTokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('auth/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('me/', UserMeView.as_view(), name='user-me'),
    path('process/', ProcessScreeningView.as_view(), name='process-screening'),
    path('dashboard/doctor/', DoctorDashboardView.as_view(), name='doctor-dashboard'),
    path('dashboard/patient/', PatientDashboardView.as_view(), name='patient-dashboard'),
    path('reports/<int:screening_id>/', ReportView.as_view(), name='report-download'),
    path('', include(router.urls)),
]
