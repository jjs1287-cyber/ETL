param([string]$Instance='C:\ETL\Server\instances\dev')
$ErrorActionPreference='Stop'
$mapping='C:\ETL\Server\src\resources\TransformationEngine\DataTypeMappings.conf'
if(!(Test-Path -LiteralPath $mapping)){throw 'Missing editable transformation data-type mapping'}
$errors='C:\ETL\Server\src\native\integration\konga\TransformationEngine\ErrorCodes'
foreach($root in @('C:\ETL\Server\dist',$Instance)){
 $schemaDirectory=Join-Path $root 'DataInterchange\xml_dtd\Schemas'
 New-Item -ItemType Directory -Path $schemaDirectory -Force|Out-Null
 Get-ChildItem -LiteralPath 'C:\ETL\Server\src\resources\Schemas' -File | ForEach-Object {Copy-Item -LiteralPath $_.FullName -Destination $schemaDirectory -Force}
 $directory=Join-Path $root 'Resources\TransformationEngine'
 New-Item -ItemType Directory -Path $directory -Force|Out-Null
 $destination=Join-Path $directory 'DataTypeMappings.conf'
 if(!(Test-Path -LiteralPath $destination)){Copy-Item -LiteralPath $mapping -Destination $destination}
 $errorDirectory=Join-Path $directory 'ErrorCodes'
 New-Item -ItemType Directory -Path $errorDirectory -Force|Out-Null
 foreach($name in @('ErrorCodes.rc','resource.h')){
  $source=Join-Path $errors $name
  if(!(Test-Path -LiteralPath $source)){throw "Missing transformation error resource: $name"}
  Copy-Item -LiteralPath $source -Destination (Join-Path $errorDirectory $name) -Force
 }
}
$configuration=Join-Path $Instance 'etlforge.conf'
if(Test-Path -LiteralPath $configuration){
 $text=[IO.File]::ReadAllText($configuration)
 if($text -notmatch '(?m)^OperationEngineFileName\s*='){
  if(!$text.Contains('[ProcessEngine]')){throw 'Missing process-engine configuration section'}
  $text=$text.Replace('[ProcessEngine]',"[ProcessEngine]`r`nOperationEngineFileName = C:/ETL/Server/dist/bin/ETLForgeOperationEngineProc.exe")
  [IO.File]::WriteAllText($configuration,$text,[Text.UTF8Encoding]::new($false))
 }
}
'Native worker path and transformation resources prepared.'

