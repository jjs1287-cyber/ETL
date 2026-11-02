$ErrorActionPreference='Stop'
if(Test-Path -LiteralPath 'C:\ETL\Studio\.project'){throw 'Studio is already staged. Use Build-Studio.ps1; recovery initialization would overwrite edited configuration and module metadata.'}
$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$root='C:\ETL\Studio'
$java=Join-Path $root 'src\main\java'
New-Item -ItemType Directory -Force $java,"$root\lib","$root\metadata\includes","$root\src\main\resources","$root\.settings","$root\configuration","$root\build" | Out-Null
if(!(Get-ChildItem $java -Recurse -Filter *.java | Select-Object -First 1)){Copy-Item "$root\recovered\vineflower-windows-5.6.6.35\*" $java -Recurse}
$modules=Import-Csv C:\ETL\Reports\windows-decompilation.csv | Where-Object Component -eq Studio
$catalog=@()
foreach($module in $modules){
 $name=[IO.Path]::GetFileNameWithoutExtension($module.Jar)
 $resources="$root\src\main\resources\$name"
 New-Item -ItemType Directory -Force $resources | Out-Null
 $z=[IO.Compression.ZipFile]::OpenRead($module.Input)
 try {
  foreach($e in $z.Entries){
   if($e.FullName.EndsWith('/') -or $e.FullName.EndsWith('.class') -or $e.FullName -match '^META-INF/(MANIFEST.MF|[^/]+\.(SF|RSA|DSA|EC)|SIG-[^/]+)$'){continue}
   $to=[IO.Path]::GetFullPath((Join-Path $resources $e.FullName))
   if(!$to.StartsWith($resources+'\',[StringComparison]::OrdinalIgnoreCase)){throw 'Unsafe resource path'}
   New-Item -ItemType Directory -Force (Split-Path $to) | Out-Null
   $inputStream=$e.Open();$outputStream=[IO.File]::Create($to);try{$inputStream.CopyTo($outputStream)}finally{$inputStream.Dispose();$outputStream.Dispose()}
  }
 }finally{$z.Dispose()}
 $raw="$root\recovered\windows-5.6.6.35\java\$name"
 $includes=foreach($file in (Get-ChildItem $raw -Recurse -Filter *.java)){
  $relative=$file.FullName.Substring($raw.Length+1).Replace('\','/')
  $catalog += [pscustomobject]@{Module=$name;Source=$relative;OriginalJar=$module.Input}
  $stem=$relative -replace '\.java$',''
  $stem+'.class';$stem+'$*.class'
 }
 $includes | Set-Content "$root\metadata\includes\$name.includes"
}
$catalog | Export-Csv "$root\metadata\source-modules.csv" -NoTypeInformation
$libRoot='C:\ETL\Reference\v5\studio-windows\Jitterbit Studio 5.6\lib'
Get-ChildItem $libRoot -Filter *.jar | Where-Object {$_.Name -notlike 'jitterbit*' -and $_.Name -notlike '*sqlite*'} | Copy-Item -Destination "$root\lib" -Force
Copy-Item C:\ETL\Dependencies\java\jsr305-3.0.2.jar "$root\lib" -Force
Copy-Item 'C:\ETL\Reference\v5\studio-windows\Jitterbit Studio 5.6\configuration\*' "$root\configuration" -Force
'JRE_HOME=C:/ETL/Tools/jdk8u504-b01' | Add-Content "$root\configuration\client.properties"
'PORT_NUMBER=37733' | Add-Content "$root\configuration\client.properties"
$catalog.Count
