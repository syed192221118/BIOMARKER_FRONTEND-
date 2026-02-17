package com.example.biomarkerai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.biomarkerai.ui.*
import com.example.biomarkerai.ui.doctor.*
import com.example.biomarkerai.ui.patient.*
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import kotlinx.coroutines.delay
import com.example.biomarkerai.data.model.AnalysisResponse

enum class Screen {
    Splash, Onboarding, RoleSelection, DoctorPortal, DoctorDashboard, AssessmentFlow, PatientDashboard, Process, Results,
    PatientList, PatientProfile, PatientHistory, DetailedReport, Analytics, Settings, ScreeningGuide, HealthScore, Wellness, Profile,
    BookAppointment, SelectDoctor, DateTimeSelection, ReasonForVisit, ConfirmBooking, BookingConfirmed, PatientLogin, PatientSignUp, 
    PatientForgotPassword, DoctorForgotPassword, PatientScreeningGuide, DoctorSignUp, AiChat
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiomarkerAITheme {
                var currentScreen by remember { mutableStateOf(Screen.Splash) }
                var previousScreen by remember { mutableStateOf(Screen.Splash) }
                var selectedPatient by remember { mutableStateOf("") }
                
                var assessmentStep by remember { mutableIntStateOf(0) }
                var labSubStep by remember { mutableIntStateOf(0) }
                var assessmentData by remember { mutableStateOf(AssessmentData()) }
                var analysisResponse by remember { mutableStateOf<AnalysisResponse?>(null) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    currentScreen = Screen.Onboarding
                }

                val showBottomBar = currentScreen in listOf(
                    Screen.DoctorDashboard, Screen.PatientList, Screen.PatientProfile, 
                    Screen.PatientHistory, Screen.Analytics, Screen.Settings, 
                    Screen.ScreeningGuide, Screen.AssessmentFlow
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            val currentBarItem = when (currentScreen) {
                                Screen.DoctorDashboard -> "Dashboard"
                                Screen.PatientList, Screen.PatientProfile, Screen.PatientHistory -> "Patients"
                                Screen.Analytics -> "Analytics"
                                Screen.Settings -> "Settings"
                                else -> "" 
                            }
                            DoctorBottomBar(
                                currentScreen = currentBarItem,
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics },
                                onSettingsClick = { currentScreen = Screen.Settings }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            Screen.Splash -> SplashScreen()
                            Screen.Onboarding -> OnboardingFlow(onFinish = {
                                currentScreen = Screen.RoleSelection
                            })
                            Screen.RoleSelection -> RoleSelectionScreen(onRoleSelected = { role ->
                                currentScreen = if (role == "doctor") Screen.DoctorPortal else Screen.PatientLogin
                            })
                            Screen.PatientLogin -> PatientLoginScreen(
                                onBack = { currentScreen = Screen.RoleSelection },
                                onSignIn = { currentScreen = Screen.PatientDashboard },
                                onSignUp = { currentScreen = Screen.PatientSignUp },
                                onForgotPassword = { currentScreen = Screen.PatientForgotPassword }
                            )
                            Screen.PatientSignUp -> PatientSignUpScreen(
                                onBack = { currentScreen = Screen.PatientLogin },
                                onSignUp = { currentScreen = Screen.PatientDashboard },
                                onSignIn = { currentScreen = Screen.PatientLogin }
                            )
                            Screen.PatientForgotPassword -> PatientForgotPasswordScreen(
                                onBack = { currentScreen = Screen.PatientLogin },
                                onSendResetCode = { },
                                onSignIn = { currentScreen = Screen.PatientLogin }
                            )
                            Screen.DoctorPortal -> DoctorPortalScreen(
                                onBack = { currentScreen = Screen.RoleSelection },
                                onLoginSuccess = { currentScreen = Screen.DoctorDashboard },
                                onForgotPassword = { currentScreen = Screen.DoctorForgotPassword },
                                onSignUp = { currentScreen = Screen.DoctorSignUp }
                            )
                            Screen.DoctorSignUp -> DoctorSignUpScreen(
                                onBack = { currentScreen = Screen.DoctorPortal },
                                onSignUpSuccess = { currentScreen = Screen.DoctorDashboard },
                                onSignIn = { currentScreen = Screen.DoctorPortal }
                            )
                            Screen.DoctorForgotPassword -> DoctorForgotPasswordScreen(
                                onBack = { currentScreen = Screen.DoctorPortal },
                                onSendResetLink = { },
                                onSignIn = { currentScreen = Screen.DoctorPortal }
                            )
                            Screen.DoctorDashboard -> DoctorDashboard(
                                onNewScreening = { 
                                    assessmentStep = 0
                                    labSubStep = 0
                                    assessmentData = AssessmentData()
                                    currentScreen = Screen.AssessmentFlow 
                                },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics },
                                onSettingsClick = { currentScreen = Screen.Settings },
                                onChatClick = { 
                                    previousScreen = Screen.DoctorDashboard
                                    currentScreen = Screen.AiChat 
                                }
                            )
                            Screen.AssessmentFlow -> AssessmentFlow(
                                currentStep = assessmentStep,
                                onStepChange = { assessmentStep = it },
                                labSubStep = labSubStep,
                                onLabSubStepChange = { labSubStep = it },
                                data = assessmentData,
                                onDataChange = { assessmentData = it },
                                onComplete = { currentScreen = Screen.DoctorDashboard },
                                onBack = { currentScreen = Screen.DoctorDashboard },
                                onScreeningGuideClick = { currentScreen = Screen.ScreeningGuide }
                            )
                            Screen.PatientDashboard -> PatientDashboard(
                                onStartAssessment = { currentScreen = Screen.Process },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile },
                                onBookAppointmentClick = { currentScreen = Screen.BookAppointment },
                                onScreeningGuideClick = { currentScreen = Screen.PatientScreeningGuide },
                                onChatClick = { 
                                    previousScreen = Screen.PatientDashboard
                                    currentScreen = Screen.AiChat 
                                }
                            )
                            Screen.AiChat -> AiChatScreen(
                                onBack = { currentScreen = previousScreen }
                            )
                            Screen.HealthScore -> HealthScoreScreen(
                                onBack = { currentScreen = Screen.PatientDashboard },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                            Screen.Wellness -> WellnessScreen(
                                onBack = { currentScreen = Screen.PatientDashboard },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                            Screen.Profile -> ProfileScreen(
                                onBack = { currentScreen = Screen.PatientDashboard },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onLogout = { currentScreen = Screen.RoleSelection }
                            )
                            Screen.BookAppointment -> BookAppointmentScreen(
                                onBack = { currentScreen = Screen.PatientDashboard },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile },
                                onTypeSelected = { currentScreen = Screen.SelectDoctor }
                            )
                            Screen.SelectDoctor -> SelectDoctorScreen(
                                onBack = { currentScreen = Screen.BookAppointment },
                                onContinue = { currentScreen = Screen.DateTimeSelection },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                            Screen.DateTimeSelection -> DateTimeSelectionScreen(
                                onBack = { currentScreen = Screen.SelectDoctor },
                                onContinue = { currentScreen = Screen.ReasonForVisit },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                            Screen.ReasonForVisit -> ReasonForVisitScreen(
                                onBack = { currentScreen = Screen.DateTimeSelection },
                                onReviewBooking = { currentScreen = Screen.ConfirmBooking },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                            Screen.ConfirmBooking -> ConfirmBookingScreen(
                                onBack = { currentScreen = Screen.ReasonForVisit },
                                onConfirmBooking = { currentScreen = Screen.BookingConfirmed },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                            Screen.BookingConfirmed -> BookingConfirmedScreen(
                                onBackToHome = { currentScreen = Screen.PatientDashboard },
                                onAddToCalendar = { }
                            )
                            Screen.Process -> ProcessScreen(onComplete = { result ->
                                analysisResponse = result
                                currentScreen = Screen.Results
                            })
                            Screen.Results -> ResultsScreen(
                                result = analysisResponse,
                                onBackToStart = {
                                    currentScreen = Screen.PatientDashboard
                                }
                            )
                            
                            Screen.PatientList -> PatientListScreen(
                                onPatientClick = { name ->
                                    selectedPatient = name
                                    currentScreen = Screen.PatientProfile
                                },
                                onBack = { currentScreen = Screen.DoctorDashboard },
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics },
                                onSettingsClick = { currentScreen = Screen.Settings }
                            )
                            Screen.PatientProfile -> PatientProfileScreen(
                                patientId = selectedPatient,
                                onHistoryClick = { currentScreen = Screen.PatientHistory },
                                onReportClick = { currentScreen = Screen.DetailedReport },
                                onNewScreeningClick = { currentScreen = Screen.AssessmentFlow },
                                onBack = { currentScreen = Screen.PatientList },
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics },
                                onSettingsClick = { currentScreen = Screen.Settings }
                            )
                            Screen.PatientHistory -> PatientHistoryScreen(
                                onReportClick = { _ -> currentScreen = Screen.DetailedReport },
                                onBack = { currentScreen = Screen.PatientProfile },
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics },
                                onSettingsClick = { currentScreen = Screen.Settings }
                            )
                            Screen.DetailedReport -> ResultsScreen(
                                result = analysisResponse,
                                onBackToStart = {
                                    currentScreen = Screen.PatientProfile
                                }
                            )

                            Screen.Analytics -> AnalyticsScreen(
                                onBack = { currentScreen = Screen.DoctorDashboard },
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onSettingsClick = { currentScreen = Screen.Settings }
                            )
                            Screen.Settings -> SettingsScreen(
                                onBack = { currentScreen = Screen.DoctorDashboard },
                                onSignOut = { currentScreen = Screen.RoleSelection },
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics }
                            )
                            Screen.ScreeningGuide -> ScreeningGuideScreen(
                                onBack = { currentScreen = Screen.AssessmentFlow },
                                onDashboardClick = { currentScreen = Screen.DoctorDashboard },
                                onPatientsClick = { currentScreen = Screen.PatientList },
                                onAnalyticsClick = { currentScreen = Screen.Analytics },
                                onSettingsClick = { currentScreen = Screen.Settings }
                            )
                            Screen.PatientScreeningGuide -> PatientScreeningGuideScreen(
                                onBack = { currentScreen = Screen.PatientDashboard },
                                onHomeClick = { currentScreen = Screen.PatientDashboard },
                                onHealthClick = { currentScreen = Screen.HealthScore },
                                onWellnessClick = { currentScreen = Screen.Wellness },
                                onProfileClick = { currentScreen = Screen.Profile }
                            )
                        }
                    }
                }
            }
        }
    }
}
