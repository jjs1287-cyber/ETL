$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$root='C:\ETL\Server\src\java\resources'
if(Test-Path $root){throw 'Resources already staged; edit the working resources instead of overwriting them.'}
foreach($module in (Import-Csv C:\ETL\Reports\windows-decompilation.csv | Where-Object Component -eq Server)){
 $name=[IO.Path]::GetFileNameWithoutExtension($module.Jar)
 $directory=Join-Path $root $name
 New-Item -ItemType Directory -Path $directory -Force|Out-Null
 $archive=[IO.Compression.ZipFile]::OpenRead($module.Input)
 try{foreach($entry in $archive.Entries){
  if($entry.FullName.EndsWith('/') -or $entry.FullName.EndsWith('.class') -or $entry.FullName -match '(?i)^META-INF/(MANIFEST.MF|.*\.(SF|RSA|DSA))$'){continue}
  $dest=[IO.Path]::GetFullPath((Join-Path $directory $entry.FullName))
  if(!$dest.StartsWith($directory+'\',[StringComparison]::OrdinalIgnoreCase)){throw 'Unsafe archive entry'}
  New-Item -ItemType Directory -Force (Split-Path $dest)|Out-Null
  [IO.Compression.ZipFileExtensions]::ExtractToFile($entry,$dest)
 }}finally{$archive.Dispose()}
}
