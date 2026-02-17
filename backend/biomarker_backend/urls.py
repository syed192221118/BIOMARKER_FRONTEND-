from django.contrib import admin
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from rest_framework_simplejwt.views import TokenRefreshView, TokenObtainPairView
from drf_yasg.views import get_schema_view
from drf_yasg import openapi
from rest_framework import permissions

from accounts.views import RegisterView, DoctorProfileViewSet, CurrentUserView
from patients.views import PatientProfileViewSet
from screenings.views import ScreeningViewSet
from notifications.views import NotificationViewSet
from reports.views import ReportViewSet
from analysis.views import RunAnalysisView

schema_view = get_schema_view(
   openapi.Info(
      title="BiomarkerAI API",
      default_version='v1',
      description="Medical AI Biomarker Screening System",
   ),
   public=True,
   permission_classes=(permissions.AllowAny,),
)

router = DefaultRouter()
router.register(r'doctors', DoctorProfileViewSet, basename='doctor')
router.register(r'patients', PatientProfileViewSet, basename='patient')
router.register(r'screenings', ScreeningViewSet, basename='screening')
router.register(r'notifications', NotificationViewSet, basename='notification')
router.register(r'reports', ReportViewSet, basename='report')

urlpatterns = [
    path('admin/', admin.site.urls),
    
    # Auth
    path('api/auth/register/', RegisterView.as_view(), name='register'),
    path('api/auth/login/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('api/auth/me/', CurrentUserView.as_view(), name='current_user'),
    path('api/auth/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    
    # Analysis
    path('api/analysis/run/<int:screening_id>/', RunAnalysisView.as_view(), name='run-analysis'),
    
    # Router
    path('api/', include(router.urls)),
    
    # Docs
    path('api/docs/', schema_view.with_ui('swagger', cache_timeout=0), name='schema-swagger-ui'),
]
