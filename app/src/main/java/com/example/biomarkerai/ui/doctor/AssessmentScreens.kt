package com.example.biomarkerai.ui.doctor

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.biomarkerai.ui.theme.SplashBlue
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream

data class AssessmentData(
    var age: String = "35", var gender: String = "Male", var height: String = "175", var weight: String = "70",
    var smokingStatus: String = "Never", var alcohol: String = "None", var sleep: String = "6-8 hrs", var physicalActivity: String = "Active",
    var systolic: String = "120", var diastolic: String = "80", var heartRate: String = "72", var waist: String = "85",
    var symptoms: Set<String> = emptySet(),
    var fpg: String = "108", var ppg: String = "140", var hba1c: String = "5.9",
    var totalCholesterol: String = "180", var ldl: String = "145", var hdl: String = "42", var triglycerides: String = "160",
    var creatinine: String = "0.9", var bun: String = "12", var egfr: String = "90",
    var alt: String = "25", var ast: String = "22", var ggt: String = "30",
    var insulin: String = "5.0", var tsh: String = "2.5",
    var hscrp: String = "2.5", var esr: String = "",
    var familyHistory: Set<String> = setOf("Father: Diabetes"), var medicalHistory: Set<String> = setOf("Mother: Hypertension")
)

@Composable
fun AssessmentFlow(
    currentStep: Int,
    onStepChange: (Int) -> Unit,
    labSubStep: Int,
    onLabSubStepChange: (Int) -> Unit,
    data: AssessmentData,
    onDataChange: (AssessmentData) -> Unit,
    onComplete: () -> Unit,
    onBack: () -> Unit,
    onScreeningGuideClick: () -> Unit
) {
    Scaffold(
        topBar = {
            if (currentStep in 1..8) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            when {
                                currentStep == 8 && labSubStep > 1 && labSubStep != 8 -> onLabSubStepChange(labSubStep - 1)
                                currentStep == 8 && (labSubStep == 1 || labSubStep == 8) -> onLabSubStepChange(0)
                                currentStep > 1 -> onStepChange(currentStep - 1)
                                else -> onBack()
                            }
                        }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF1B5E20)) }
                        Text(
                            text = when {
                                currentStep == 8 && labSubStep == 1 -> "Glucose Profile"
                                currentStep == 8 && labSubStep == 2 -> "Lipid Profile"
                                currentStep == 8 && labSubStep == 3 -> "Kidney Function"
                                currentStep == 8 && labSubStep == 4 -> "Liver Function"
                                currentStep == 8 && labSubStep == 5 -> "Hormone Panel"
                                currentStep == 8 && labSubStep == 6 -> "Inflammation"
                                currentStep == 8 && labSubStep == 8 -> "Review Biomarkers"
                                currentStep == 8 -> "Lab Reports"
                                currentStep == 7 -> "Review Data"
                                currentStep == 6 -> "Symptoms"
                                currentStep == 5 -> "Vital Signs"
                                currentStep == 4 -> "Medical History"
                                currentStep == 3 -> "Family History"
                                currentStep == 2 -> "Lifestyle"
                                currentStep == 1 -> "Basic Information"
                                else -> ""
                            },
                            fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val progress = if (currentStep == 8 && labSubStep > 0) {
                        if (labSubStep == 8) 1f else labSubStep.toFloat() / 8f
                    } else currentStep.toFloat() / 8f
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (currentStep == 8 && labSubStep > 0) "Step ${if(labSubStep==8) 8 else labSubStep} of 8" else "Step $currentStep of 8", fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.weight(1f))
                        Text("${(progress * 100).toInt()}%", fontSize = 12.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = SplashBlue, trackColor = Color(0xFFF1F5F9))
                }
            } else if (currentStep >= 10) {
                Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (currentStep > 10) onStepChange(currentStep - 1) else { onStepChange(8); onLabSubStepChange(8) } }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF1B5E20)) }
                        Text(
                            text = when(currentStep) {
                                10 -> "AI Analysis Results"
                                11 -> "Analysis: Comparison"
                                12 -> "Analysis: Patterns"
                                13 -> "Analysis: Insulin"
                                14 -> "Analysis: Heart"
                                15 -> "Metabolic Syndrome"
                                16 -> "Risk Assessment"
                                17 -> "Diabetes Risk"
                                18 -> "Your Plan"
                                19 -> "Medical Report"
                                else -> "Analysis"
                            },
                            fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B5E20)
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (currentStep > 0 && !(currentStep == 8 && labSubStep == 0) && currentStep != 9 && currentStep != 19) {
                Button(
                    onClick = {
                        when {
                            currentStep == 8 && labSubStep < 6 -> onLabSubStepChange(labSubStep + 1)
                            currentStep == 8 && labSubStep == 6 -> onLabSubStepChange(8)
                            currentStep == 8 && labSubStep == 8 -> onStepChange(9)
                            currentStep < 19 -> onStepChange(currentStep + 1)
                            else -> onComplete()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SplashBlue)
                ) {
                    val btnText = when {
                        currentStep == 18 -> "View Full Report"
                        currentStep == 17 -> "Next: Your Plan"
                        currentStep == 16 -> "Next: Diabetes Risk"
                        currentStep == 15 -> "Next: Risk Assessment"
                        currentStep == 14 -> "Next: Metabolic Syndrome"
                        currentStep == 13 -> "Next: Heart Risk"
                        currentStep == 12 -> "Next: Insulin Analysis"
                        currentStep == 11 -> "Next: Pattern Detection"
                        currentStep == 10 -> "Verify & Continue"
                        currentStep == 8 && labSubStep == 8 -> "Run AI Analysis"
                        currentStep == 7 -> "Confirm & Continue"
                        currentStep == 6 -> "Review Data"
                        else -> "Next Step"
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(btnText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(if (currentStep == 0) Color.White else Color(0xFFF8F9FA))
                .let { if (currentStep != 9) it.verticalScroll(rememberScrollState()) else it }
        ) {
            Box(modifier = Modifier.padding(if (currentStep == 0) 0.dp else 24.dp)) {
                when (currentStep) {
                    0 -> MetabolicHealthIntro(onStart = { onStepChange(1) }, onBack = onBack, onScreeningGuideClick = onScreeningGuideClick)
                    1 -> DemographicsStep(data) { onDataChange(it) }
                    2 -> LifestyleStep(data) { onDataChange(it) }
                    3 -> FamilyHistoryStep(data) { onDataChange(it) }
                    4 -> MedicalHistoryStep(data) { onDataChange(it) }
                    5 -> VitalSignsStep(data) { onDataChange(it) }
                    6 -> SymptomsStep(data) { onDataChange(it) }
                    7 -> ReviewSummaryStep(data, onEdit = { onStepChange(it) }, onScreeningGuideClick = onScreeningGuideClick)
                    8 -> {
                        when (labSubStep) {
                            0 -> LabReportsSelectionStep(
                                onEnterManually = { onLabSubStepChange(1) },
                                onExtractAI = {
                                    onDataChange(data.copy(fpg = "108", hba1c = "5.9", ldl = "145", hdl = "42", triglycerides = "160", tsh = "2.5"))
                                    onLabSubStepChange(8)
                                }
                            )
                            1 -> GlucoseProfileStep(data) { onDataChange(it) }
                            2 -> LipidProfileStep(data) { onDataChange(it) }
                            3 -> KidneyFunctionStep(data) { onDataChange(it) }
                            4 -> LiverFunctionStep(data) { onDataChange(it) }
                            5 -> HormonePanelStep(data) { onDataChange(it) }
                            6 -> InflammationStep(data) { onDataChange(it) }
                            8 -> ReviewBiomarkersStep(data)
                        }
                    }
                    9 -> AnalyzingBiomarkersStep { onStepChange(10) }
                    10 -> AIAnalysisResultsStep()
                    11 -> AnalysisComparisonStep(data)
                    12 -> AnalysisPatternsStep()
                    13 -> AnalysisInsulinStep(data)
                    14 -> AnalysisHeartStep(data)
                    15 -> MetabolicSyndromeStep(data)
                    16 -> RiskAssessmentStep(data)
                    17 -> DiabetesRiskStep()
                    18 -> PersonalizedPlanStep()
                    19 -> MedicalReportStep(data, onComplete)
                }
            }
        }
    }
}

@Composable
fun DemographicsStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column {
        Text("Basic Information", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        LabField("Age", data.age, { onDataChange(data.copy(age = it)) }, "years")
        LabField("Gender", data.gender, { onDataChange(data.copy(gender = it)) }, "")
        LabField("Height", data.height, { onDataChange(data.copy(height = it)) }, "cm")
        LabField("Weight", data.weight, { onDataChange(data.copy(weight = it)) }, "kg")
    }
}

@Composable
fun LifestyleStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column {
        Text("Lifestyle Habits", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        SegmentedControl("Smoking Status", listOf("Never", "Former", "Current"), data.smokingStatus, { onDataChange(data.copy(smokingStatus = it)) }, Icons.Default.SmokingRooms)
        DropdownField("Alcohol Consumption", listOf("None", "Occasional", "Moderate", "Heavy"), data.alcohol, { onDataChange(data.copy(alcohol = it)) }, Icons.Default.LocalBar)
        SegmentedControl("Average Sleep", listOf("< 6 hrs", "6-8 hrs", "> 8 hrs"), data.sleep, { onDataChange(data.copy(sleep = it)) }, Icons.Default.NightsStay)
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.DirectionsRun, null, modifier = Modifier.size(18.dp), tint = Color.Gray); Spacer(modifier = Modifier.width(8.dp)); Text("Physical Activity", fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
            Spacer(modifier = Modifier.height(12.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
                Column {
                    listOf("Sedentary", "Lightly Active", "Active", "Very Active").forEachIndexed { index, activity ->
                        val isSelected = data.physicalActivity == activity
                        Box(modifier = Modifier.fillMaxWidth().clickable { onDataChange(data.copy(physicalActivity = activity)) }.background(if (isSelected) SplashBlue.copy(alpha = 0.05f) else Color.Transparent).padding(16.dp)) {
                            Text(activity, color = if (isSelected) SplashBlue else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
                        }
                        if (index < 3) HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F5F9))
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyHistoryStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column {
        Text("Family History", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp)); Text("Select any conditions present in your immediate family (parents, siblings).", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        listOf("Type 2 Diabetes", "Hypertension", "Heart Disease", "Stroke", "High Cholesterol", "Obesity", "Thyroid Disorder", "None of the above").forEach { condition ->
            val isSelected = data.familyHistory.contains(condition)
            MultiSelectionItem(condition, isSelected) {
                val newSet = if (condition == "None of the above") setOf(condition) else {
                    val current = data.familyHistory.toMutableSet()
                    current.remove("None of the above")
                    if (isSelected) current.remove(condition) else current.add(condition)
                    current
                }
                onDataChange(data.copy(familyHistory = newSet))
            }
        }
    }
}

@Composable
fun MedicalHistoryStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column {
        Text("Existing Conditions", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp)); Text("Do you currently have or have you been diagnosed with any of the following?", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        listOf("Pre-diabetes", "PCOS (Polycystic Ovary Syndrome)", "Fatty Liver", "Hypothyroidism", "Sleep Apnea", "Gout", "None").forEach { condition ->
            val isSelected = data.medicalHistory.contains(condition)
            MultiSelectionItem(condition, isSelected) {
                val newSet = if (condition == "None") setOf(condition) else {
                    val current = data.medicalHistory.toMutableSet()
                    current.remove("None")
                    if (isSelected) current.remove(condition) else current.add(condition)
                    current
                }
                onDataChange(data.copy(medicalHistory = newSet))
            }
        }
    }
}

@Composable
fun VitalSignsStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column {
        Text("Vital Signs", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp)); Text("Enter your most recent measurements.", fontSize = 14.sp, color = Color.Gray); Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF).copy(alpha = 0.5f)), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Timeline, null, tint = SplashBlue, modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Blood Pressure", fontWeight = FontWeight.Bold, color = SplashBlue) }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) { Text("Systolic (Top)", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp)); OutlinedTextField(value = data.systolic, onValueChange = { onDataChange(data.copy(systolic = it)) }, suffix = { Text("mmHg", fontSize = 12.sp, color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) { Text("Diastolic (Bottom)", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp)); OutlinedTextField(value = data.diastolic, onValueChange = { onDataChange(data.copy(diastolic = it)) }, suffix = { Text("mmHg", fontSize = 12.sp, color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }
                }
            }
        }
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2).copy(alpha = 0.5f)), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Favorite, null, tint = Color.Red, modifier = Modifier.size(20.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Heart Rate", fontWeight = FontWeight.Bold, color = Color.Red) }
                Spacer(modifier = Modifier.height(16.dp)); Text("Resting Heart Rate", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp)); OutlinedTextField(value = data.heartRate, onValueChange = { onDataChange(data.copy(heartRate = it)) }, suffix = { Text("bpm", fontSize = 12.sp, color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        }
        LabField("Waist Circumference (Optional)", data.waist, { onDataChange(data.copy(waist = it)) }, "cm")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SymptomsStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column {
        Text("Current Symptoms", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp)); Text("Select any symptoms you have experienced recently.", fontSize = 14.sp, color = Color.Gray); Spacer(modifier = Modifier.height(24.dp))
        FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("Fatigue", "Frequent Urination", "Increased Thirst", "Blurred Vision", "Slow Healing Sores", "Tingling Hands/Feet", "Unexplained Weight Loss", "Dark Skin Patches").forEach { symptom ->
                val isSelected = data.symptoms.contains(symptom)
                FilterChip(selected = isSelected, onClick = { val current = data.symptoms.toMutableSet(); if (isSelected) current.remove(symptom) else current.add(symptom); onDataChange(data.copy(symptoms = current)) }, label = { Text(symptom, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) }, shape = RoundedCornerShape(24.dp), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = SplashBlue.copy(alpha = 0.1f), selectedLabelColor = SplashBlue, containerColor = Color.White, labelColor = Color.Gray), border = FilterChipDefaults.filterChipBorder(borderColor = Color(0xFFE2E8F0), selectedBorderColor = SplashBlue, borderWidth = 1.dp, selectedBorderWidth = 1.dp, enabled = true, selected = isSelected))
            }
        }
    }
}

