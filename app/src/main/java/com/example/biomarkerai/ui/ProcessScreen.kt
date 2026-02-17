package com.example.biomarkerai.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.data.local.SessionManager
import com.example.biomarkerai.data.model.AnalysisRequest
import com.example.biomarkerai.data.model.AnalysisResponse
import com.example.biomarkerai.data.model.BiomarkerReadingInput
import com.example.biomarkerai.data.remote.RetrofitClient
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProcessScreen(onComplete: (AnalysisResponse) -> Unit) {
    var currentStep by remember { mutableIntStateOf(1) }
    val steps = listOf("Scanning", "Analyzing", "Predicting", "Results")
    var analysisResult by remember { mutableStateOf<AnalysisResponse?>(null) }
    var isError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }

    LaunchedEffect(Unit) {
        // Simulate scanning steps while fetching data
        // Step 1: Scanning
        delay(1500)
        currentStep = 2
        
        // Step 2: Analyzing (Call API)
        val token = sessionManager.fetchAuthToken()
        if (token != null) {
            try {
                // Mock Input Data (e.g., from a connected device or previous input)
                val mockReadings = listOf(
                    BiomarkerReadingInput(biomarkerId = 1, value = (80..160).random().toFloat()), // Glucose
                    BiomarkerReadingInput(biomarkerId = 2, value = (100..150).random().toFloat()) // LDL
                )
                
                val response = RetrofitClient.instance.analyze(
                    token = "Bearer $token",
                    request = AnalysisRequest(readings = mockReadings)
                )

                if (response.isSuccessful && response.body() != null) {
                    analysisResult = response.body()
                    delay(1000)
                    currentStep = 3 // Predicting
                    delay(1000)
                    currentStep = 4 // Results ready
                    delay(500)
                    onComplete(analysisResult!!)
                } else {
                    isError = true
                    Toast.makeText(context, "Analysis Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                isError = true
                Toast.makeText(context, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            isError = true
            Toast.makeText(context, "Authentication Error. Please Login again.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Analysis Process",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        
        Text(
            text = if (isError) "Analysis Failed" else "AI is evaluating your biomarkers",
            fontSize = 16.sp,
            color = if (isError) Color.Red else Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Step Progress Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, step ->
                val stepNum = index + 1
                val isActive = stepNum <= currentStep
                val isCompleted = stepNum < currentStep

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) SplashGreen 
                                else if (isActive) SplashBlue 
                                else Color.LightGray.copy(alpha = 0.5f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = stepNum.toString(),
                                color = if (isActive) Color.White else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = step,
                        fontSize = 12.sp,
                        fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                        color = if (isActive) SplashBlue else Color.Gray
                    )
                }

                if (index < steps.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 24.dp)
                            .padding(horizontal = 8.dp),
                        color = if (stepNum < currentStep) SplashGreen else Color.LightGray.copy(alpha = 0.5f),
                        thickness = 2.dp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Main Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isError) {
                    Icon(
                        imageVector = Icons.Default.Check, // Should be Error icon but using Check for now or null
                        contentDescription = "Error",
                        tint = Color.Red,
                        modifier = Modifier.size(64.dp)
                    )
                    Text("Error occurred during analysis", color = Color.Red)
                } else {
                    CircularProgressIndicator(
                        progress = { currentStep.toFloat() / steps.size.toFloat() },
                        modifier = Modifier.size(120.dp),
                        color = SplashBlue,
                        strokeWidth = 8.dp,
                        trackColor = Color.LightGray.copy(alpha = 0.3f)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "Step $currentStep: ${steps.getOrElse(currentStep-1) { "" }}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SplashBlue
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Please wait while our AI model processes your data. This usually takes less than a minute.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProcessScreenPreview() {
    // ProcessScreen(onComplete = {}) // Requires AnalysisResponse mock
}
