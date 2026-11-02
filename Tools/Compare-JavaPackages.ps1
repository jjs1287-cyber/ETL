$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
function Class-Digest($path){
 $zip=[IO.Compression.ZipFile]::OpenRead($path);$sha=[Security.Cryptography.SHA256]::Create();$builder=[Text.StringBuilder]::new()
 try{foreach($entry in ($zip.Entries | Where-Object {$_.FullName.EndsWith('.class')} | Sort-Object FullName)){$s=$entry.Open();try{$hash=[BitConverter]::ToString($sha.ComputeHash($s));$null=$builder.AppendLine($entry.FullName+':'+$hash)}finally{$s.Dispose()}};return [BitConverter]::ToString($sha.ComputeHash([Text.Encoding]::UTF8.GetBytes($builder.ToString())))}finally{$zip.Dispose();$sha.Dispose()}
}
$results=foreach($component in @('Server','Studio')){
 $base="C:\ETL\Reference\v5\$($component.ToLower())"
 $old=@{};Get-ChildItem "$base-linux" -Recurse -Filter 'jitterbit*.jar' | ForEach-Object {$old[$_.Name]=$_.FullName}
 foreach($jar in (Get-ChildItem "$base-windows" -Recurse -Filter 'jitterbit*.jar')){
 $same=$false;if($old.ContainsKey($jar.Name)){$same=(Class-Digest $jar.FullName) -eq (Class-Digest $old[$jar.Name])}
 [pscustomobject]@{Component=$component;Jar=$jar.Name;WindowsPath=$jar.FullName;LinuxPath=$old[$jar.Name];IdenticalClassBytes=$same}
 }
}
$results | Export-Csv C:\ETL\Reports\windows-linux-class-comparison.csv -NoTypeInformation
$results | Group-Object Component,IdenticalClassBytes | Select-Object Name,Count
