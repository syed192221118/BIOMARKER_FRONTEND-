package com.example.biomarkerai.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.data.local.SessionManager
import com.example.biomarkerai.data.model.LoginRequest
import com.example.biomarkerai.data.remote.RetrofitClient
import com.example.biomarkerai.ui.theme.SplashBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorPortalScreen(
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPassword: () -> Unit,
    onSignUp: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFF0F7FF), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info, // Placeholder for stethoscope icon
                    contentDescription = null,
                    tint = SplashBlue,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Doctor Portal",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )

            Text(
                text = "Secure access for medical professionals",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "Email Address", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("doctor@example.com", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = SplashBlue,
                unfocusedIndicatorColor = Color(0xFFF0F0F0),
                focusedContainerColor = Color(0xFFF9F9F9),
                unfocusedContainerColor = Color(0xFFF9F9F9)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Password", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = SplashBlue,
                unfocusedIndicatorColor = Color(0xFFF0F0F0),
                focusedContainerColor = Color(0xFFF9F9F9),
                unfocusedContainerColor = Color(0xFFF9F9F9)
            )
        )

        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(Alignment.End),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Forgot password?",
                color = SplashBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                scope.launch {
                    try {
                        val response = RetrofitClient.instance.login(LoginRequest(email, password))
                        if (response.isSuccessful && response.body() != null) {
                            val token = response.body()!!.accessToken
                            sessionManager.saveAuthToken(token)
                            
                            // Fetch profile to verify role
                            val profileResponse = RetrofitClient.instance.getUserProfile("Bearer $token")
                            if (profileResponse.isSuccessful && profileResponse.body() != null) {
                                val user = profileResponse.body()!!
                                if (user.role == "doctor") {
                                    sessionManager.saveUserRole(user.role)
                                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess()
                                } else {
                                    Toast.makeText(context, "Access Denied: This portal is for doctors only (Role: ${user.role})", Toast.LENGTH_LONG).show()
                                    sessionManager.clearSession()
                                }
                            } else {
                                val errorMsg = profileResponse.errorBody()?.string() ?: "Code: ${profileResponse.code()}"
                                Toast.makeText(context, "Failed to fetch user profile: $errorMsg", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            val errorMsg = response.errorBody()?.string() ?: "Login failed (Code: ${response.code()})"
                            Toast.makeText(context, "Login Failed: $errorMsg", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Network Error: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
            shape = RoundedCornerShape(14.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Secure Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have a doctor account? ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            TextButton(
                onClick = onSignUp,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Register",
                    color = SplashBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "By logging in, you agree to the Medical Data Privacy Policy.",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorPortalPreview() {
    DoctorPortalScreen(onBack = {}, onLoginSuccess = {}, onForgotPassword = {}, onSignUp = {})
}
