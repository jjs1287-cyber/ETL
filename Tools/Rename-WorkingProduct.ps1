$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$trees=@('C:\ETL\Studio\src','C:\ETL\Studio\metadata','C:\ETL\Studio\configuration','C:\ETL\Studio\.settings','C:\ETL\Server\src','C:\ETL\Server\tests','C:\ETL\Server\cmake')
$files=@('C:\ETL\Studio\build.xml','C:\ETL\Studio\.classpath','C:\ETL\Studio\.project','C:\ETL\Studio\ETLForge Studio.launch','C:\ETL\Studio\Launch-Studio.cmd','C:\ETL\Server\CMakeLists.txt','C:\ETL\Tools\Test-ServerJava.ps1')
$snapshot='C:\ETL\Reports\before-product-rename.zip'
if(Test-Path $snapshot){throw 'Rename already started; do not run twice.'}
$all=@(foreach($tree in $trees){Get-ChildItem -LiteralPath $tree -Recurse -File})+@(foreach($file in $files){Get-Item -LiteralPath $file})
$zip=[IO.Compression.ZipFile]::Open($snapshot,[IO.Compression.ZipArchiveMode]::Create)
try{foreach($file in $all){[IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip,$file.FullName,$file.FullName.Substring(7).Replace('\','/'),[IO.Compression.CompressionLevel]::Optimal)|Out-Null}}finally{$zip.Dispose()}
function Rename-Text([string]$text){
 $text=$text.Replace('JITTERBIT','ETLFORGE').Replace('Jitterbit','ETLForge').Replace('jitterbit','etlforge')
 # No invented public product domain. These are reserved namespace/help identifiers.
 return $text.Replace('etlforge.com','etlforge.invalid').Replace('etlforge.org','etlforge.invalid')
}
$utf8=[Text.UTF8Encoding]::new($false)
$extensions=@('.java','.xml','.properties','.csv','.includes','.txt','.html','.htm','.css','.js','.json','.xsd','.wsdl','.sql','.cpp','.c','.h','.hpp','.inl','.rc','.idl','.def','.cmake','.cmd','.ps1','.launch','.settings','.project','.classpath','.prefs','.ini','.conf','.config','.bat','.sh','.template','.vm','.dtd','.xsl','.xslt')
$changed=0
foreach($file in $all){
 if($file.Extension -notin $extensions -and $file.Name -notin @('.project','.classpath','CMakeLists.txt')){continue}
 $text=[IO.File]::ReadAllText($file.FullName)
 # Keep upstream legal notices and original archive provenance fields unchanged.
 $lines=[regex]::Split($text,'(?<=\n)')
 $updated=($lines|ForEach-Object {if($_ -match '(?i)copyright|OriginalJar|C:[\\/]ETL[\\/]Reference'){$_}else{Rename-Text $_}}) -join ''
 # Source catalog embeds original provenance and active paths in the same CSV row.
 if($file.Name -eq 'source-modules.csv'){
  $rows=Import-Csv $file.FullName
  foreach($row in $rows){$row.Module=Rename-Text $row.Module;$row.Source=Rename-Text $row.Source}
  $rows|Export-Csv $file.FullName -NoTypeInformation -Encoding utf8
 }elseif($updated -cne $text){[IO.File]::WriteAllText($file.FullName,$updated,$utf8);$changed++}
}
foreach($tree in $trees){
 # Rename leaves before parents so every move is local and collision checked.
 foreach($item in (Get-ChildItem -LiteralPath $tree -Recurse | Sort-Object { $_.FullName.Length } -Descending)){
  $new=Rename-Text $item.Name
  if($new -cne $item.Name){
   $parent=if($item.PSIsContainer){$item.Parent.FullName}else{$item.DirectoryName}
   $target=Join-Path $parent $new
   if(Test-Path -LiteralPath $target){throw "Rename collision: $target"}
   Rename-Item -LiteralPath $item.FullName -NewName $new
  }
 }
}
"Renamed working product: $changed text files; snapshot $snapshot"
