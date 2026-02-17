package com.example.biomarkerai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.SplashGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    onBack: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(
        ChatMessage("Hello! I'm your BioScan AI assistant. How can I help you understand your health today?", false)
    ) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).background(SplashGreen, CircleShape), contentAlignment = Alignment.Center) {
                            Text("AI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Health Assistant", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Online", fontSize = 11.sp, color = SplashGreen)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask about your biomarkers...") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SplashGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        maxLines = 3
                    )
                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val userMsg = inputText
                                messages.add(ChatMessage(userMsg, true))
                                inputText = ""
                                scope.launch {
                                    listState.animateScrollToItem(messages.size - 1)
                                    delay(1000)
                                    val aiResponse = getAiResponse(userMsg)
                                    messages.add(ChatMessage(aiResponse, false))
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                            }
                        },
                        containerColor = SplashGreen,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (message.isUser) SplashGreen else Color.White,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            ),
            tonalElevation = 2.dp,
            shadowElevation = 1.dp
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = if (message.isUser) Color.White else Color.Black,
                fontSize = 14.sp
            )
        }
    }
}

private fun getAiResponse(query: String): String {
    val q = query.lowercase()
    return when {
        q.contains("fpg") || q.contains("glucose") -> "Fasting Plasma Glucose (FPG) measures your blood sugar after an 8-hour fast. Values between 100-125 mg/dL suggest pre-diabetes."
        q.contains("hba1c") -> "HbA1c shows your average blood sugar over the past 3 months. A normal level is below 5.7%."
        q.contains("ldl") || q.contains("cholesterol") -> "LDL is often called 'bad' cholesterol. High levels can lead to plaque buildup in arteries. Target is usually below 100 mg/dL."
        q.contains("diet") || q.contains("eat") -> "For metabolic health, I recommend a diet rich in fiber, lean proteins, and healthy fats. Try to limit refined sugars and processed carbs."
        q.contains("exercise") -> "Consistent physical activity helps improve insulin sensitivity. Aim for at least 150 minutes of moderate-intensity exercise per week."
        q.contains("hello") || q.contains("hi") -> "Hello! I'm here to help you understand your health data. What would you like to know?"
        else -> "That's a great question. While I can provide general health information, please consult your doctor for medical advice. Would you like to know more about a specific biomarker?"
    }
}
