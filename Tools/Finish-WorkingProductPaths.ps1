$ErrorActionPreference='Stop'
$trees=@('C:\ETL\Studio\src','C:\ETL\Studio\metadata','C:\ETL\Studio\configuration','C:\ETL\Studio\.settings','C:\ETL\Server\src','C:\ETL\Server\tests','C:\ETL\Server\cmake')
foreach($tree in $trees){foreach($item in (Get-ChildItem -LiteralPath $tree -Recurse | Sort-Object { $_.FullName.Length } -Descending)){
 $new=$item.Name.Replace('JITTERBIT','ETLFORGE').Replace('Jitterbit','ETLForge').Replace('jitterbit','etlforge')
 if($new -cne $item.Name){
  $parent=if($item.PSIsContainer){$item.Parent.FullName}else{$item.DirectoryName}
  $target=Join-Path $parent $new
  if(Test-Path -LiteralPath $target){throw "Rename collision: $target"}
  Rename-Item -LiteralPath $item.FullName -NewName $new
 }
}}
