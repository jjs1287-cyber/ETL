$ErrorActionPreference='Stop'
. "$PSScriptRoot\DevEnvironment.ps1"
Add-Type -AssemblyName System.IO.Compression.FileSystem
$root="${script:ETLRoot}\Server";$classes="$root\build-probe\etlforge-server-java\classes"
if((Get-Content "$root\build-probe\etlforge-server-java\exit-code.txt") -ne '0'){throw 'Compile server successfully before packaging'}
New-Item -ItemType Directory -Force "$root\dist\lib"|Out-Null
$moduleNames=@(Get-ChildItem "$root\src\java\modules" -Directory | ForEach-Object Name)
$removedConnectorModules=@('etlforge-server-connector-engine','etlforge-integration-common-mscrm','etlforge-integration-common-connector')
$moduleNames=@($moduleNames|Where-Object {$_ -notin $removedConnectorModules})
foreach($name in $removedConnectorModules){
 $stale=Join-Path "$root\dist\lib" ($name+'.jar')
 if(Test-Path -LiteralPath $stale){Remove-Item -LiteralPath $stale}
}
$report=foreach($name in $moduleNames){
 $dest="$root\dist\lib\$name.jar"
 if(Test-Path -LiteralPath $dest){Remove-Item -LiteralPath $dest}
 $output=[IO.Compression.ZipFile]::Open($dest,[IO.Compression.ZipArchiveMode]::Create)
 $count=0;$resources=0
 try{
  $manifest=$output.CreateEntry('META-INF/MANIFEST.MF');$writer=[IO.StreamWriter]::new($manifest.Open());try{$writer.Write("Manifest-Version: 1.0`r`nImplementation-Title: ETLForge Server rebuilt module`r`nImplementation-Version: 42.0.0.0`r`n`r`n")}finally{$writer.Dispose()}
  $resourceRoot="$root\src\java\resources\$name"
  # Modules without non-code resources are valid (for example the XSLT bridge).
  foreach($file in Get-ChildItem -LiteralPath $resourceRoot -File -Recurse -ErrorAction SilentlyContinue){$entryName=$file.FullName.Substring($resourceRoot.Length+1).Replace('\','/');[IO.Compression.ZipFileExtensions]::CreateEntryFromFile($output,$file.FullName,$entryName)|Out-Null;$resources++}
  foreach($file in Get-ChildItem "$root\src\java\modules\$name" -Recurse -Filter *.java){
   $relative=$file.FullName.Substring(("$root\src\java\modules\$name\").Length);$stem=$relative -replace '\.java$','';$class=Join-Path $classes ($stem+'.class');if(!(Test-Path -LiteralPath $class)){throw "Missing rebuilt class $class"}
   $items=@(Get-Item -LiteralPath $class)+@(Get-ChildItem (Split-Path $class) -Filter ((Split-Path $stem -Leaf)+'$*.class'))
   foreach($item in $items){$entryName=$item.FullName.Substring($classes.Length+1).Replace('\','/');[IO.Compression.ZipFileExtensions]::CreateEntryFromFile($output,$item.FullName,$entryName)|Out-Null;$count++}
  }
 }finally{$output.Dispose()}
 [pscustomobject]@{Module=$name;Classes=$count;Resources=$resources;Jar=$dest;SHA256=(Get-FileHash $dest -Algorithm SHA256).Hash}
}
$report|Export-Csv "${script:ETLRoot}\Reports\server-java-packaged-modules.csv" -NoTypeInformation
"Packaged $(@($report).Count) rebuilt server modules"
