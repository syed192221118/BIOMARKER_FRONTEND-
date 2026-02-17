package com.example.biomarkerai.ui.patient

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReasonForVisitScreen(
    onBack: () -> Unit,
    onReviewBooking: () -> Unit,
    onHomeClick: () -> Unit,
    onHealthClick: () -> Unit,
    onWellnessClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var additionalNotes by remember { mutableStateOf("") }
    val selectedReasons = remember { mutableStateListOf<String>() }
    var uploadedFileUri by remember { mutableStateOf<Uri?>(null) }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uploadedFileUri = uri
    }

    val commonReasons = listOf(
        "General Checkup", "Fever", "Headache", "Stomach Pain",
        "Fatigue", "Skin Rash", "Joint Pain", "Cough/Cold",
        "Blood Pressure", "Diabetes Review"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reason for Visit", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SplashGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onReviewBooking,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Review Booking", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    NavigationBarItem(
                        selected = false,
                        onClick = onHomeClick,
                        icon = { Icon(Icons.Default.Home, "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onHealthClick,
                        icon = { Icon(Icons.Default.FavoriteBorder, "Health") },
                        label = { Text("Health") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onWellnessClick,
                        icon = { Icon(Icons.Default.Shield, "Wellness") },
                        label = { Text("Wellness") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = onProfileClick,
                        icon = { Icon(Icons.Default.PersonOutline, "Profile") },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Progress Bar Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Step 4 of 5", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Text(text = "80 %", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.8f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SplashBlue,
                trackColor = Color(0xFFE9ECEF)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "What's the reason for your visit?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Select all that apply or describe your symptoms.",
                fontSize = 15.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Common Reasons", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                commonReasons.forEach { reason ->
                    val isSelected = selectedReasons.contains(reason)
                    Surface(
                        modifier = Modifier.clickable {
                            if (isSelected) selectedReasons.remove(reason) else selectedReasons.add(reason)
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SplashBlue.copy(alpha = 0.1f) else Color(0xFFF1F5F9),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, SplashBlue) else null
                    ) {
                        Text(
                            text = reason,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            fontSize = 14.sp,
                            color = if (isSelected) SplashBlue else Color.Gray,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Additional Notes", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = additionalNotes,
                onValueChange = { additionalNotes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Describe your symptoms, duration, or any specific concerns...", color = Color.LightGray, fontSize = 14.sp) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF8FAFC).copy(alpha = 0.5f),
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(0xFFF1F5F9),
                    focusedBorderColor = SplashBlue
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("Attachments (Optional)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 1.dp,
                        color = if (uploadedFileUri != null) SplashBlue else Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(if (uploadedFileUri != null) SplashBlue.copy(alpha = 0.05f) else Color(0xFFF8FAFC).copy(alpha = 0.5f))
                    .clickable { launcher.launch("*/*") },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (uploadedFileUri != null) Icons.Default.CheckCircle else Icons.Default.AttachFile,
                        contentDescription = null,
                        tint = if (uploadedFileUri != null) SplashBlue else Color.Gray
                    )
                    Text(
                        text = if (uploadedFileUri != null) "File Selected" else "Upload Reports",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (uploadedFileUri != null) SplashBlue else Color.Black
                    )
                    Text(
                        text = uploadedFileUri?.path?.split("/")?.lastOrNull() ?: "PDF, JPG, or PNG (Max 5MB)",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReasonForVisitScreenPreview() {
    BiomarkerAITheme {
        ReasonForVisitScreen({}, {}, {}, {}, {}, {})
    }
}
