param(
    [Parameter(Mandatory=$true)][string]$Stylesheet,
    [Parameter(Mandatory=$true)][string]$InputFile,
    [Parameter(Mandatory=$true)][string]$OutputFile
)
$ErrorActionPreference='Stop'
$cp='C:\ETL\Server\build-probe\xslt\etlforge-xslt.jar;C:\ETL\Dependencies\java\xslt\*'
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -cp $cp org.etlforge.integration.xslt.EtlForgeXsltRuntime $Stylesheet $InputFile $OutputFile
if($LASTEXITCODE -ne 0){throw "XSLT execution failed ($LASTEXITCODE)"}
