$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$report=foreach($component in @('Studio','Server')){
 $classes=0;$old=@();$gettingStarted=@();$jars=@(Get-ChildItem "C:\ETL\$component\dist\lib" -File -Filter *.jar)
 foreach($jar in $jars){
  if($jar.Name -match '(?i)jitterbit'){throw "Old module name: $($jar.Name)"}
  $archive=[IO.Compression.ZipFile]::OpenRead($jar.FullName)
  try{foreach($entry in $archive.Entries){
   if($entry.FullName.EndsWith('.class')){$classes++}
   if($entry.FullName -match '(?i)jitterbit'){$old+=$entry.FullName}
   if($component -eq 'Studio' -and $entry.FullName -match '(GettingStartedPage|OpenGettingStartedPageAction|ShowGettingStartedPagePreference)(\$.*)?\.class$'){$gettingStarted+=$entry.FullName}
  }}finally{$archive.Dispose()}
 }
 if($old.Count){throw "Old package/resource paths in $component : $($old -join ', ')"}
 if($gettingStarted.Count){throw "Excluded Getting Started classes in Studio: $($gettingStarted -join ', ')"}
 [pscustomobject]@{Component=$component;Modules=$jars.Count;Classes=$classes;OldPackagePaths=$old.Count;GettingStartedClasses=$gettingStarted.Count;Result='PASS'}
}
$report|Export-Csv C:\ETL\Reports\rebranded-package-audit.csv -NoTypeInformation
$report|Format-Table
