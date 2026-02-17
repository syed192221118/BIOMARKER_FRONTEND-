package com.example.biomarkerai.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthScoreScreen(onBack: () -> Unit, onHomeClick: () -> Unit, onWellnessClick: () -> Unit, onProfileClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Score", fontWeight = FontWeight.Bold) },
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
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Favorite, "Health") },
                    label = { Text("Health") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashGreen, selectedTextColor = SplashGreen, indicatorColor = Color(0xFFE8F5E9))
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onWellnessClick,
                    icon = { Icon(Icons.Default.Shield, "Wellness") },
                    label = { Text("Wellness") }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Circular Score
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .border(2.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "84",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Excellent",
                        fontSize = 20.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Metabolic Age Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Metabolic Age: 28", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your metabolic age is 4 years younger than your actual age! Keep it up.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metabolic Health & Recovery Status
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HealthStatusCard(
                    modifier = Modifier.weight(1f),
                    title = "Metabolic Health",
                    status = "Good",
                    statusColor = SplashGreen
                )
                HealthStatusCard(
                    modifier = Modifier.weight(1f),
                    title = "Recovery Status",
                    status = "Excellent",
                    statusColor = Color(0xFF3B82F6)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Areas for Improvement
            Text(
                "Areas for Improvement",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1F2937)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sleep Quality", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Fair", color = Color(0xFFF59E0B), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { 0.5f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color(0xFFF59E0B),
                        trackColor = Color(0xFFF3F4F6)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Try to get 30 mins more sleep to improve recovery.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                    Text(
                        "This summary is approved by your healthcare provider. For detailed results and specific biomarker values, please consult your doctor.",
                        fontSize = 12.sp,
                        color = Color(0xFF1E40AF),
                        lineHeight = 18.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun HealthStatusCard(modifier: Modifier, title: String, status: String, statusColor: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(status, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = statusColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HealthScoreScreenPreview() {
    BiomarkerAITheme {
        HealthScoreScreen(onBack = {}, onHomeClick = {}, onWellnessClick = {}, onProfileClick = {})
    }
}
