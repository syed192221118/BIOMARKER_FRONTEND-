package com.example.biomarkerai.ui.patient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun PatientDashboard(
    onStartAssessment: () -> Unit,
    onHealthClick: () -> Unit,
    onWellnessClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBookAppointmentClick: () -> Unit,
    onScreeningGuideClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Health", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, "Menu") } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.NotificationsNone, "Notifications") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onChatClick,
                containerColor = SplashGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Chat, contentDescription = "AI Assistant")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = SplashGreen, selectedTextColor = SplashGreen)
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
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Hello, Sarah! 👋", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Here's your health summary", color = Color.Gray, fontSize = 14.sp)
                }
                Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(16.dp)) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EmojiEvents, null, tint = SplashGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("7 Day Streak!", color = SplashGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Score Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Your Health Score", color = Color.LightGray, fontSize = 14.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("85", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = SplashGreen)
                        Text("/100", color = Color.LightGray, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
                    }
                    Text("Reviewed by Dr. Smith", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(progress = { 0.85f }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = SplashGreen, trackColor = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Excellent! You're making great progress", color = SplashGreen, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Screening Guide Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onScreeningGuideClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF0F7FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Search, null, tint = SplashBlue, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Screening Guide", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Learn about biomarkers & diseases", fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // AI Assistant Card (Quick access)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChatClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SplashGreen.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(SplashGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Psychology, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("AI Health Assistant", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SplashGreen)
                        Text("Ask anything about your health", fontSize = 12.sp, color = Color.Gray)
                    }
                    Icon(Icons.Default.Chat, null, tint = SplashGreen)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Doctor's Summary Header
            SectionHeader(Icons.Default.HealthAndSafety, "Doctor's Summary")
            
            Spacer(modifier = Modifier.height(12.dp))

            SummaryCard(
                icon = Icons.Default.Adjust,
                iconColor = Color(0xFFFF9800),
                title = "Risk Level",
                badge = "Moderate",
                badgeColor = Color(0xFFFFF3E0),
                badgeTextColor = Color(0xFFFF9800),
                subtitle = "Based on your latest screening reviewed by Dr. Smith"
            )

            Spacer(modifier = Modifier.height(12.dp))

            SummaryCard(
                icon = Icons.Default.CalendarToday,
                iconColor = SplashBlue,
                title = "Follow-up Recommended",
                subtitle = "Recheck lipid panel in 3 months\nNext screening: March 2025"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Recommendations
            SectionHeader(Icons.Default.CheckCircleOutline, "Your Recommendations")
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    RecommendationItem(Icons.Default.Restaurant, Color(0xFFFF9800), "Reduce refined carbohydrates and sugars")
                    RecommendationItem(Icons.Default.ElectricBolt, SplashBlue, "Walk 30 minutes daily after meals")
                    RecommendationItem(Icons.Default.Bedtime, Color(0xFF9C27B0), "Aim for 7-8 hours of quality sleep")
                    RecommendationItem(Icons.Default.WaterDrop, Color(0xFF03A9F4), "Stay hydrated - 8 glasses of water daily")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's Goals
            SectionHeader(Icons.Default.TrendingUp, "Today's Goals")
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GoalCard(modifier = Modifier.weight(1f), icon = Icons.Default.DirectionsWalk, value = "6420", label = "Steps", progress = 0.6f, color = SplashBlue)
                GoalCard(modifier = Modifier.weight(1f), icon = Icons.Default.WaterDrop, value = "5", label = "Glasses", progress = 0.5f, color = Color(0xFF03A9F4))
                GoalCard(modifier = Modifier.weight(1f), icon = Icons.Default.Bedtime, value = "7.5", label = "Hrs", progress = 0.9f, color = Color(0xFF9C27B0))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Book an Appointment Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFF8A00), Color(0xFFFFAB40))
                        )
                    )
                    .clickable { onBookAppointmentClick() }
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.CalendarMonth, null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Book an Appointment", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                        Text("Schedule a visit with your doctor", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Upcoming Appointments
            SectionHeader(Icons.Default.CalendarMonth, "Upcoming Appointments")
            Spacer(modifier = Modifier.height(12.dp))
            AppointmentCard("Dr. Sarah Smith", "Cardiologist • In-Person", "Tue, 10 Feb, 10:30 am", Icons.Default.MedicalServices, SplashBlue)
            Spacer(modifier = Modifier.height(12.dp))
            AppointmentCard("Dr. James Wilson", "Endocrinologist • Video Call", "Sat, 14 Feb, 2:30 pm", Icons.Default.Videocam, Color(0xFF9C27B0))

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Tip
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(Color(0xFFFFF9C4), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFFFD600))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Daily Health Tip", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Walking just 15 minutes after meals can significantly help manage your blood sugar levels.", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = SplashGreen, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1B5E20))
    }
}

@Composable
fun SummaryCard(icon: ImageVector, iconColor: Color, title: String, subtitle: String, badge: String? = null, badgeColor: Color = Color.Transparent, badgeTextColor: Color = Color.Transparent) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(40.dp).background(iconColor.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    if (badge != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = badgeColor, shape = RoundedCornerShape(8.dp)) {
                            Text(badge, color = badgeTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
                Text(subtitle, fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
fun RecommendationItem(icon: ImageVector, color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(32.dp).background(color.copy(alpha = 0.1f), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun GoalCard(modifier: Modifier, icon: ImageVector, value: String, label: String, progress: Float, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(36.dp).background(color.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(label, fontSize = 10.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape), color = color, trackColor = Color(0xFFF1F5F9))
        }
    }
}

@Composable
fun AppointmentCard(name: String, role: String, time: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // Left Accent Bar
            Box(modifier = Modifier.fillMaxHeight().width(6.dp).background(SplashGreen, RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)))
            
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(48.dp).background(iconColor.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = iconColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(role, fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, null, tint = SplashGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(time, color = SplashGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientDashboardPreview() {
    BiomarkerAITheme {
        PatientDashboard(onStartAssessment = {}, onHealthClick = {}, onWellnessClick = {}, onProfileClick = {}, onBookAppointmentClick = {}, onScreeningGuideClick = {}, onChatClick = {})
    }
}
