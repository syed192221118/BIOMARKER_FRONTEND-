package com.example.biomarkerai.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.BiomarkerAITheme
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashGreen

@Composable
fun AssessmentIntroScreen(onStartAssessment: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        IconButton(onClick = { /* Back */ }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(80.dp).background(Color(0xFFF0F7FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.List, contentDescription = null, tint = SplashBlue, modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Metabolic Health Assessment",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "This screening will analyze your vitals, lifestyle, and biomarkers to predict metabolic risks.",
                color = Color(0xFF616161),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        AssessmentInfoItem(icon = Icons.Default.DateRange, text = "5-10 Minutes", subtext = "Estimated time to complete")
        AssessmentInfoItem(icon = Icons.Default.Info, text = "AI Powered", subtext = "Analyzed by advanced medical algorithms")
        AssessmentInfoItem(icon = Icons.Default.Search, text = "Screening Guide", subtext = "Learn about diseases & biomarkers we screen", hasArrow = true)

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStartAssessment,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 320.dp)
                .align(Alignment.CenterHorizontally)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Start Assessment", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AssessmentInfoItem(icon: ImageVector, text: String, subtext: String, hasArrow: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color(0xFFF8F9FA), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(40.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = SplashBlue, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = text, fontWeight = FontWeight.Bold)
            Text(text = subtext, color = Color(0xFF616161), fontSize = 12.sp)
        }
        if (hasArrow) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AssessmentIntroScreenPreview() {
    BiomarkerAITheme {
        AssessmentIntroScreen(onStartAssessment = {})
    }
}
