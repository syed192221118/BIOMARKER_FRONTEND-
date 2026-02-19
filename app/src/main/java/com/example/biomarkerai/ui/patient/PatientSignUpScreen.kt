package com.example.biomarkerai.ui.patient

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.data.model.RegisterRequest
import com.example.biomarkerai.data.remote.RetrofitClient
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashGreen
import kotlinx.coroutines.launch

@Composable
fun PatientSignUpScreen(
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    onSignIn: () -> Unit
) {
    var fullName by remember { mutableStateOf("") } // Used as username in RegisterRequest
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Profile Icon
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = Color(0xFFE8F5E9)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp),
                    tint = SplashGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Title Text
        Text(
            text = "Create Account",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Start your health journey today",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Up Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                // Full Name Field
                SignUpTextField(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "John Doe"
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email Address Field
                SignUpTextField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "john@example.com",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password Field
                SignUpTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "••••••••",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Password Field
                SignUpTextField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "••••••••",
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Sign Up Button
                Button(
                    onClick = {
                        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (password != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        scope.launch {
                            try {
                                val response = RetrofitClient.instance.register(
                                    RegisterRequest(
                                        username = email,
                                        email = email,
                                        password = password,
                                        role = "patient",
                                        fullName = fullName
                                    )
                                )
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Account Created! Please Login.", Toast.LENGTH_SHORT).show()
                                    onSignIn() // Navigate to login
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Toast.makeText(context, "Registration Failed: $errorBody", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SplashGreen),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Sign Up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sign In Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    TextButton(
                        onClick = onSignIn,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Sign In",
                            color = SplashGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun SignUpTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF8F9FA),
                unfocusedContainerColor = Color(0xFFF8F9FA),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PatientSignUpScreenPreview() {
    BiomarkerAITheme {
        PatientSignUpScreen({}, {}, {})
    }
}
