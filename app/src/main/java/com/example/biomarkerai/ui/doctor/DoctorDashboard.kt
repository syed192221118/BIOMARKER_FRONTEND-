package com.example.biomarkerai.ui.doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashGreen

@Composable
fun DoctorDashboard(
    onNewScreening: () -> Unit, 
    onPatientsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onChatClick,
                containerColor = SplashGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Chat, contentDescription = "AI Assistant")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
        ) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 48.dp, 24.dp, 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                Text(text = "Doctor Dashboard", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                // Welcome Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SplashBlue)
                ) {
                    Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Welcome, Dr. Smith", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "Cardiology Department • City Hospital", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatCard(modifier = Modifier.weight(1f), value = "1,248", label = "Total Patients", icon = Icons.Default.Person, color = SplashBlue)
                    StatCard(modifier = Modifier.weight(1f), value = "86", label = "High Risk", icon = Icons.Default.Warning, color = Color.Red)
                    StatCard(modifier = Modifier.weight(1f), value = "24", label = "This Week", icon = Icons.Default.DateRange, color = SplashGreen)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(text = "Today's Schedule", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ScheduleItem("09:00 AM", "John Smith", "Follow-up")
                ScheduleItem("10:30 AM", "Mary Wilson", "New Screening", isHighlight = true)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onNewScreening,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "New Assessment", fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, value: String, label: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = label, color = Color.Gray, fontSize = 10.sp)
        }
    }
}

@Composable
fun ScheduleItem(time: String, name: String, type: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(if(isHighlight) 4.dp else 0.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = time, fontWeight = FontWeight.Bold, color = SplashBlue, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = name, fontWeight = FontWeight.Bold)
            Text(text = type, color = Color.Gray, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorDashboardPreview() {
    BiomarkerAITheme {
        DoctorDashboard(onNewScreening = {}, onPatientsClick = {}, onAnalyticsClick = {}, onSettingsClick = {}, onChatClick = {})
    }
}
