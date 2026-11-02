$ErrorActionPreference='Stop'
. "$PSScriptRoot\DevEnvironment.ps1"
$java="${script:ETLRoot}\Tools\jdk8u504-b01\bin"
$classpath="${script:ETLRoot}\Server\build-probe;${script:ETLRoot}\Dependencies\java\postgresql-42.7.14.jar;${script:ETLRoot}\Studio\configuration;${script:ETLRoot}\Studio\dist\lib\*;${script:ETLRoot}\Studio\lib\*"
Push-Location "${script:ETLRoot}\Studio"
try {
    & (Join-Path $java 'java.exe') '-Dfile.encoding=UTF-8' -cp $classpath DeployRegressionProject run
    if($LASTEXITCODE -ne 0){throw "Regression suite failed. See operation logs and ${script:ETLRoot}\Samples\RegressionSuite\results\latest.csv."}
    $rows=@(Import-Csv -LiteralPath "${script:ETLRoot}\Samples\RegressionSuite\results\latest.csv")
    $expected=(Get-Content -LiteralPath "${script:ETLRoot}\Reports\regression-suite\manifest.json" -Raw | ConvertFrom-Json).cases.Count
    if($rows.Count -ne $expected -or @($rows | Where-Object {$_.Result -ne 'PASS'}).Count -gt 0){throw 'Regression results are incomplete or contain failures.'}
    Write-Output "PASS: all $expected regression cases. Results: ${script:ETLRoot}\Samples\RegressionSuite\results\latest.csv"
} finally { Pop-Location }
