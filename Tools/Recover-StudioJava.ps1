$ErrorActionPreference='Stop'
$out='C:\ETL\Studio\recovered\vineflower-windows-5.6.6.35'
New-Item -ItemType Directory -Force $out | Out-Null
$libs=Get-ChildItem C:\ETL\Reference\v5\studio-windows -Recurse -Filter *.jar
$inputs=Import-Csv C:\ETL\Reports\windows-decompilation.csv | Where-Object Component -eq Studio | ForEach-Object Input
$argsList=@('--folder','--log-level=warn','--ascii-strings=true','--thread-count=3','--include-runtime=0','-e=C:\ETL\Tools\jdk8u504-b01\jre\lib\rt.jar','-e=C:\ETL\Dependencies\java\jsr305-3.0.2.jar')+@($libs | ForEach-Object {'-e='+$_.FullName})+@($inputs)+@($out)
Set-Content C:\ETL\Reports\studio-vineflower-recovery.log ''
& 'C:\Program Files\Jitterbit Agent\jre\bin\java.exe' -Xmx4g -jar C:\ETL\Tools\vineflower-1.12.0.jar @argsList 2>&1 | Tee-Object C:\ETL\Reports\studio-vineflower-recovery.log
$code=$LASTEXITCODE
Set-Content C:\ETL\Reports\studio-vineflower-exit.txt $code
if($code -ne 0){throw "Studio recovery failed: $code"}