@Composable
fun ReviewSummaryStep(data: AssessmentData, onEdit: (Int) -> Unit, onScreeningGuideClick: () -> Unit) {
    Column {
        Text("Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp)); Text("Please verify your information before proceeding to lab data.", fontSize = 14.sp, color = Color.Gray); Spacer(modifier = Modifier.height(24.dp))
        ReviewSection("Demographics", "${data.gender}, ${data.age} yrs\n${data.height}cm, ${data.weight}kg", 1, onEdit)
        ReviewSection("Lifestyle", "${data.smokingStatus}\n${data.alcohol}\n${data.sleep}", 2, onEdit)
        ReviewSection("History", (data.familyHistory + data.medicalHistory).joinToString("\n"), 3, onEdit)
        ReviewSection("Vitals", "BP: ${data.systolic}/${data.diastolic}\nHR: ${data.heartRate} bpm", 5, onEdit)
        Spacer(modifier = Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth().clickable { onScreeningGuideClick() }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(Color(0xFFF0F7FF), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.MenuBook, null, tint = SplashBlue, modifier = Modifier.size(20.dp)) }
                Spacer(modifier = Modifier.width(16.dp)); Column(modifier = Modifier.weight(1f)) { Text("View Screening Guide", fontWeight = FontWeight.Bold, fontSize = 14.sp); Text("See which biomarkers we'll analyze", fontSize = 11.sp, color = Color.Gray) }
                Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun ReviewSection(title: String, content: String, stepIndex: Int, onEdit: (Int) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) { Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f)); IconButton(onClick = { onEdit(stepIndex) }, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.Edit, null, tint = SplashBlue, modifier = Modifier.size(16.dp)) } }
            Spacer(modifier = Modifier.height(4.dp)); Text(content, fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)
        }
    }
}

