$ErrorActionPreference='Stop'
$rows=Import-Csv C:\ETL\Reports\windows-linux-class-comparison.csv
$rows | ForEach-Object -Parallel {
 $r=$_;$out="C:\ETL\$($r.Component)\recovered\windows-5.6.6.35\java\$([IO.Path]::GetFileNameWithoutExtension($r.Jar))"
 New-Item -ItemType Directory -Force $out | Out-Null
 $method='CFR'
 if($r.IdenticalClassBytes -eq 'True'){
  $source="C:\ETL\$($r.Component)\recovered\java\$([IO.Path]::GetFileNameWithoutExtension($r.Jar))"
  Copy-Item "$source\*" $out -Recurse -Force;$code=0;$method='Reused identical class bytes'
 }else{
  & 'C:\Program Files\Jitterbit Agent\jre\bin\java.exe' -Xmx768m -jar C:\ETL\Tools\cfr-0.152.jar $r.WindowsPath --outputdir $out --silent true 1> "$out\cfr-stdout.log" 2> "$out\cfr-stderr.log"
  $code=$LASTEXITCODE
 }
 [pscustomobject]@{Component=$r.Component;Jar=$r.Jar;Input=$r.WindowsPath;SHA256=(Get-FileHash $r.WindowsPath).Hash;Method=$method;ExitCode=$code;JavaFiles=(Get-ChildItem $out -Recurse -Filter *.java | Measure-Object).Count;Output=$out} | Export-Csv "$out\recovery.csv" -NoTypeInformation
 Write-Output "$($r.Component) $($r.Jar): $code"
} -ThrottleLimit 4
Get-ChildItem C:\ETL\Server\recovered\windows-5.6.6.35,C:\ETL\Studio\recovered\windows-5.6.6.35 -Recurse -Filter recovery.csv | ForEach-Object {Import-Csv $_.FullName} | Export-Csv C:\ETL\Reports\windows-decompilation.csv -NoTypeInformation
