package com.example.biomarkerai.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    onBack: () -> Unit,
    onHomeClick: () -> Unit,
    onHealthClick: () -> Unit,
    onWellnessClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTypeSelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Appointment", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SplashGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
                .padding(16.dp)
        ) {
            // Progress Bar Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Step 1 of 5",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "20 %",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.2f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SplashBlue,
                trackColor = Color(0xFFE9ECEF)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Choose Appointment Type",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Select how you would like to consult with our specialists.",
                fontSize = 15.sp,
                color = Color.Gray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Appointment Type Cards
            //
            AppointmentTypeCard(
                title = "In-Person Visit",
                description = "Visit a doctor at the clinic for a physical checkup.",
                icon = Icons.Default.MedicalServices,
                iconColor = SplashBlue,
                onClick = { onTypeSelected("In-Person") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppointmentTypeCard(
                title = "Video Consultation",
                description = "Connect with a doctor remotely via video call.",
                icon = Icons.Default.Videocam,
                iconColor = Color(0xFF9C27B0),
                onClick = { onTypeSelected("Video") }
            )
        }
    }
}

@Composable
fun AppointmentTypeCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1A1C1E)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookAppointmentScreenPreview() {
    BiomarkerAITheme {
        BookAppointmentScreen(
            onBack = {},
            onHomeClick = {},
            onHealthClick = {},
            onWellnessClick = {},
            onProfileClick = {},
            onTypeSelected = {}
        )
    }
}