@Composable
fun GlucoseProfileStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column { Text("Glucose Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(24.dp)); LabField("FPG", data.fpg, { onDataChange(data.copy(fpg = it)) }, "mg/dL"); LabField("PPG", data.ppg, { onDataChange(data.copy(ppg = it)) }, "mg/dL"); LabField("HbA1c", data.hba1c, { onDataChange(data.copy(hba1c = it)) }, "%") }
}

@Composable
fun LipidProfileStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column { Text("Lipid Profile", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(24.dp)); LabField("Total Cholesterol", data.totalCholesterol, { onDataChange(data.copy(totalCholesterol = it)) }, "mg/dL"); LabField("LDL", data.ldl, { onDataChange(data.copy(ldl = it)) }, "mg/dL"); LabField("HDL", data.hdl, { onDataChange(data.copy(hdl = it)) }, "mg/dL"); LabField("Triglycerides", data.triglycerides, { onDataChange(data.copy(triglycerides = it)) }, "mg/dL") }
}

@Composable
fun KidneyFunctionStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column { Text("Kidney Function", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(24.dp)); LabField("Creatinine", data.creatinine, { onDataChange(data.copy(creatinine = it)) }, "mg/dL"); LabField("BUN", data.bun, { onDataChange(data.copy(bun = it)) }, "mg/dL") }
}

