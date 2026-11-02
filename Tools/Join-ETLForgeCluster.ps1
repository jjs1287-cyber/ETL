param(
    [Parameter(Mandatory=$true)][string]$ExistingServer,
    [string]$InstanceHome='C:\ETL\Server\instances\dev',
    [pscredential]$Credential,
    [switch]$AcceptTrustedNetworkBootstrapRisk
)
$ErrorActionPreference='Stop'
if(!$AcceptTrustedNetworkBootstrapRisk){
    throw 'Cluster join was not authorized. Re-run with -AcceptTrustedNetworkBootstrapRisk after accepting that the existing server will transmit its database credentials and vault master key over the configured HTTP(S) connection.'
}
if(!$Credential){$Credential=Get-Credential -Message 'Existing ETLForge administrator account'}
$origin=$ExistingServer.TrimEnd('/')
if($origin -notmatch '^https?://[^/]+$'){throw 'ExistingServer must be an HTTP(S) origin such as http://192.168.99.11:47100'}
$configuration=Join-Path $InstanceHome 'etlforge.conf'
if(!(Test-Path -LiteralPath $configuration -PathType Leaf)){throw "Server configuration was not found: $configuration"}

$plain=$Credential.GetNetworkCredential().Password
$md5=[Security.Cryptography.MD5]::Create()
try{$passwordHash=([BitConverter]::ToString($md5.ComputeHash([Text.Encoding]::UTF8.GetBytes($plain)))).Replace('-','').ToLowerInvariant()}finally{$md5.Dispose();$plain=$null}
$payload='{"action":"cluster.bootstrap","acceptTrustedNetworkBootstrapRisk":true}'
function Escape-Xml([string]$value){[Security.SecurityElement]::Escape($value)}
$body='<?xml version="1.0" encoding="UTF-8"?>'+
 '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:adm="urn:org.etlforge.integration.server.implementation.webservice.admin.Admin">'+
 '<soapenv:Body><adm:auditRequest><in0>'+(Escape-Xml $Credential.UserName)+'</in0><in1>'+(Escape-Xml $passwordHash)+'</in1><in2>'+(Escape-Xml $payload)+'</in2></adm:auditRequest></soapenv:Body></soapenv:Envelope>'
$response=Invoke-WebRequest -UseBasicParsing -Uri "$origin/axis/konga_admin" -Method Post -ContentType 'text/xml; charset=utf-8' -Headers @{SOAPAction='""'} -Body ([Text.Encoding]::UTF8.GetBytes($body)) -TimeoutSec 60
[xml]$soap=$response.Content
$fault=$soap.SelectSingleNode('//*[local-name()="Fault"]')
if($fault){throw ('Existing server rejected cluster join: '+$fault.InnerText)}
$value=$soap.SelectSingleNode('//*[local-name()="auditRequestReturn"]')
if(!$value){throw 'Existing server returned no cluster bootstrap package'}
$package=$value.InnerText|ConvertFrom-Json
if($package.format -ne 1 -or [string]::IsNullOrWhiteSpace($package.clusterId) -or ([string]$package.keyVaultMasterKey).Length -lt 32){throw 'Existing server returned an invalid cluster bootstrap package'}
$serverUri=[Uri]$origin

$text=[IO.File]::ReadAllText($configuration,[Text.Encoding]::UTF8)
$nodeMatch=[regex]::Match($text,'(?m)^NodeId\s*=\s*([0-9a-fA-F-]{36})\s*$')
$nodeId=if($nodeMatch.Success){[Guid]::Parse($nodeMatch.Groups[1].Value)}else{[Guid]::NewGuid()}
$driverMatch=[regex]::Match($text,'(?m)^Driver\s*=\s*(.+?)\s*$')
$localOdbcDriver=if($driverMatch.Success){$driverMatch.Groups[1].Value}else{''}
if($env:OS -eq 'Windows_NT' -and $localOdbcDriver -match '\s'){
    $sourceDriver='HKLM:\SOFTWARE\ODBC\ODBCINST.INI\'+$localOdbcDriver.Trim('"','''')
    $stableDriver='HKLM:\SOFTWARE\ODBC\ODBCINST.INI\PostgreSQL'
    if(Test-Path -LiteralPath $sourceDriver){
        $values=Get-ItemProperty -LiteralPath $sourceDriver
        New-Item -Path $stableDriver -Force|Out-Null
        foreach($name in 'Driver','Setup','APILevel','ConnectFunctions','DriverODBCVer','SQLLevel'){
            if($null -ne $values.$name){Set-ItemProperty -LiteralPath $stableDriver -Name $name -Value $values.$name}
        }
        Set-ItemProperty -LiteralPath 'HKLM:\SOFTWARE\ODBC\ODBCINST.INI\ODBC Drivers' -Name 'PostgreSQL' -Value 'Installed'
        $localOdbcDriver='PostgreSQL'
    }
}
function Set-IniValue([string]$content,[string]$section,[string]$key,[string]$value){
    $safe=$value.Replace("`r",'').Replace("`n",'')
    $sectionPattern='(?ms)^\['+[regex]::Escape($section)+'\]\s*\r?\n(?<body>.*?)(?=^\[|\z)'
    $match=[regex]::Match($content,$sectionPattern)
    if(!$match.Success){return $content.TrimEnd()+"`r`n`r`n[$section]`r`n$key=$safe`r`n"}
    $body=$match.Groups['body'].Value
    $keyPattern='(?m)^\s*'+[regex]::Escape($key)+'\s*=.*$'
    if([regex]::IsMatch($body,$keyPattern)){$body=[regex]::Replace($body,$keyPattern,"$key=$safe",1)}else{$body=$body.TrimEnd()+"`r`n$key=$safe`r`n"}
    return $content.Substring(0,$match.Groups['body'].Index)+$body+$content.Substring($match.Groups['body'].Index+$match.Groups['body'].Length)
}
foreach($property in $package.sharedConfiguration.psobject.Properties){
    $setting=[string]$property.Value
    if($property.Name -eq 'Server' -and ($setting -in @('','localhost','127.0.0.1','::1'))){$setting=$serverUri.Host}
    if($property.Name -eq 'Driver' -and ![string]::IsNullOrWhiteSpace($localOdbcDriver)){
        $setting=$localOdbcDriver
    }
    $text=Set-IniValue $text 'DbInfo' $property.Name $setting
}
$text=Set-IniValue $text 'Security' 'KeyVaultMasterKey' ([string]$package.keyVaultMasterKey)
$text=Set-IniValue $text 'Cluster' 'NodeId' $nodeId.ToString()

$archive=Join-Path 'C:\ETL\Archives' ('cluster-join-'+(Get-Date -Format 'yyyyMMdd-HHmmss'))
New-Item -ItemType Directory -Path $archive -Force|Out-Null
Copy-Item -LiteralPath $configuration -Destination (Join-Path $archive 'etlforge.conf')
$temporary=$configuration+'.join.tmp'
[IO.File]::WriteAllText($temporary,$text,(New-Object Text.UTF8Encoding($false)))
Move-Item -LiteralPath $temporary -Destination $configuration -Force
& icacls.exe $configuration /inheritance:r /grant:r '*S-1-5-32-544:(F)' '*S-1-5-18:(F)' | Out-Null
if($LASTEXITCODE -ne 0){throw 'Cluster values were written but the configuration ACL could not be restricted'}
Write-Output "Joined ETLForge cluster '$($package.clusterName)' ($($package.clusterId)). Shared database configuration and vault key were synchronized; local NodeId $nodeId was preserved or generated as needed. Backup: $archive"
