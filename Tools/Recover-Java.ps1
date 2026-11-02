$ErrorActionPreference='Stop'
$java='C:\Program Files\Jitterbit Agent\jre\bin\java.exe'
$roots=@{Studio='C:\ETL\Reference\v5\studio-linux';Server='C:\ETL\Reference\v5\server-linux'}
$results=@()
foreach($component in @('Server','Studio')) {
 $jars=Get-ChildItem $roots[$component] -Recurse -Filter 'jitterbit*.jar'
 foreach($jar in $jars) {
  $out="C:\ETL\$component\recovered\java\$($jar.BaseName)"
  New-Item -ItemType Directory -Force $out | Out-Null
  & $java -Xmx2g -jar C:\ETL\Tools\cfr-0.152.jar $jar.FullName --outputdir $out --silent true 1> "$out\cfr-stdout.log" 2> "$out\cfr-stderr.log"
  $results += [pscustomobject]@{Component=$component;Input=$jar.FullName;SHA256=(Get-FileHash $jar.FullName -Algorithm SHA256).Hash;ExitCode=$LASTEXITCODE;JavaFiles=(Get-ChildItem $out -Recurse -Filter *.java | Measure-Object).Count;Output=$out}
  $results | Export-Csv C:\ETL\Reports\decompilation.csv -NoTypeInformation
  Write-Output "$component $($jar.Name): $LASTEXITCODE"
 }
}