@Composable
fun LiverFunctionStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column { Text("Liver Function", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(24.dp)); LabField("ALT", data.alt, { onDataChange(data.copy(alt = it)) }, "U/L"); LabField("AST", data.ast, { onDataChange(data.copy(ast = it)) }, "U/L") }
}

@Composable
fun HormonePanelStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column { Text("Hormone Panel", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(24.dp)); LabField("Fasting Insulin", data.insulin, { onDataChange(data.copy(insulin = it)) }, "μIU/mL"); LabField("TSH", data.tsh, { onDataChange(data.copy(tsh = it)) }, "mIU/L") }
}

@Composable
fun InflammationStep(data: AssessmentData, onDataChange: (AssessmentData) -> Unit) {
    Column { Text("Inflammation", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(24.dp)); LabField("hs-CRP", data.hscrp, { onDataChange(data.copy(hscrp = it)) }, "mg/L"); LabField("ESR", data.esr, { onDataChange(data.copy(esr = it)) }, "mm/hr") }
}

@Composable
fun ReviewBiomarkersStep(data: AssessmentData) {
    Column { 
        Text("Add Biomarker Data", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Upload your recent blood test report or enter values manually.", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).background(Color(0xFFF0F7FF), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Description, null, tint = SplashBlue, modifier = Modifier.size(20.dp)) }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("LAB_REPORT_OCT.pdf", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Analyzed Oct 24, 2023", fontSize = 11.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                ExtractionStepItem("Extracted 6 biomarkers", true)
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(start = 28.dp)) {
                    BiomarkerReviewRow("Fasting Glucose", data.fpg, "mg/dL")
                    BiomarkerReviewRow("HbA1c", data.hba1c, "%")
                    BiomarkerReviewRow("LDL Cholesterol", data.ldl, "mg/dL")
                }
            }
        }
    }
}

@Composable
fun AnalyzingBiomarkersStep(onComplete: () -> Unit) {
    var progress by remember { mutableFloatStateOf(0f) }; var currentSubStep by remember { mutableIntStateOf(1) }
    LaunchedEffect(Unit) { while (currentSubStep <= 3) { val targetProgress = currentSubStep * 0.33f; while (progress < targetProgress) { delay(30); progress += 0.02f }; delay(1000); currentSubStep++ }; onComplete() }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(64.dp))
        Box(modifier = Modifier.size(120.dp).background(Color(0xFFF0F7FF), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Psychology, null, tint = SplashBlue, modifier = Modifier.size(60.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("Analyzing Biomarkers", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("Our AI is processing your data with medical algorithms.", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 48.dp))
        Spacer(modifier = Modifier.height(48.dp))
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth(0.8f).height(10.dp).clip(RoundedCornerShape(5.dp)), color = SplashBlue, trackColor = Color(0xFFF1F5F9))
        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            ExtractionStepItem("Identifying data patterns...", currentSubStep >= 1)
            ExtractionStepItem("Calculating risk scores...", currentSubStep >= 2)
            ExtractionStepItem("Generating personalized insights...", currentSubStep >= 3)
        }
    }
}

@Composable
fun AIAnalysisResultsStep() {
    var selectedTab by remember { mutableIntStateOf(0) }
    Column {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = SplashBlue)) { Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(56.dp).background(Color.White.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Biotech, null, tint = Color.White, modifier = Modifier.size(28.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Column { Text("Analysis Complete", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("Extracted 6 biomarkers & identified 3 risks.", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp) } } }
        Spacer(modifier = Modifier.height(24.dp)); Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFF1F5F9)).padding(4.dp)) {
            Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(if (selectedTab == 0) Color.White else Color.Transparent).clickable { selectedTab = 0 }.padding(vertical = 8.dp), contentAlignment = Alignment.Center) { Text("Suggested Risks", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp, color = if (selectedTab == 0) SplashBlue else Color.Gray) }
            Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)).background(if (selectedTab == 1) Color.White else Color.Transparent).clickable { selectedTab = 1 }.padding(vertical = 8.dp), contentAlignment = Alignment.Center) { Text("Extracted Data", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium, fontSize = 13.sp, color = if (selectedTab == 1) SplashBlue else Color.Gray) }
        }
        Spacer(modifier = Modifier.height(20.dp)); if (selectedTab == 0) { RiskItemCard("Pre-diabetes", "High Confidence", Icons.Default.Whatshot, Color(0xFFEF4444)); RiskItemCard("Dyslipidemia", "Medium Confidence", Icons.Default.Timeline, Color(0xFFF59E0B)); RiskItemCard("Metabolic Syndrome", "Medium Confidence", Icons.Default.Favorite, Color(0xFFF59E0B)) } else { BiomarkerResultCard("Fasting Glucose", "108", "mg/dL", "70-99", "High", Color(0xFFEF4444)); BiomarkerResultCard("HbA1c", "5.9", "%", "< 5.7", "High", Color(0xFFEF4444)); BiomarkerResultCard("LDL Cholesterol", "145", "mg/dL", "< 100", "High", Color(0xFFEF4444)) }
    }
}

