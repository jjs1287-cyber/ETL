$ErrorActionPreference='Stop'
$java='C:\ETL\Tools\jdk8u504-b01\bin'
$classpath='C:\ETL\Server\build-probe;C:\ETL\Dependencies\java\postgresql-42.7.14.jar;C:\ETL\Studio\configuration;C:\ETL\Studio\dist\lib\*;C:\ETL\Studio\lib\*'
Push-Location 'C:\ETL\Studio'
try {
    & (Join-Path $java 'java.exe') '-Dfile.encoding=UTF-8' -cp $classpath DeployRegressionProject run
    if($LASTEXITCODE -ne 0){throw 'Regression suite failed. See operation logs and C:\ETL\Samples\RegressionSuite\results\latest.csv.'}
    $rows=@(Import-Csv -LiteralPath 'C:\ETL\Samples\RegressionSuite\results\latest.csv')
    $expected=(Get-Content -LiteralPath 'C:\ETL\Reports\regression-suite\manifest.json' -Raw | ConvertFrom-Json).cases.Count
    if($rows.Count -ne $expected -or @($rows | Where-Object {$_.Result -ne 'PASS'}).Count -gt 0){throw 'Regression results are incomplete or contain failures.'}
    Write-Output "PASS: all $expected regression cases. Results: C:\ETL\Samples\RegressionSuite\results\latest.csv"
} finally { Pop-Location }
