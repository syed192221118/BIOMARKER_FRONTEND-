package com.example.biomarkerai.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.biomarkerai.ui.theme.SplashBlue

@Composable
fun DiseaseList(query: String) {
    val diseases = listOf(
        DiseaseGuideData(
            "Cardiovascular Disease",
            "Conditions affecting the heart and blood vessels.",
            Icons.Default.Favorite,
            listOf("Troponins" to "< 0.04 ng/mL", "BNP/NT-proBNP" to "< 100 pg/mL", "hs-CRP" to "< 2.0 mg/L", "LDL Cholesterol" to "< 100 mg/dL", "Lipid Panel" to "Varies"),
            listOf("High Blood Pressure", "High Cholesterol", "Smoking", "Obesity", "Physical Inactivity")
        ),
        DiseaseGuideData(
            "Type 2 Diabetes",
            "Chronic condition affecting how the body processes blood sugar.",
            Icons.Default.Opacity,
            listOf("HbA1c" to ">= 6.5%", "Fasting Glucose" to ">= 126 mg/dL", "Oral Glucose Tolerance" to ">= 200 mg/dL", "Random Glucose" to ">= 200 mg/dL"),
            listOf("Overweight/Obesity", "Inactivity", "Family History", "Age (45+)", "History of Gestational Diabetes")
        ),
        DiseaseGuideData(
            "Liver Disease (NAFLD)",
            "Build-up of fat in the liver not caused by alcohol.",
            Icons.Default.MonitorHeart,
            listOf("ALT" to "Elevated (>30-40 U/L)", "AST" to "Elevated (>30-40 U/L)", "GGT" to "Elevated", "Liver Ultrasound" to "Fatty infiltration", "FIB-4 Score" to "Assessment of fibrosis"),
            listOf("Obesity", "Type 2 Diabetes", "High Cholesterol", "Metabolic Syndrome", "Rapid Weight Loss")
        ),
        DiseaseGuideData(
            "Chronic Kidney Disease",
            "Gradual loss of kidney function over time.",
            Icons.Default.LocalHospital,
            listOf("eGFR" to "< 60 mL/min/1.73m²", "Creatinine" to "Elevated", "Albuminuria" to "Urine ACR >= 30mg/g", "BUN" to "Elevated"),
            listOf("Diabetes", "High Blood Pressure", "Heart Disease", "Family History", "Older Age")
        ),
        DiseaseGuideData(
            "Metabolic Syndrome",
            "Cluster of conditions increasing heart disease and diabetes risk.",
            Icons.Default.Balance,
            listOf("Waist Circumference" to "> 40in (M) / > 35in (F)", "Triglycerides" to ">= 150 mg/dL", "HDL" to "< 40 (M) / < 50 (F)", "BP" to ">= 130/85 mmHg", "Fasting Glucose" to ">= 100 mg/dL"),
            listOf("Insulin Resistance", "Obesity", "Physical Inactivity", "Genetics", "Age")
        ),
        DiseaseGuideData(
            "Thyroid Disorders",
            "Problems with thyroid hormone production.",
            Icons.Default.DeviceThermostat,
            listOf("TSH" to "0.4 - 4.0 mIU/L", "Free T4" to "0.8 - 1.8 ng/dL", "Free T3" to "2.3 - 4.2 pg/mL", "TPO Antibodies" to "Indicates Autoimmune"),
            listOf("Autoimmune Disease", "Iodine Deficiency", "Radiation Therapy", "Family History", "Previous Thyroid Surgery")
        ),
        DiseaseGuideData(
            "Dyslipidemia",
            "Unhealthy levels of one or more kinds of lipid (fat).",
            Icons.Default.FlashOn,
            listOf("Total Cholesterol" to "> 200 mg/dL", "LDL" to "> 130 mg/dL", "HDL" to "< 40 mg/dL", "Triglycerides" to "> 150 mg/dL", "Non-HDL" to "> 160 mg/dL"),
            listOf("Diet high in saturated/trans fats", "Obesity", "Genetics (FH)", "Diabetes", "Smoking")
        ),
        DiseaseGuideData(
            "Anemia / Iron Deficiency",
            "Lack of enough healthy red blood cells to carry adequate oxygen.",
            Icons.Default.Bloodtype,
            listOf("Hemoglobin" to "< 13.5 (M) / < 12.0 (F) g/dL", "Hematocrit" to "Low", "Ferritin" to "< 30 ng/mL (Iron deficiency)", "MCV" to "Size of RBCs"),
            listOf("Iron deficiency", "Vitamin deficiency (B12, Folate)", "Chronic Disease", "Blood loss")
        ),
        DiseaseGuideData(
            "PCOS",
            "Polycystic Ovary Syndrome affecting hormonal balance.",
            Icons.Default.Female,
            listOf("Total Testosterone" to "Elevated", "FSH/LH Ratio" to "LH > FSH", "Fasting Insulin" to "Elevated", "Pelvic Ultrasound" to "Polycystic ovaries"),
            listOf("Insulin Resistance", "Obesity", "Family History", "Irregular Periods")
        ),
        DiseaseGuideData(
            "Gout / Hyperuricemia",
            "Form of arthritis characterized by severe pain and joint redness.",
            Icons.Default.HealthAndSafety,
            listOf("Uric Acid" to "> 7.0 mg/dL", "Joint Fluid Analysis" to "Urate crystals"),
            listOf("High purine diet", "Obesity", "Alcohol consumption", "Kidney Disease")
        ),
        DiseaseGuideData(
            "Osteoporosis",
            "Condition where bones become weak and brittle.",
            Icons.Default.Accessibility,
            listOf("DEXA Scan (T-Score)" to "< -2.5", "Calcium" to "Varies", "Vitamin D" to "Often Low"),
            listOf("Age", "Menopause", "Low Calcium intake", "Smoking", "Sedentary lifestyle")
        ),
        DiseaseGuideData(
            "Chronic Inflammation",
            "Prolonged inflammatory response in the body.",
            Icons.Default.Warning,
            listOf("hs-CRP" to "> 3.0 mg/L", "ESR" to "Elevated", "Fibrinogen" to "Elevated"),
            listOf("Autoimmune diseases", "Untreated infections", "Obesity", "Smoking", "Stress")
        ),
        DiseaseGuideData(
            "Pancreatitis",
            "Inflammation of the pancreas.",
            Icons.Default.MedicalServices,
            listOf("Amylase" to "3x Normal", "Lipase" to "3x Normal", "Abdominal CT" to "Inflammation"),
            listOf("Gallstones", "Heavy Alcohol use", "High Triglycerides", "Abdominal trauma")
        ),
        DiseaseGuideData(
            "Vitamin D Deficiency",
            "Insufficient level of Vitamin D in the body.",
            Icons.Default.LightMode,
            listOf("25-hydroxy Vitamin D" to "< 20 ng/mL"),
            listOf("Lack of sun exposure", "Dark skin", "Obesity", "Certain medications")
        ),
        DiseaseGuideData(
            "Hypertension",
            "High blood pressure increasing heart risk.",
            Icons.Default.Speed,
            listOf("Systolic" to ">= 130 mmHg", "Diastolic" to ">= 80 mmHg"),
            listOf("High Salt diet", "Lack of exercise", "Obesity", "Alcohol", "Genetics")
        ),
        DiseaseGuideData(
            "Heart Failure",
            "Heart muscle doesn't pump blood as well as it should.",
            Icons.Default.HeartBroken,
            listOf("BNP" to "Elevated", "Ejection Fraction" to "< 40%", "Chest X-Ray" to "Enlarged heart"),
            listOf("CVD", "Hypertension", "Diabetes", "Obesity")
        ),
        DiseaseGuideData(
            "Stroke Risk",
            "Risk of blood supply interruption to the brain.",
            Icons.Default.Psychology,
            listOf("Carotid Ultrasound" to "Plaque/Stenosis", "Blood Pressure" to "High"),
            listOf("Hypertension", "AFib", "Smoking", "Diabetes", "High Cholesterol")
        ),
        DiseaseGuideData(
            "Obesity / Adiposity",
            "Excessive body fat increasing risk of health problems.",
            Icons.Default.Boy,
            listOf("BMI" to ">= 30 kg/m²", "Waist Circumference" to "Elevated"),
            listOf("Caloric imbalance", "Genetic factors", "Inactivity", "Sleep deprivation")
        ),
        DiseaseGuideData(
            "Sleep Apnea",
            "Sleep disorder where breathing repeatedly stops and starts.",
            Icons.Default.Bedtime,
            listOf("Sleep Study (AHI)" to "> 5 events/hr"),
            listOf("Obesity", "Thick neck", "Narrow airway", "Male gender", "Age")
        ),
        DiseaseGuideData(
            "Autoimmune Thyroid",
            "Immune system attacks the thyroid gland.",
            Icons.Default.Shield,
            listOf("TPO Antibodies" to "Positive", "TG Antibodies" to "Positive", "TSH" to "Abnormal"),
            listOf("Family history", "Other autoimmune diseases", "Radiation exposure")
        )
    )

    diseases.filter { it.title.contains(query, ignoreCase = true) }.forEach { disease ->
        DiseaseGuideCard(disease)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun BiomarkerList(query: String) {
    val biomarkers = listOf(
        BiomarkerGuideData(
            "hs-CRP",
            "Normal: < 2.0 mg/L",
            "High-sensitivity C-reactive protein produced by the liver in response to inflammation.",
            listOf("Cardiovascular Disease", "Metabolic Syndrome", "Chronic Inflammation", "Autoimmune Disorders"),
            "Indicates acute or chronic inflammation, infection, or tissue injury. Values > 3.0 mg/L indicate high cardiovascular risk.",
            "Normal; indicates low systemic inflammation. Very low values are generally not clinically significant."
        ),
        BiomarkerGuideData(
            "Troponins (I/T)",
            "Normal: < 0.04 ng/mL",
            "Cardiac-specific proteins released into the blood when the heart muscle is damaged.",
            listOf("Myocardial Infarction", "Myocarditis", "Heart Failure", "Severe Sepsis"),
            "Indicates cardiac muscle damage or acute coronary syndrome. Requires immediate clinical evaluation.",
            "Normal; indicates no significant recent heart muscle damage."
        ),
        BiomarkerGuideData(
            "HbA1c",
            "Normal: < 5.7 %",
            "Glycated hemoglobin reflecting average blood sugar levels over the past 2-3 months.",
            listOf("Type 2 Diabetes", "Prediabetes", "Metabolic Syndrome"),
            "Indicates poor long-term glucose control. 5.7-6.4% is Prediabetes; >= 6.5% is Diabetes.",
            "Normal range; indicates good glucose control. Very low levels may occur in hemolytic anemia."
        ),
        BiomarkerGuideData(
            "Fasting Glucose",
            "Normal: 70 - 99 mg/dL",
            "The amount of sugar in your blood after an overnight fast (8-12 hours).",
            listOf("Diabetes", "Insulin Resistance", "Prediabetes", "Pancreatitis"),
            "Indicates hyperglycemia. 100-125 mg/dL is IFG (Prediabetes); >= 126 mg/dL is Diabetes.",
            "May indicate hypoglycemia if < 70 mg/dL. Can be caused by medication, alcohol, or endocrine issues."
        ),
        BiomarkerGuideData(
            "LDL Cholesterol",
            "Normal: < 100 mg/dL",
            "Low-density lipoprotein, often called 'bad' cholesterol, which contributes to plaque buildup.",
            listOf("Atherosclerosis", "Coronary Artery Disease", "Peripheral Artery Disease"),
            "Increased risk of plaque buildup and heart disease. Targets may be lower (< 70 or < 55) for high-risk patients.",
            "Desirable; associated with lower cardiovascular risk. Extremely low levels are rare."
        ),
        BiomarkerGuideData(
            "HDL Cholesterol",
            "Normal: > 40 (M) / > 50 (F) mg/dL",
            "High-density lipoprotein, or 'good' cholesterol, which helps remove other forms of cholesterol.",
            listOf("Cardiovascular Health", "Metabolic Syndrome"),
            "Generally protective, though extremely high levels (> 100 mg/dL) may not provide additional benefit.",
            "Increased risk of heart disease; 'good' cholesterol is too low to be effectively cardioprotective."
        ),
        BiomarkerGuideData(
            "Triglycerides",
            "Normal: < 150 mg/dL",
            "The most common type of fat in the body, used for energy between meals.",
            listOf("Metabolic Syndrome", "Pancreatitis", "Heart Disease", "Obesity"),
            "Values > 150 mg/dL increase risk of heart disease. > 500 mg/dL increases risk of acute pancreatitis.",
            "Generally not a medical concern. Can be seen with low-fat diets or hyperthyroidism."
        ),
        BiomarkerGuideData(
            "ALT (Alanine Aminotransferase)",
            "Normal: 7 - 56 U/L",
            "An enzyme found mostly in the liver; its release into the blood is a sign of liver injury.",
            listOf("Hepatitis", "Fatty Liver Disease (NAFLD)", "Cirrhosis", "Liver Cancer"),
            "Indicates potential liver inflammation or damage. More specific for the liver than AST.",
            "Always normal; low levels have no clinical significance."
        ),
        BiomarkerGuideData(
            "Creatinine",
            "Normal: 0.6 - 1.2 mg/dL",
            "A waste product from muscle breakdown filtered by the kidneys.",
            listOf("Chronic Kidney Disease", "Kidney Failure", "Dehydration", "Obstruction"),
            "Indicates reduced kidney function. Levels rise as kidney filtration rate (eGFR) falls.",
            "May relate to low muscle mass, malnutrition, or advanced liver disease."
        ),
        BiomarkerGuideData(
            "TSH (Thyroid Stimulating Hormone)",
            "Normal: 0.4 - 4.0 mIU/L",
            "Pituitary hormone that stimulates the thyroid to produce T4 and T3.",
            listOf("Hypothyroidism", "Hyperthyroidism", "Pituitary disorders"),
            "High TSH usually indicates Hypothyroidism (underactive thyroid) as the body tries to stimulate more production.",
            "Low TSH usually indicates Hyperthyroidism (overactive thyroid) or pituitary insufficiency."
        ),
        BiomarkerGuideData(
            "Ferritin",
            "Normal: 20 - 250 ng/mL",
            "A blood protein that contains iron; the best indicator of total body iron stores.",
            listOf("Iron Deficiency Anemia", "Hemochromatosis", "Inflammation"),
            "Can indicate iron overload (Hemochromatosis) or act as an acute phase reactant during inflammation.",
            "Indicates depleted iron stores, often before anemia develops. Common in iron deficiency."
        ),
        BiomarkerGuideData(
            "Uric Acid",
            "Normal: 3.4 - 7.0 mg/dL",
            "A waste product found in blood, created when the body breaks down purines.",
            listOf("Gout", "Kidney Stones", "Metabolic Syndrome"),
            "High levels can lead to gout or kidney stones. Often associated with high purine diet.",
            "Generally rare, can be seen with low purine diet or some medications."
        ),
        BiomarkerGuideData(
            "Insulin",
            "Normal: 2.6 - 24.9 uIU/mL",
            "Hormone that allows your body to use sugar (glucose).",
            listOf("Insulin Resistance", "PCOS", "Metabolic Syndrome"),
            "High fasting insulin indicates the body is struggling to manage blood sugar, a sign of insulin resistance.",
            "Can be seen in Type 1 Diabetes or late-stage Type 2 Diabetes when the pancreas fails."
        ),
        BiomarkerGuideData(
            "Albumin",
            "Normal: 3.4 - 5.4 g/dL",
            "A protein made by your liver that helps keep fluid in your bloodstream.",
            listOf("Liver Disease", "Kidney Disease", "Malnutrition"),
            "High levels are usually due to dehydration.",
            "Low levels can indicate liver or kidney disease, or inflammation."
        ),
        BiomarkerGuideData(
            "Hemoglobin",
            "Normal: 13.5-17.5 (M) / 12.0-15.5 (F) g/dL",
            "Protein in red blood cells that carries oxygen.",
            listOf("Anemia", "Dehydration", "Lung Disease"),
            "Can be seen in dehydration or polycythemia.",
            "Indicates anemia, which can cause fatigue and weakness."
        ),
        BiomarkerGuideData(
            "Free T4",
            "Normal: 0.8 - 1.8 ng/dL",
            "The main form of thyroid hormone circulating in the blood.",
            listOf("Hyperthyroidism", "Hypothyroidism"),
            "High levels indicate an overactive thyroid (hyperthyroidism).",
            "Low levels indicate an underactive thyroid (hypothyroidism)."
        ),
        BiomarkerGuideData(
            "Free T3",
            "Normal: 2.3 - 4.2 pg/mL",
            "The active form of thyroid hormone.",
            listOf("Hyperthyroidism", "Thyroiditis"),
            "High levels usually indicate hyperthyroidism.",
            "Low levels can be seen in hypothyroidism or severe non-thyroidal illness."
        ),
        BiomarkerGuideData(
            "ESR",
            "Normal: 0 - 20 mm/hr",
            "Erythrocyte Sedimentation Rate; a marker of inflammation.",
            listOf("Infection", "Autoimmune Disease", "Cancer"),
            "High ESR indicates inflammation in the body.",
            "Low levels are usually not a concern."
        ),
        BiomarkerGuideData(
            "eGFR",
            "Normal: > 90 mL/min/1.73m²",
            "Estimated Glomerular Filtration Rate; best test to measure your level of kidney function.",
            listOf("Chronic Kidney Disease"),
            "N/A",
            "Low levels indicate decreasing kidney function."
        ),
        BiomarkerGuideData(
            "Total Protein",
            "Normal: 6.0 - 8.3 g/dL",
            "Measures the total amount of albumin and globulin in your body.",
            listOf("Liver Disease", "Kidney Disease"),
            "Can indicate chronic inflammation or bone marrow disorders.",
            "Can indicate liver or kidney disease."
        ),
        BiomarkerGuideData(
            "Calcium",
            "Normal: 8.5 - 10.2 mg/dL",
            "One of the most important minerals in the body.",
            listOf("Bone Disease", "Parathyroid issues"),
            "High levels can indicate parathyroid issues or bone problems.",
            "Low levels can be seen with Vitamin D deficiency or kidney issues."
        ),
        BiomarkerGuideData(
            "Phosphorus",
            "Normal: 2.5 - 4.5 mg/dL",
            "Mineral that works with calcium to build bones.",
            listOf("Kidney Disease", "Bone disorders"),
            "High levels are often seen in kidney disease.",
            "Low levels can indicate hyperparathyroidism or malnutrition."
        ),
        BiomarkerGuideData(
            "Vitamin D",
            "Normal: 30 - 100 ng/mL",
            "Helps your body absorb calcium for healthy bones.",
            listOf("Osteoporosis", "Rickets", "Immune dysfunction"),
            "Can be seen with excessive supplementation. Very high levels can cause hypercalcemia.",
            "Leads to bone weakening and increased risk of fractures."
        ),
        BiomarkerGuideData(
            "BNP / NT-proBNP",
            "Normal: < 100 pg/mL",
            "Peptide produced by the heart when it is under stress or working too hard.",
            listOf("Heart Failure", "Kidney Failure"),
            "Indicates heart failure or significant heart strain.",
            "Normal; indicates heart is likely not under significant strain."
        ),
        BiomarkerGuideData(
            "Testosterone",
            "Normal: 300 - 1000 (M) / 15 - 70 (F) ng/dL",
            "Main sex hormone in males, also present in females in smaller amounts.",
            listOf("Hypogonadism", "PCOS (in females)", "Adrenal issues"),
            "In females, can indicate PCOS. In males, usually not a medical concern unless very extreme.",
            "In males, can cause low libido, fatigue, and muscle loss."
        )
    )

    biomarkers.filter { it.title.contains(query, ignoreCase = true) }.forEach { biomarker ->
        BiomarkerGuideCard(biomarker)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun DiseaseGuideCard(data: DiseaseGuideData) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (expanded) androidx.compose.foundation.BorderStroke(2.dp, SplashBlue) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(data.icon, contentDescription = null, tint = SplashBlue, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(data.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(data.subtitle, fontSize = 12.sp, color = Color.Gray)
                }
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
                    
                    if (data.description != null) {
                        Text("\"${data.description}\"", fontStyle = FontStyle.Italic, color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    
                    Text("CONFIRMATORY BIOMARKERS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    data.biomarkers.forEach { (name, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("RISK FACTORS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    @OptIn(ExperimentalLayoutApi::class)
                    androidx.compose.foundation.layout.FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.riskFactors.forEach { factor ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(factor, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BiomarkerGuideCard(data: BiomarkerGuideData) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (expanded) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF38A169)) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF0FFF4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.List, contentDescription = null, tint = Color(0xFF38A169), modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(data.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(data.range, fontSize = 13.sp, color = Color.Gray)
                }
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.LightGray
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
                    
                    Text("\"${data.description}\"", fontStyle = FontStyle.Italic, color = Color.Gray, fontSize = 14.sp)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("ASSOCIATED DISEASES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    @OptIn(ExperimentalLayoutApi::class)
                    androidx.compose.foundation.layout.FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        data.diseases.forEach { disease ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF7ED), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFFFEDD5), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(disease, fontSize = 12.sp, color = Color(0xFF9A3412), fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // High Levels Warning
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF1F2), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("HIGH LEVELS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFFE11D48))
                            Text(data.highInfo, fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Low Levels Info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF0F9FF), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SplashBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("LOW LEVELS", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SplashBlue)
                            Text(data.lowInfo, fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        }
    }
}

data class DiseaseGuideData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val biomarkers: List<Pair<String, String>>,
    val riskFactors: List<String>,
    val description: String? = null
)

data class BiomarkerGuideData(
    val title: String,
    val range: String,
    val description: String,
    val diseases: List<String>,
    val highInfo: String,
    val lowInfo: String
)