@Composable
fun AnalysisComparisonStep(data: AssessmentData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Normal Range Comparison", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Visualizing where you stand compared to healthy baselines.", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        ComparisonBar(label = "Fasting Glucose", value = data.fpg, unit = "mg/dL", min = 70f, max = 100f, isHigherBetter = false)
        Spacer(modifier = Modifier.height(24.dp))
        ComparisonBar(label = "LDL Cholesterol", value = data.ldl, unit = "mg/dL", min = 0f, max = 100f, isHigherBetter = false)
        Spacer(modifier = Modifier.height(24.dp))
        ComparisonBar(label = "Triglycerides", value = data.triglycerides, unit = "mg/dL", min = 0f, max = 150f, isHigherBetter = false)
        Spacer(modifier = Modifier.height(24.dp))
        ComparisonBar(label = "HDL Cholesterol", value = data.hdl, unit = "mg/dL", min = 40f, max = 60f, isHigherBetter = true)
    }
}

@Composable
fun ComparisonBar(label: String, value: String, unit: String, min: Float, max: Float, isHigherBetter: Boolean) {
    val numericValue = value.toFloatOrNull() ?: 0f
    val isNormal = if (isHigherBetter) numericValue >= min else numericValue <= max
    val statusColor = if (isNormal) Color(0xFF2E7D32) else Color(0xFFEF4444)

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.weight(1f))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = statusColor)
            Text(" $unit", fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(12.dp))
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(brush = androidx.compose.ui.graphics.Brush.horizontalGradient(colors = if (isHigherBetter) listOf(Color(0xFFFEE2E2), Color(0xFFDCFCE7)) else listOf(Color(0xFFDCFCE7), Color(0xFFFEE2E2)))))
            val buffer = (max - min).coerceAtLeast(max * 0.2f)
            val displayMin = (min - buffer).coerceAtLeast(0f)
            val displayMax = max + buffer
            val progress = ((numericValue - displayMin) / (displayMax - displayMin)).coerceIn(0f, 1f)
            Box(modifier = Modifier.fillMaxHeight().width(3.dp).align(Alignment.CenterStart).offset(x = maxWidth * progress).background(statusColor))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(min.toInt().toString(), fontSize = 11.sp, color = Color.LightGray)
            Spacer(modifier = Modifier.weight(1f))
            Text("Target: ${max.toInt()}", fontSize = 11.sp, color = Color.LightGray)
        }
    }
}

@Composable fun AnalysisPatternsStep() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Box(modifier = Modifier.size(48.dp).background(Color(0xFFF3E8FF), CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Fingerprint, null, tint = Color(0xFF9333EA), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Pattern Detected", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF1F5F9))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Insulin Resistance Cluster", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Our AI has detected a cluster of biomarkers often associated with early-stage insulin resistance.", fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                PatternBulletItem("Elevated Triglycerides", Color(0xFFEF4444))
                PatternBulletItem("Borderline Fasting Glucose", Color(0xFFF59E0B))
                PatternBulletItem("Low HDL Cholesterol", Color(0xFFF59E0B))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("What this means", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Even though individual values might be within \"normal\" ranges, the combination suggests your body is working harder than it should to process sugar.", fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun PatternBulletItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(6.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
    }
}

@Composable fun AnalysisInsulinStep(data: AssessmentData) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("HOMA-IR Score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(modifier = Modifier.size(240.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 16.dp.toPx()
                drawArc(color = Color(0xFFF1F5F9), startAngle = 180f, sweepAngle = 180f, useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
                drawArc(color = Color(0xFFF59E0B), startAngle = 180f, sweepAngle = 120f, useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("2.4", fontWeight = FontWeight.Bold, fontSize = 48.sp, color = Color(0xFF1E293B))
                Text("Moderate Risk", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFF59E0B))
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF1F5F9))) {
            Column(modifier = Modifier.padding(24.dp)) {
                ScoreRangeRow("Optimal", "< 1.0", Color(0xFF2E7D32))
                Spacer(modifier = Modifier.height(16.dp))
                ScoreRangeRow("Early Resistance", "1.9 - 2.9", Color(0xFFF59E0B))
                Spacer(modifier = Modifier.height(16.dp))
                ScoreRangeRow("Significant Resistance", "> 3.0", Color(0xFFEF4444))
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Text("Your HOMA-IR score indicates early signs of insulin resistance. This is reversible with lifestyle changes.", textAlign = TextAlign.Center, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 24.dp), lineHeight = 20.sp)
    }
}

