package com.example.biomarkerai.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.R
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashGreen

@Composable
fun OnboardingFlow(onFinish: () -> Unit) {
    var step by remember { mutableStateOf(1) }

    if (step == 1) {
        WelcomeScreen(onNext = { step = 2 })
    } else {
        FeaturesScreen(onContinue = onFinish)
    }
}

@Composable
fun WelcomeScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Profile/Doctor Image Placeholder
        Box(
            modifier = Modifier
                .size(240.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main circular image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(10.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                // In a real app, use an actual image:
                // Image(painter = painterResource(id = R.drawable.doctor_img), contentDescription = null, contentScale = ContentScale.Crop)
                Icon(
                    imageVector = Icons.Default.Info, 
                    contentDescription = null,
                    modifier = Modifier.size(100.dp).align(Alignment.Center),
                    tint = Color.Gray
                )
            }
            
            // Floating mini icons
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = (-10).dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SplashBlue, modifier = Modifier.size(20.dp))
            }
            
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = 20.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = SplashGreen, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Early Detection Saves Lives",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
            color = Color(0xFF1A1C1E)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Advanced AI analysis of your biomarkers to predict and prevent metabolic diseases before they start.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Accuracy Card
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = SplashBlue)
                    Text(text = "99% Accuracy", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SplashBlue, modifier = Modifier.padding(top = 8.dp))
                }
            }
            
            // Real-time Card
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1FAF4)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = SplashGreen)
                    Text(text = "Real-time", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SplashGreen, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(text = "Get Started", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeaturesScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Key Features",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C1E)
        )

        Text(
            text = "Everything you need to manage your metabolic health.",
            fontSize = 16.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FeatureItem(
                title = "Biomarker Analysis",
                desc = "Deep analysis of blood work and vital signs.",
                icon = Icons.Default.Search,
                color = Color(0xFFE3F2FD),
                iconColor = Color(0xFF1E88E5)
            )
            FeatureItem(
                title = "AI Risk Prediction",
                desc = "Machine learning models to forecast health risks.",
                icon = Icons.Default.Info,
                color = Color(0xFFF3E5F5),
                iconColor = Color(0xFF8E24AA)
            )
            FeatureItem(
                title = "Health Tracking",
                desc = "Monitor trends in your metabolic health over time.",
                icon = Icons.Default.Notifications,
                color = Color(0xFFE8F5E9),
                iconColor = Color(0xFF43A047)
            )
            FeatureItem(
                title = "Report Generation",
                desc = "Detailed medical reports for you and your doctor.",
                icon = Icons.Default.List,
                color = Color(0xFFFFF3E0),
                iconColor = Color(0xFFFB8C00)
            )
        }

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SplashBlue),
            shape = RoundedCornerShape(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FeatureItem(title: String, desc: String, icon: ImageVector, color: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(28.dp))
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1C1E))
            Text(text = desc, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    OnboardingFlow(onFinish = {})
}
