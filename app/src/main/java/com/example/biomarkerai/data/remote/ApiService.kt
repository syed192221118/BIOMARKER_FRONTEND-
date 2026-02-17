package com.example.biomarkerai.data.remote

import com.example.biomarkerai.data.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    // Auth
    @POST("auth/register/")
    suspend fun register(@Body request: RegisterRequest): retrofit2.Response<User>

    @POST("auth/login/")
    suspend fun login(@Body request: LoginRequest): retrofit2.Response<TokenResponse>

    @GET("auth/me/")
    suspend fun getCurrentUser(@Header("Authorization") token: String): retrofit2.Response<User>

    @GET("auth/me/")
    suspend fun getUserProfile(@Header("Authorization") token: String): retrofit2.Response<User>

    // Profiles
    @GET("patients/profile/") // Assuming endpoint for current patient
    suspend fun getPatientProfile(@Header("Authorization") token: String): retrofit2.Response<List<PatientProfile>>

    @GET("doctors/profile/") 
    suspend fun getDoctorProfile(@Header("Authorization") token: String): retrofit2.Response<List<DoctorProfile>>

    // Screenings
    @POST("screenings/")
    suspend fun createScreening(
        @Header("Authorization") token: String, 
        @Body screening: Screening
    ): retrofit2.Response<Screening>

    @GET("screenings/")
    suspend fun getScreenings(@Header("Authorization") token: String): retrofit2.Response<List<Screening>>

    @POST("screenings/{id}/add_biomarkers/")
    suspend fun addBiomarkers(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("id") id: Int,
        @Body biomarkers: BiomarkerPanel
    ): retrofit2.Response<BiomarkerPanel>

    // Analysis
    @POST("analysis/run/{id}/")
    suspend fun runAnalysis(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("id") id: Int
    ): retrofit2.Response<AIAnalysisResult>
}
