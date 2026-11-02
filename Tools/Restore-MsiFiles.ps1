param([string]$Msi,[string]$CabFiles,[string]$Output)
$ErrorActionPreference='Stop'
$installer=New-Object -ComObject WindowsInstaller.Installer
$db=$installer.OpenDatabase($Msi,0)
function Read-Table($query,$columns){
 $v=$db.OpenView($query);$null=$v.Execute();$rows=@();while($r=$v.Fetch()){$row=@{};for($i=1;$i -le $columns.Count;$i++){$row[$columns[$i-1]]=$r.StringData($i)};$rows+=,$row};$null=$v.Close();return $rows
}
$dirs=@{};foreach($r in (Read-Table 'SELECT `Directory`, `Directory_Parent`, `DefaultDir` FROM `Directory`' @('Id','Parent','Name'))){$dirs[$r.Id]=$r}
$comps=@{};foreach($r in (Read-Table 'SELECT `Component`, `Directory_` FROM `Component`' @('Id','Dir'))){$comps[$r.Id]=$r.Dir}
function Relative-Dir($id){if(!$id -or !$dirs.ContainsKey($id)){return ''};$d=$dirs[$id];$name=(($d.Name -split ':')[0] -split '\|')[-1];if($name -eq '.' -or $id -eq 'TARGETDIR'){$name=''};return ((Relative-Dir $d.Parent)+'\'+$name).Trim('\')}
$manifest=foreach($r in (Read-Table 'SELECT `File`, `Component_`, `FileName` FROM `File`' @('Id','Component','Name'))){
 $name=($r.Name -split '\|')[-1];$relative=(Relative-Dir $comps[$r.Component])+'\'+$name
 $dest=[IO.Path]::GetFullPath((Join-Path $Output $relative.TrimStart('\')))
 if(!$dest.StartsWith([IO.Path]::GetFullPath($Output)+'\',[StringComparison]::OrdinalIgnoreCase)){throw 'Unsafe MSI path'}
 $inputFile=Join-Path $CabFiles $r.Id
 if(Test-Path -LiteralPath $inputFile){New-Item -ItemType Directory -Force (Split-Path $dest) | Out-Null; Copy-Item -LiteralPath $inputFile -Destination $dest}
 [pscustomobject]@{Id=$r.Id;File=$name;RelativePath=$relative;Found=(Test-Path -LiteralPath $inputFile);Output=$dest}
}
$manifest | Export-Csv ($Output+'-manifest.csv') -NoTypeInformation
$manifest | Group-Object Found | Select-Object Name,Count


