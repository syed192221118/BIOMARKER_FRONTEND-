$base_url = "http://127.0.0.1:8000/api"
$user = "testuser" + (Get-Random)
$pass = "password123"

Write-Host "Registering $user..."
try {
    $reg = Invoke-RestMethod -Uri "$base_url/register/" -Method Post -Body @{username=$user; password=$pass; email="$user@example.com"}
    Write-Host "Registered: $($reg.username)"
} catch {
    Write-Host "Registration failed: $_"
    # Continue if user already exists (random collision unlikely but possible)
}

Write-Host "Logging in..."
try {
    $token_res = Invoke-RestMethod -Uri "$base_url/login/" -Method Post -Body @{username=$user; password=$pass}
    $token = $token_res.access
    Write-Host "Got Token"
} catch {
    Write-Host "Login failed: $_"
    exit
}

Write-Host "Getting Biomarkers..."
try {
    $biomarkers = Invoke-RestMethod -Uri "$base_url/biomarkers/" -Method Get
    if ($biomarkers.Count -gt 0) {
        $b_id = $biomarkers[0].id
        Write-Host "First Biomarker ID: $b_id Name: $($biomarkers[0].symbol)"
    } else {
        Write-Host "No biomarkers found!"
        exit
    }
} catch {
    Write-Host "Get Biomarkers failed: $_"
    exit
}

Write-Host "Analyzing..."
$headers = @{Authorization="Bearer $token"}
$body = @{
    readings_input = @(
        @{biomarker_id=$b_id; value=150}
    )
} | ConvertTo-Json -Depth 3

try {
    $analysis = Invoke-RestMethod -Uri "$base_url/process/" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    Write-Host "Analysis Result: Score=$($analysis.metabolic_score), Risk=$($analysis.risk_level)"
    Write-Host "Insights: $($analysis.insights)"
} catch {
    Write-Host "Analysis failed: $_"
    try {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $reader.BaseStream.Position = 0
        $reader.ReadToEnd()
    } catch {}
}
