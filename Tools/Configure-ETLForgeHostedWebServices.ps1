param([string]$Instance='C:\ETL\Server\instances\dev')
$ErrorActionPreference='Stop'
$configuration=Join-Path $Instance 'apache\conf\httpd.conf'
$text=[IO.File]::ReadAllText($configuration)
if($text -notmatch 'LoadModule\s+etlforge_hosted_module') {
 $text += @'

LoadModule etlforge_hosted_module "C:/ETL/Server/dist/bin/mod_etlforge_hosted.so"
<Location /etlforge/soap>
 SetHandler konga_ws
 AcceptPathInfo On
 Require local
</Location>
'@ + "`r`n"
 [IO.File]::WriteAllText($configuration,$text,[Text.UTF8Encoding]::new($false))
}
& 'C:\ETL\Server\runtime\apache-httpd-2.4.69\Apache24\bin\httpd.exe' -t -f $configuration
if($LASTEXITCODE -ne 0){throw 'Apache configuration validation failed'}
