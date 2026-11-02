$ErrorActionPreference='Stop'
$raw='C:\ETL\Server\recovered\windows-5.6.6.35\java'
$vf='C:\ETL\Server\recovered\vineflower-server'
$dest='C:\ETL\Server\src\java\modules'
$provenance=foreach($module in (Get-ChildItem $raw -Directory)){
 foreach($file in (Get-ChildItem $module.FullName -Recurse -Filter *.java)){
  $relative=$file.FullName.Substring($module.FullName.Length+1)
  $from=Join-Path $vf $relative
  if(!(Test-Path -LiteralPath $from)){throw "Missing recovered file $relative"}
  $to=Join-Path (Join-Path $dest $module.Name) $relative
  New-Item -ItemType Directory -Force (Split-Path $to) | Out-Null
  Copy-Item -LiteralPath $from -Destination $to -Force
  [pscustomobject]@{Module=$module.Name;File=$relative;Recovery='Vineflower 1.12.0';RecoveredSource=$from;WorkingSource=$to}
 }
 foreach($name in @('recovery.csv','summary.txt','cfr-stdout.log','cfr-stderr.log')){
  $metadata=Join-Path (Join-Path $dest $module.Name) $name
  if(Test-Path -LiteralPath $metadata){Remove-Item -LiteralPath $metadata}
 }
}
# Preserve the four manually repaired, behavior-tested language utility sources.
foreach($file in @('org\jitterbit\lang\Arrays2.java','org\jitterbit\lang\KongaClassUtils.java','org\jitterbit\lang\StringConverter.java','org\jitterbit\system\EnumWithIdLookup.java')){
 Copy-Item -LiteralPath (Join-Path 'C:\ETL\Server\src\java\lang-utils' $file) -Destination (Join-Path "$dest\jitterbit-lang-utils" $file) -Force
 ($provenance | Where-Object {$_.Module -eq 'jitterbit-lang-utils' -and $_.File -eq $file}).Recovery='CFR 0.152 plus tested generic type repairs'
 ($provenance | Where-Object {$_.Module -eq 'jitterbit-lang-utils' -and $_.File -eq $file}).RecoveredSource=Join-Path 'C:\ETL\Server\src\java\lang-utils' $file
}
$provenance | Export-Csv C:\ETL\Reports\server-java-source-provenance.csv -NoTypeInformation

