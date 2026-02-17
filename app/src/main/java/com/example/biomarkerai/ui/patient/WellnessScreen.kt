package com.example.biomarkerai.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WellnessScreen(onBack: () -> Unit, onHomeClick: () -> Unit, onHealthClick: () -> Unit, onProfileClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Wellness", fontWeight = FontWeight.Bold) },
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
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Shield, "Wellness") },
                    label = { Text("Wellness") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashGreen, selectedTextColor = SplashGreen, indicatorColor = Color(0xFFE8F5E9))
                )
                NavigationBarItem(selected = false, onClick = onProfileClick, icon = { Icon(Icons.Default.PersonOutline, "Profile") }, label = { Text("Profile") })
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
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Based on your doctor-approved\nsummary",
                        color = Color(0xFFC5E1A5),
                        fontSize = 14.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Health Overview
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, null, tint = SplashGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Health Overview", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    WellnessMetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.WaterDrop,
                        iconColor = Color(0xFF42A5F5),
                        title = "Blood Sugar",
                        status = "Normal",
                        description = "Your levels are within healthy range"
                    )
                    WellnessMetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Favorite,
                        iconColor = Color(0xFFEF5350),
                        title = "Heart Health",
                        status = "Good",
                        description = "Cardiovascular indicators look healthy"
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    WellnessMetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Timeline,
                        iconColor = Color(0xFF66BB6A),
                        title = "Weight Management",
                        status = "Good",
                        description = "You're making good progress"
                    )
                    WellnessMetricCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Shield,
                        iconColor = Color(0xFFAB47BC),
                        title = "Recovery & Wellness",
                        status = "Normal",
                        description = "Inflammation markers are healthy"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Doctor's Notes
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notes, null, tint = SplashGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Doctor's Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    DoctorNoteItem("Overall health trajectory is positive")
                    DoctorNoteItem("Continue with current lifestyle modifications")
                    DoctorNoteItem("Great improvement in daily activity levels")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Last reviewed by Dr. Smith • Oct 24, 2024", color = Color.LightGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Next Steps
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, tint = SplashGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Next Steps", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    NextStepItem(Icons.Default.DirectionsRun, "Continue daily walks after meals")
                    NextStepItem(Icons.Default.Bedtime, "Maintain current sleep schedule")
                    NextStepItem(Icons.Default.CalendarMonth, "Follow-up screening in 3 months")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFEFF6FF)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Need detailed lab results?", fontWeight = FontWeight.Bold, color = Color(0xFF1E40AF), fontSize = 14.sp)
                        Text(
                            "For specific biomarker values and detailed analysis, please consult your healthcare provider during your next appointment.",
                            fontSize = 12.sp,
                            color = Color(0xFF3B82F6),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun WellnessMetricCard(modifier: Modifier, icon: ImageVector, iconColor: Color, title: String, status: String, description: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).background(iconColor.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = iconColor, modifier = Modifier.size(16.dp))
                }
                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF81C784), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(12.dp)) {
                Text(status, color = Color(0xFF4CAF50), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, fontSize = 11.sp, color = Color.Gray, lineHeight = 16.sp)
        }
    }
}

@Composable
fun DoctorNoteItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(6.dp).background(Color(0xFF42A5F5), CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 13.sp, color = Color.Gray)
    }
}

@Composable
fun NextStepItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(36.dp).background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
fun WellnessScreenPreview() {
    BiomarkerAITheme {
        WellnessScreen(onBack = {}, onHomeClick = {}, onHealthClick = {}, onProfileClick = {})
    }
}
