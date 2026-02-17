package com.example.biomarkerai.data.model

import com.google.gson.annotations.SerializedName

// User & Auth
data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    // Optional profile fields to be handled in 2nd step if needed, or ignored for now
)

data class TokenResponse(
    @SerializedName("access") val accessToken: String,
    @SerializedName("refresh") val refreshToken: String
)

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    @SerializedName("profile_photo") val profilePhoto: String?
)

// Profiles
data class DoctorProfile(
    val id: Int,
    val user: User,
    @SerializedName("hospital_name") val hospitalName: String,
    val specialization: String,
    @SerializedName("license_number") val licenseNumber: String
)

data class PatientProfile(
    val id: Int,
    val user: User,
    val age: Int?,
    val gender: String?,
    val height: Float?,
    val weight: Float?,
    @SerializedName("lifestyle_data") val lifestyleData: Map<String, Any>?,
    @SerializedName("family_history") val familyHistory: List<String>?,
    val conditions: List<String>?
)

// Main Flow
data class Screening(
    val id: Int,
    val patient: Int, // ID
    val doctor: Int?, // ID
    val status: String,
    val vitals: Map<String, Any>?,
    val symptoms: List<String>?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("biomarker_panel") val biomarkerPanel: BiomarkerPanel?
)

data class BiomarkerPanel(
    val id: Int,
    val screening: Int,
    @SerializedName("glucose_fasting") val glucoseFasting: Float?,
    @SerializedName("glucose_pp") val glucosePp: Float?,
    val hba1c: Float?,
    val hdl: Float?,
    val ldl: Float?,
    val triglycerides: Float?,
    val cholesterol: Float?,
    val creatinine: Float?,
    val urea: Float?,
    val alt: Float?,
    val ast: Float?,
    val insulin: Float?,
    val tsh: Float?,
    val crp: Float?,
    val esr: Float?
)

data class AIAnalysisResult(
    val id: Int,
    val screening: Int,
    @SerializedName("metabolic_score") val metabolicScore: Int,
    @SerializedName("diabetes_risk_1yr") val diabetesRisk1yr: Float?,
    @SerializedName("diabetes_risk_5yr") val diabetesRisk5yr: Float?,
    @SerializedName("heart_risk") val heartRisk: Float?,
    @SerializedName("fatty_liver_risk") val fattyLiverRisk: Float?,
    @SerializedName("syndrome_flags") val syndromeFlags: List<String>?,
    @SerializedName("abnormal_markers") val abnormalMarkers: List<String>?
)

