package com.example.biomarkerai.ui.doctor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun DoctorBottomBar(
    currentScreen: String,
    onDashboardClick: () -> Unit,
    onPatientsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == "Dashboard",
            onClick = onDashboardClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashBlue, selectedTextColor = SplashBlue)
        )
        NavigationBarItem(
            selected = currentScreen == "Patients",
            onClick = onPatientsClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Patients") },
            label = { Text("Patients") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashBlue, selectedTextColor = SplashBlue)
        )
        NavigationBarItem(
            selected = currentScreen == "Analytics",
            onClick = onAnalyticsClick,
            icon = { Icon(Icons.Default.Info, contentDescription = "Analytics") },
            label = { Text("Analytics") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashBlue, selectedTextColor = SplashBlue)
        )
        NavigationBarItem(
            selected = currentScreen == "Settings",
            onClick = onSettingsClick,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashBlue, selectedTextColor = SplashBlue)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBack: () -> Unit,
    onDashboardClick: () -> Unit,
    onPatientsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Practice Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Patient Risk Distribution Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Patient Risk Distribution", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Donut Chart Mockup
                    Box(modifier = Modifier.size(200.dp).align(Alignment.CenterHorizontally)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(Color(0xFF00C853), -90f, 162f, false, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 30.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                            drawArc(Color(0xFFFFB300), 72f, 126f, false, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 30.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                            drawArc(Color(0xFFFF5252), 198f, 72f, false, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 30.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round))
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        RiskLegendItem(Color(0xFF00C853), "Low (45%)")
                        Spacer(modifier = Modifier.width(16.dp))
                        RiskLegendItem(Color(0xFFFFB300), "Moderate (35%)")
                        Spacer(modifier = Modifier.width(16.dp))
                        RiskLegendItem(Color(0xFFFF5252), "High (20%)")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Screenings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Monthly Screenings", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Bar Chart Mockup
                    Row(
                        modifier = Modifier.height(150.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val data = listOf(65, 80, 90, 85, 110, 125)
                        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
                        data.forEachIndexed { index, value ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height((value).dp)
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(SplashBlue)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(months[index], fontSize = 10.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Stats Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AnalyticsStatCard(
                    modifier = Modifier.weight(1f),
                    label = "Avg. Risk Score",
                    value = "4.2",
                    trend = "↓ 0.3 from last month",
                    trendColor = Color(0xFF00C853)
                )
                AnalyticsStatCard(
                    modifier = Modifier.weight(1f),
                    label = "New Diagnoses",
                    value = "12",
                    trend = "↑ 2 from last month",
                    trendColor = Color(0xFFFF5252)
                )
            }
        }
    }
}

@Composable
fun RiskLegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun AnalyticsStatCard(modifier: Modifier, label: String, value: String, trend: String, trendColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(trend, fontSize = 10.sp, color = trendColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onSignOut: () -> Unit,
    onDashboardClick: () -> Unit,
    onPatientsClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(SplashBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("DS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Dr. Smith", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Cardiology Dept.", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Settings Menu
            SettingsMenuItem(Icons.Default.Person, "Account Profile", "Manage your details")
            SettingsMenuItem(Icons.Default.Lock, "Security & Privacy", "2FA, Password")
            SettingsMenuItem(Icons.Default.Notifications, "Notifications", "Email & Push alerts")
            SettingsMenuItem(Icons.Default.Build, "AI Model Config", "Thresholds & Sensitivity")

            Spacer(modifier = Modifier.height(32.dp))

            // Sign Out Button
            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Version 2.4.0 • Build 2024.05.12",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SettingsMenuItem(icon: ImageVector, title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsScreenPreview() {
    BiomarkerAITheme {
        AnalyticsScreen(onBack = {}, onDashboardClick = {}, onPatientsClick = {}, onSettingsClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    BiomarkerAITheme {
        SettingsScreen(onBack = {}, onSignOut = {}, onDashboardClick = {}, onPatientsClick = {}, onAnalyticsClick = {})
    }
}
