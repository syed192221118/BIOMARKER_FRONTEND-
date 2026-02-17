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
import com.example.biomarkerai.ui.theme.SplashGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmBookingScreen(
    onBack: () -> Unit,
    onConfirmBooking: () -> Unit,
    onHomeClick: () -> Unit,
    onHealthClick: () -> Unit,
    onWellnessClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var agreedToPolicy by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Booking", fontWeight = FontWeight.Bold) },
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
                        onClick = onConfirmBooking,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
                        shape = RoundedCornerShape(14.dp),
                        enabled = agreedToPolicy
                    ) {
                        Text("Confirm Booking", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    NavigationBarItem(selected = false, onClick = onHomeClick, icon = { Icon(Icons.Default.Home, "Home") }, label = { Text("Home") })
                    NavigationBarItem(selected = false, onClick = onHealthClick, icon = { Icon(Icons.Default.FavoriteBorder, "Health") }, label = { Text("Health") })
                    NavigationBarItem(selected = false, onClick = onWellnessClick, icon = { Icon(Icons.Default.Shield, "Wellness") }, label = { Text("Wellness") })
                    NavigationBarItem(selected = false, onClick = onProfileClick, icon = { Icon(Icons.Default.PersonOutline, "Profile") }, label = { Text("Profile") })
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
                Text(text = "Step 5 of 5", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                Text(text = "100 %", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 1.0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SplashBlue,
                trackColor = Color(0xFFE9ECEF)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Review Details", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))

            Spacer(modifier = Modifier.height(24.dp))

            // Review Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Doctor info
                    ReviewItem(
                        icon = {
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray)) {
                                Icon(Icons.Default.Person, null, modifier = Modifier.fillMaxSize().padding(8.dp), tint = Color.White)
                            }
                        },
                        label = "Doctor",
                        value = "Dr. Sarah Smith",
                        subValue = "Cardiologist",
                        showEdit = true
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                    ReviewItem(
                        icon = { Icon(Icons.Default.CalendarToday, null, tint = SplashBlue, modifier = Modifier.size(24.dp)) },
                        label = "Date",
                        value = "Thursday, Oct 24, 2024",
                        showEdit = true
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                    ReviewItem(
                        icon = { Icon(Icons.Default.AccessTime, null, tint = SplashBlue, modifier = Modifier.size(24.dp)) },
                        label = "Time",
                        value = "10:30 AM (30 mins)",
                        showEdit = true
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                    ReviewItem(
                        icon = { Icon(Icons.Default.LocationOn, null, tint = SplashBlue, modifier = Modifier.size(24.dp)) },
                        label = "Location",
                        value = "Main Clinic, Room 302",
                        subValue = "Location"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                    ReviewItem(
                        icon = { Icon(Icons.Default.Description, null, tint = SplashBlue, modifier = Modifier.size(24.dp)) },
                        label = "Reason",
                        value = "General Checkup",
                        subValue = "Reason",
                        showEdit = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Payment Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Payment Summary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Consultation Fee", color = Color.Gray, fontSize = 14.sp)
                        Text("₹1,200.00", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Booking Fee", color = Color.Gray, fontSize = 14.sp)
                        Text("₹50.00", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("₹1,250.00", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SplashGreen)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Payment will be collected at the clinic.", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Policy Agreement
            Row(verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = agreedToPolicy,
                    onCheckedChange = { agreedToPolicy = it },
                    colors = CheckboxDefaults.colors(checkedColor = SplashBlue)
                )
                Text(
                    text = "I agree to the cancellation policy. Appointments cancelled less than 24 hours in advance may incur a fee.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ReviewItem(icon: @Composable () -> Unit, label: String, value: String, subValue: String? = null, showEdit: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            if (subValue == null) {
                Text(text = label, fontSize = 12.sp, color = Color.Gray)
                Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            } else {
                Text(text = value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subValue, fontSize = 14.sp, color = Color.Gray)
            }
        }
        if (showEdit) {
            IconButton(onClick = {}) { Icon(Icons.Default.Edit, "Edit", tint = SplashGreen, modifier = Modifier.size(20.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmBookingScreenPreview() {
    BiomarkerAITheme {
        ConfirmBookingScreen({}, {}, {}, {}, {}, {})
    }
}
