package com.example.biomarkerai.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.SplashBlue
import com.example.biomarkerai.ui.theme.SplashDarkBlue
import com.example.biomarkerai.ui.theme.SplashGreen

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SplashBlue, SplashDarkBlue)
                )
            )
    ) {
        // Artistic background glows
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.6f), Color.Transparent),
                    center = center.copy(x = size.width * 0.2f, y = size.height * 0.2f),
                    radius = size.width * 0.6f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SplashGreen.copy(alpha = 0.5f), Color.Transparent),
                    center = center.copy(x = size.width * 0.8f, y = size.height * 0.8f),
                    radius = size.width * 0.8f
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // DNA Icon Container (Glassmorphism effect)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                DNAsvgIcon()
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "BioScan AI",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AI Biomarker Screening for\nMetabolic Disease Risk",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }

        // Footer
        Text(
            text = "Powered by Medical AI",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}

@Composable
fun DNAsvgIcon() {
    Canvas(modifier = Modifier.size(60.dp)) {
        val width = size.width
        val height = size.height
        val strokeWidth = 3.dp.toPx()
        
        // Simple DNA representation (two intertwined paths)
        // This is a placeholder for the actual DNA icon
        for (i in 0 until 10) {
            val y = height * (i.toFloat() / 10f)
            val xOffset = 15.dp.toPx() * kotlin.math.sin(i.toFloat() * 0.8f)
            
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width / 2 + xOffset, y)
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width / 2 - xOffset, y)
            )
            
            // Draw horizontal rungs
            if (i % 2 == 0) {
                drawLine(
                    color = Color.White.copy(alpha = 0.5f),
                    start = androidx.compose.ui.geometry.Offset(width / 2 - xOffset, y),
                    end = androidx.compose.ui.geometry.Offset(width / 2 + xOffset, y),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
