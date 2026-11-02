param([string]$Log='C:\ETL\Studio\build\ant-compile.log')
$lines=Get-Content $Log
$errors=for($i=0;$i -lt $lines.Count;$i++){
 if($lines[$i] -match '^\s*\[javac\] (C:\\ETL\\Studio\\src\\main\\java\\(.+\.java)):(\d+): error: (.*)$'){
  [pscustomobject]@{Path=$matches[1];File=$matches[2];Line=[int]$matches[3];Error=$matches[4];Source=($lines[$i+1] -replace '^\s*\[javac\] ','');Caret=($lines[$i+2] -replace '^\s*\[javac\] ','');Details=(($lines | Select-Object -Skip ($i+3) -First 5) -join "`n")}
 }
}
$errors | Export-Csv C:\ETL\Reports\studio-compile-errors.csv -NoTypeInformation
$errors | ConvertTo-Json -Depth 4 | Set-Content C:\ETL\Reports\studio-compile-errors.json
$errors | Select-Object File,Line,Error,Source | Format-List
