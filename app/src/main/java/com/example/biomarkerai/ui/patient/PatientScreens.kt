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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(onPatientClick: (String) -> Unit, onBack: () -> Unit) {
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            Text(risk, color = if (risk == "High Risk") Color.Red else Color.Green, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileScreen(patientId: String, onHistoryClick: () -> Unit, onReportClick: () -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Patient Profile") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState()).padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(patientId, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Female • 45 yrs • ID #8492", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Recent History", fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), onClick = onHistoryClick) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("Full Screening - Oct 24, 2024", modifier = Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowRight, null)
                }
            }
            Text("Lab Results", fontWeight = FontWeight.Bold)
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), onClick = onReportClick) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("Blood Work Panel", modifier = Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowRight, null)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientHistoryScreen(onReportClick: (String) -> Unit, onBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("History") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            HistoryItem("Oct 24, 2024", "Moderate Risk") { onReportClick("Oct 24, 2024") }
            HistoryItem("Jul 12, 2024", "Low Risk") { onReportClick("Jul 12, 2024") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItem(date: String, risk: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), onClick = onClick) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, null, modifier = Modifier.padding(end = 16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(date)
                Text(risk, color = if (risk == "Low Risk") Color.Green else Color.Red)
            }
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientListScreenPreview() {
    BiomarkerAITheme {
        PatientListScreen(onPatientClick = {}, onBack = {})
    }
}