@Composable
fun ScoreRangeRow(label: String, range: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp, color = Color.Gray)
        Text(range, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable fun AnalysisHeartStep(data: AssessmentData) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(64.dp).background(Color(0xFFFFF1F2), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Favorite, null, tint = Color(0xFFEF4444), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text("ASCVD Risk Score", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("10-Year Heart Disease Risk", fontSize = 13.sp, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth().height(140.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("3.2%", fontWeight = FontWeight.Bold, fontSize = 48.sp, color = Color(0xFF166534))
                Text("LOW RISK", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF166534), letterSpacing = 1.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Contributing Factors", fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))
        
        ContributingFactorRow("Age & Gender", "Low Impact", Color(0xFFF1F5F9))
        Spacer(modifier = Modifier.height(12.dp))
        ContributingFactorRow("Blood Pressure", "Normal", Color(0xFFF1F5F9))
        Spacer(modifier = Modifier.height(12.dp))
        ContributingFactorRow("Cholesterol", "Moderate", Color(0xFFFFEDD5))
    }
}

@Composable
fun ContributingFactorRow(label: String, status: String, tagColor: Color) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF1F5F9))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp, color = Color(0xFF475569))
            Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(tagColor).padding(horizontal = 12.dp, vertical = 6.dp)) {
                Text(status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (tagColor == Color(0xFFFFEDD5)) Color(0xFF9A3412) else Color(0xFF475569))
            }
        }
    }
}

@Composable fun MetabolicSyndromeStep(data: AssessmentData) {
    Column {
        Text("Metabolic Syndrome", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        MetabolicCriteriaItem("Blood Pressure", data.systolic.toIntOrNull() ?: 0 >= 130, "≥ 130/85 mmHg")
        MetabolicCriteriaItem("Fasting Glucose", data.fpg.toIntOrNull() ?: 0 >= 100, "≥ 100 mg/dL")
        MetabolicCriteriaItem("Triglycerides", data.triglycerides.toIntOrNull() ?: 0 >= 150, "≥ 150 mg/dL")
        MetabolicCriteriaItem("HDL Cholesterol", data.hdl.toIntOrNull() ?: 60 < 40, "< 40 mg/dL (M) / 50 (F)")
        MetabolicCriteriaItem("Waist Circumference", data.waist.toIntOrNull() ?: 0 >= 90, "> 90 cm (M) / 80 (F)")
    }
}

@Composable fun MetabolicCriteriaItem(label: String, isMet: Boolean, target: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF1F5F9))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Criteria: $target", fontSize = 11.sp, color = Color.Gray)
            }
            if (isMet) Icon(Icons.Default.Warning, null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
            else Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable fun RiskAssessmentStep(data: AssessmentData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Overall Metabolic Risk", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(48.dp))
        Box(modifier = Modifier.size(160.dp).background(Color(0xFFFFEDD5), CircleShape), contentAlignment = Alignment.Center) {
            Text("MODERATE", fontWeight = FontWeight.Bold, color = Color(0xFF9A3412), fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text("Your profile shows moderate risk of metabolic complications based on extracted biomarkers and vitals.", textAlign = TextAlign.Center, color = Color.Gray, modifier = Modifier.padding(horizontal = 24.dp))
    }
}

@Composable
fun DiabetesRiskStep() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Diabetes Prediction", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF1F5F9))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Risk Projection", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.height(200.dp).fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.Bottom) {
                    Column(modifier = Modifier.fillMaxHeight().padding(bottom = 24.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        listOf("36%", "27%", "18%", "9%", "0%").forEach { Text(it, fontSize = 10.sp, color = Color.Gray) }
                    }
                    Row(modifier = Modifier.weight(1f).fillMaxHeight().padding(bottom = 24.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.Bottom) {
                        RiskBarItem("1 Year", 0.15f, Color(0xFFF87171))
                        RiskBarItem("5 Years", 0.45f, Color(0xFFF87171))
                        RiskBarItem("10 Years", 0.95f, Color(0xFFF87171))
                    }
                }
                Text("Estimated probability of developing Type 2 Diabetes without intervention.", fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 16.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED))) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Key Drivers", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF9A3412))
                Spacer(modifier = Modifier.height(16.dp))
                listOf("Elevated Fasting Insulin", "High Triglycerides", "Sedentary Lifestyle").forEach { driver ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                        Box(modifier = Modifier.size(4.dp).background(Color(0xFF9A3412), CircleShape))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(driver, fontSize = 13.sp, color = Color(0xFF9A3412), fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun RiskBarItem(label: String, heightFractions: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
        Box(modifier = Modifier.fillMaxHeight(heightFractions).width(32.dp).clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)).background(color))
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
    }
}

@Composable fun PersonalizedPlanStep() {
    Column {
        Text("Your Personalized Plan", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        PlanItem("Dietary Adjustments", "Reduce refined carbs and increase fiber intake.", Icons.Default.Restaurant)
        PlanItem("Exercise Plan", "At least 150 min of moderate activity per week.", Icons.Default.DirectionsRun)
        PlanItem("Sleep Hygiene", "Maintain consistent 7-8 hours of sleep.", Icons.Default.NightsStay)
    }
}

@Composable fun PlanItem(title: String, description: String, icon: ImageVector) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF1F5F9))) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = SplashBlue, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column { Text(title, fontWeight = FontWeight.Bold); Text(description, fontSize = 12.sp, color = Color.Gray) }
        }
    }
}

