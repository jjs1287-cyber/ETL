param([string]$InstanceHome='C:\ETL\Server\instances\dev')
$ErrorActionPreference='Stop'
$configuration=Join-Path $InstanceHome 'etlforge.conf'
if(!(Test-Path -LiteralPath $configuration -PathType Leaf)){throw 'Server etlforge.conf was not found'}
$text=[IO.File]::ReadAllText($configuration,[Text.Encoding]::UTF8)
if($text -match '(?ms)^\[Cluster\]\s*\r?\n(?<section>.*?)(?=^\[|\z)'){
 $section=$Matches.section
 if($section -match '(?m)^NodeId\s*=\s*(\S+)\s*$'){
  $node=[Guid]::Parse($Matches[1]);Write-Output "Existing node identity: $node";exit 0
 }
 throw 'Cluster section exists without NodeId; correct it before continuing'
}
$node=[Guid]::NewGuid()
$archive=Join-Path 'C:\ETL\Archives' ('cluster-node-'+(Get-Date -Format 'yyyyMMdd-HHmmss'))
New-Item -ItemType Directory -Path $archive | Out-Null
Copy-Item -LiteralPath $configuration -Destination (Join-Path $archive 'etlforge.conf')
$text+="`r`n[Cluster]`r`nNodeId=$node`r`n"
$temporary=$configuration+'.cluster.tmp'
[IO.File]::WriteAllText($temporary,$text,(New-Object Text.UTF8Encoding($false)))
Move-Item -LiteralPath $temporary -Destination $configuration -Force
Write-Output "Created unique node identity: $node. Restart Scheduler to register it."
