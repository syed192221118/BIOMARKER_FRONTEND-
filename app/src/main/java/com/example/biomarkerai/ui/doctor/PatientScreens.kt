package com.example.biomarkerai.ui.doctor

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    onPatientClick: (String) -> Unit, 
    onBack: () -> Unit,
    onDashboardClick: () -> Unit,
    onPatientsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Patients") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") } }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            PatientListItem("Sarah Johnson", "High Risk", "Today") { onPatientClick("Sarah Johnson") }
            PatientListItem("Michael Chen", "Low Risk", "Yesterday") { onPatientClick("Michael Chen") }
            PatientListItem("Emma Davis", "Moderate Risk", "2 days ago") { onPatientClick("Emma Davis") }
            PatientListItem("James Wilson", "High Risk", "3 days ago") { onPatientClick("James Wilson") }
            PatientListItem("Linda Martinez", "Low Risk", "1 week ago") { onPatientClick("Linda Martinez") }
            PatientListItem("Robert Taylor", "Moderate Risk", "1 week ago") { onPatientClick("Robert Taylor") }
        }
    }
}

@Composable
fun PatientListItem(name: String, risk: String, lastVisit: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(lastVisit, fontSize = 12.sp, color = Color.Gray)
            }
            Text(risk, color = if (risk == "High Risk") Color.Red else if (risk == "Low Risk") Color.Green else Color(0xFFFF9800), fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileScreen(
    patientId: String,
    onHistoryClick: () -> Unit,
    onReportClick: () -> Unit,
    onNewScreeningClick: () -> Unit,
    onBack: () -> Unit,
    onDashboardClick: () -> Unit,
    onPatientsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Patient Profile") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.height(16.dp))
                Text(patientId, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text("Female • 45 yrs • ID #8492", color = Color.Gray, fontSize = 14.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                VitalsCompactCard("Heart Rate", "78 bpm", Icons.Default.Favorite, Color.Red, Modifier.weight(1f))
                VitalsCompactCard("Blood Pressure", "120/80", Icons.Default.Info, SplashBlue, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                VitalsCompactCard("Weight", "68 kg", Icons.Default.Person, Color.Green, Modifier.weight(1f))
                VitalsCompactCard("Temp", "36.6°C", Icons.Default.Info, Color(0xFFFFA500), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("Recent History", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            
            HistoryCard("Full Screening", "Dr. Smith • General Checkup", "Oct 24", onHistoryClick)
            HistoryCard("Lab Results", "Blood Work Panel", "Sep 12", onReportClick)
            
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onNewScreeningClick, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(12.dp)) {
                    Text("New Screening")
                }
                OutlinedButton(onClick = onReportClick, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(12.dp)) {
                    Text("View Reports")
                }
            }
        }
    }
}

@Composable
fun VitalsCompactCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(label, fontSize = 10.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HistoryCard(title: String, subtitle: String, date: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), onClick = onClick, colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(date.split(" ")[0], fontSize = 12.sp, color = Color.Gray)
                Text(date.split(" ")[1], fontWeight = FontWeight.Bold, fontSize = 18.sp, color = SplashBlue)
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.Info, null, tint = SplashBlue)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHistoryScreen(
    onReportClick: (String) -> Unit, 
    onBack: () -> Unit,
    onDashboardClick: () -> Unit,
    onPatientsClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("History") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            HistoryListItem("Oct 24, 2024", "Moderate Risk", "84") { onReportClick("Oct 24, 2024") }
            HistoryListItem("Jul 12, 2024", "Low Risk", "22") { onReportClick("Jul 12, 2024") }
            HistoryListItem("Mar 05, 2024", "Low Risk", "30") { onReportClick("Mar 05, 2024") }
            HistoryListItem("Dec 10, 2023", "High Risk", "92") { onReportClick("Dec 10, 2023") }
        }
    }
}

@Composable
fun HistoryListItem(date: String, risk: String, score: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DateRange, null, modifier = Modifier.size(32.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(date, fontWeight = FontWeight.Bold)
                Row {
                    Text(risk, color = if (risk == "Low Risk") Color.Green else if (risk == "High Risk") Color.Red else Color(0xFFFF9800), fontSize = 12.sp)
                    Text(" • Score: $score", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientListScreenPreview() {
    BiomarkerAITheme {
        PatientListScreen({}, {}, {}, {}, {}, {})
    }
}