@Composable fun MedicalReportStep(data: AssessmentData, onComplete: () -> Unit) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Medical Report", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("BioScan AI", color = SplashBlue, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Metabolic Assessment Report", color = Color.Gray, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Date: Oct 24, 2024", color = Color.Gray, fontSize = 11.sp)
                        Text("Patient ID: #8492", color = Color.Gray, fontSize = 11.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text("1. Summary", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Patient shows moderate metabolic risk with early signs of insulin resistance. Cardiovascular risk remains low.", fontSize = 13.sp, color = Color.DarkGray, lineHeight = 20.sp)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("2. Key Biomarkers", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    BiomarkerReportItem("Fasting Glucose", "${data.fpg} mg/dL", Color.Black, Modifier.weight(1f))
                    BiomarkerReportItem("HbA1c", "${data.hba1c}%", Color.Black, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    BiomarkerReportItem("Triglycerides", "${data.triglycerides} mg/dL", Color.Red, Modifier.weight(1f))
                    BiomarkerReportItem("LDL", "${data.ldl} mg/dL", Color.Red, Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text("3. AI Analysis", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("HOMA-IR Score: 2.4 (Moderate). Pattern recognition suggests metabolic syndrome precursors.", fontSize = 13.sp, color = Color.DarkGray, lineHeight = 20.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                Text("Powered by BioScan AI Medical Engine • Not a diagnosis", fontSize = 10.sp, color = Color.LightGray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = { shareReport(context, data) },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SplashBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SplashBlue)
            ) {
                Icon(Icons.Default.Share, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share", fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = { generatePdf(context, data) },
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SplashBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SplashBlue)
            ) {
                Icon(Icons.Default.Download, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("PDF", fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1967D2))
        ) {
            Icon(Icons.Default.Home, null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back to Dashboard", fontWeight = FontWeight.Bold)
        }
    }
}

fun generatePdf(context: Context, data: AssessmentData): File? {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    var y = 40f
    paint.textSize = 24f
    paint.isFakeBoldText = true
    paint.color = android.graphics.Color.BLUE
    canvas.drawText("BioScan AI Report", 40f, y, paint)
    
    y += 40f
    paint.textSize = 14f
    paint.isFakeBoldText = false
    paint.color = android.graphics.Color.GRAY
    canvas.drawText("Metabolic Assessment Report - Oct 24, 2024", 40f, y, paint)

    y += 60f
    paint.color = android.graphics.Color.BLACK
    paint.isFakeBoldText = true
    canvas.drawText("1. Summary", 40f, y, paint)
    y += 20f
    paint.isFakeBoldText = false
    canvas.drawText("Patient shows moderate metabolic risk with early signs of insulin resistance.", 40f, y, paint)

    y += 40f
    paint.isFakeBoldText = true
    canvas.drawText("2. Key Biomarkers", 40f, y, paint)
    y += 25f
    paint.isFakeBoldText = false
    canvas.drawText("Fasting Glucose: ${data.fpg} mg/dL", 40f, y, paint)
    y += 20f
    canvas.drawText("HbA1c: ${data.hba1c}%", 40f, y, paint)
    y += 20f
    canvas.drawText("Triglycerides: ${data.triglycerides} mg/dL", 40f, y, paint)
    y += 20f
    canvas.drawText("LDL Cholesterol: ${data.ldl} mg/dL", 40f, y, paint)

    y += 40f
    paint.isFakeBoldText = true
    canvas.drawText("3. AI Insights", 40f, y, paint)
    y += 20f
    paint.isFakeBoldText = false
    canvas.drawText("HOMA-IR Score: 2.4 (Moderate Risk)", 40f, y, paint)

    pdfDocument.finishPage(page)

    val file = File(context.cacheDir, "BioScan_Report.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF Generated in cache", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show()
        return null
    }
    pdfDocument.close()
    return file
}

fun shareReport(context: Context, data: AssessmentData) {
    val file = generatePdf(context, data)
    if (file != null && file.exists()) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_SUBJECT, "BioScan AI Medical Report")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Report"))
    }
}

@Composable
fun BiomarkerReportItem(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable fun BiomarkerResultCard(label: String, value: String, unit: String, range: String, status: String, statusColor: Color) { Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) { Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(40.dp).background(statusColor.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Opacity, null, tint = statusColor, modifier = Modifier.size(20.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Column(modifier = Modifier.weight(1f)) { Text(label, fontWeight = FontWeight.Bold, fontSize = 15.sp); Text("Range: $range $unit", color = Color.Gray, fontSize = 11.sp) }; Column(horizontalAlignment = Alignment.End) { Row(verticalAlignment = Alignment.CenterVertically) { Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = statusColor); Spacer(modifier = Modifier.width(4.dp)); Text(unit, color = Color.Gray, fontSize = 11.sp) }; Text(status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold) } } } }
@Composable fun RiskItemCard(title: String, confidence: String, icon: ImageVector, color: Color) { Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) { Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(40.dp).background(color.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(20.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Column(modifier = Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp); Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(color.copy(alpha = 0.1f)).padding(horizontal = 6.dp, vertical = 2.dp)) { Text(confidence, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold) } }; Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.LightGray) } } }
@Composable fun ExtractionStepItem(text: String, isCompleted: Boolean) { Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) { if (isCompleted) Icon(Icons.Default.Check, null, tint = SplashBlue, modifier = Modifier.size(16.dp)) else CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = Color.LightGray); Spacer(modifier = Modifier.width(12.dp)); Text(text, fontSize = 12.sp, color = if (isCompleted) Color.Black else Color.Gray) } }
@Composable fun ExtractedValueRow(label: String, value: String) { Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) { Text(label, fontSize = 13.sp, color = Color(0xFF4A5568), modifier = Modifier.weight(1f)); Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3748)) } }
@Composable fun LabField(label: String, value: String, onValueChange: (String) -> Unit, unit: String) { Column(modifier = Modifier.padding(bottom = 20.dp)) { Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp)); OutlinedTextField(value = value, onValueChange = onValueChange, suffix = { Text(unit, fontSize = 12.sp, color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) } }
@Composable fun BiomarkerReviewRow(label: String, value: String, unit: String) { Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) { Text(label, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568), modifier = Modifier.weight(1f)); Text("$value $unit", fontWeight = FontWeight.Bold, color = Color(0xFF2D3748)) } }
@Composable fun MetabolicHealthIntro(onStart: () -> Unit, onBack: () -> Unit, onScreeningGuideClick: () -> Unit) { Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }; Spacer(modifier = Modifier.height(24.dp)); Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) { Box(modifier = Modifier.size(80.dp).background(Color(0xFFF0F7FF), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.List, null, tint = SplashBlue, modifier = Modifier.size(40.dp)) }; Spacer(modifier = Modifier.height(32.dp)); Text("Metabolic Health Assessment", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center); Spacer(modifier = Modifier.height(16.dp)); Text("This screening will analyze your vitals, lifestyle, and biomarkers.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 24.dp)) }; Spacer(modifier = Modifier.height(48.dp)); AssessmentIntroInfoItem(Icons.Default.DateRange, "5-10 Minutes", "Time to complete"); AssessmentIntroInfoItem(Icons.Default.Info, "AI Powered", "Analyzed by medical algorithms"); AssessmentIntroInfoItem(Icons.Default.Search, "Screening Guide", "Diseases we screen", true, onScreeningGuideClick); Spacer(modifier = Modifier.height(48.dp)); Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = SplashBlue), shape = RoundedCornerShape(16.dp)) { Text("Start Assessment", fontWeight = FontWeight.Bold, fontSize = 18.sp) } } }
@Composable fun AssessmentIntroInfoItem(icon: ImageVector, text: String, subtext: String, hasArrow: Boolean = false, onClick: () -> Unit = {}) { Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF8F9FA)).clickable(onClick = onClick).padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Box(modifier = Modifier.size(40.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) { Icon(icon, null, tint = SplashBlue, modifier = Modifier.size(20.dp)) }; Spacer(modifier = Modifier.width(16.dp)); Column { Text(text, fontWeight = FontWeight.Bold); Text(subtext, color = Color.Gray, fontSize = 12.sp) }; if (hasArrow) { Spacer(modifier = Modifier.weight(1f)); Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.LightGray) } } }
@Composable fun LabReportsSelectionStep(onEnterManually: () -> Unit, onExtractAI: (Uri) -> Unit) { val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { if (it != null) onExtractAI(it) }; Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) { Spacer(modifier = Modifier.height(32.dp)); Box(modifier = Modifier.size(80.dp).background(Color(0xFFF0F7FF), CircleShape), contentAlignment = Alignment.Center) { Icon(Icons.Default.Description, null, tint = SplashBlue, modifier = Modifier.size(40.dp)) }; Spacer(modifier = Modifier.height(24.dp)); Text("Add Lab Reports", fontWeight = FontWeight.Bold, fontSize = 20.sp); Spacer(modifier = Modifier.height(32.dp)); Card(modifier = Modifier.fillMaxWidth().height(140.dp).clickable { launcher.launch("*/*") }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFE2E8F0))) { Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.CloudUpload, null, tint = Color.LightGray, modifier = Modifier.size(32.dp)); Spacer(modifier = Modifier.height(12.dp)); Text("Upload PDF / Image", fontWeight = FontWeight.Bold, fontSize = 14.sp) } }; Spacer(modifier = Modifier.height(32.dp)); Text("OR", fontSize = 12.sp, color = Color.LightGray); Spacer(modifier = Modifier.height(32.dp)); OutlinedButton(onClick = onEnterManually, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp)) { Text("Enter Manual", fontWeight = FontWeight.Bold) } } }
@Composable fun SegmentedControl(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit, icon: ImageVector? = null) { Column(modifier = Modifier.padding(bottom = 24.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { if (icon != null) { Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color.Gray); Spacer(modifier = Modifier.width(8.dp)) }; Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }; Spacer(modifier = Modifier.height(12.dp)); Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { options.forEach { option -> val isSelected = option == selectedOption; Button(onClick = { onOptionSelected(option) }, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) SplashBlue else Color.White, contentColor = if (isSelected) Color.White else Color.Gray), border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null, contentPadding = PaddingValues(0.dp)) { Text(option, fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) } } } } }
@OptIn(ExperimentalMaterial3Api::class) @Composable fun DropdownField(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit, icon: ImageVector? = null) { var expanded by remember { mutableStateOf(false) }; Column(modifier = Modifier.padding(bottom = 24.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { if (icon != null) { Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color.Gray); Spacer(modifier = Modifier.width(8.dp)) }; Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }; Spacer(modifier = Modifier.height(12.dp)); ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) { OutlinedTextField(value = selectedOption, onValueChange = {}, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp)); ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { options.forEach { option -> DropdownMenuItem(text = { Text(option) }, onClick = { onOptionSelected(option); expanded = false }) } } } } }
@Composable fun MultiSelectionItem(text: String, isSelected: Boolean, onClick: () -> Unit) { Card(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clickable { onClick() }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, if (isSelected) SplashBlue else Color(0xFFE2E8F0))) { Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) { Text(text, modifier = Modifier.weight(1f), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = if (isSelected) SplashBlue else Color.Black); if (isSelected) Icon(Icons.Default.CheckCircle, null, tint = SplashBlue) } } }
