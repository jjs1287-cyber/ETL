param([string]$Endpoint='http://127.0.0.1:47100/axis/konga_admin')
$ErrorActionPreference='Stop'
$config=Get-Content C:\ETL\Server\instances\dev\etlforge.conf
$user=(($config|Where-Object{$_ -match '^User\s*='}|Select-Object -First 1)-split '=',2)[1].Trim()
$env:PGPASSWORD=((($config|Where-Object{$_ -match '^Password\s*='}|Select-Object -First 1)-split '=',2)[1].Trim())
try{$raw=& C:\ETL\Server\runtime\postgresql-18\pgsql\bin\psql.exe -h 127.0.0.1 -p 5432 -U $user -d etlforge_dev_tran -At -v ON_ERROR_STOP=1 -c 'SELECT json_build_object(''settings'',settings,''revision'',revision)::text FROM cloudlogging.configuration WHERE singleton'}finally{Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue}
if($LASTEXITCODE -ne 0 -or !$raw){throw 'Could not read cloud logging configuration'}
$record=$raw|ConvertFrom-Json;$settings=$record.settings
function Unprotect-Legacy([string]$hex){
 if(!$hex){return ''};if($hex.StartsWith('gcm1:')){throw 'Cloud credentials are already portable; no migration is needed'}
 $bytes=New-Object byte[] ($hex.Length/2);for($i=0;$i -lt $bytes.Length;$i++){$bytes[$i]=[Convert]::ToByte($hex.Substring($i*2,2),16)}
 try{$plain=[Security.Cryptography.ProtectedData]::Unprotect($bytes,$null,[Security.Cryptography.DataProtectionScope]::LocalMachine);return [Text.Encoding]::UTF8.GetString($plain)}finally{if($plain){[Array]::Clear($plain,0,$plain.Length)};[Array]::Clear($bytes,0,$bytes.Length)}
}
$password=Unprotect-Legacy $settings.passwordProtected;$bearer=Unprotect-Legacy $settings.bearerProtected
$settings.PSObject.Properties.Remove('passwordProtected');$settings.PSObject.Properties.Remove('bearerProtected');$settings|Add-Member -NotePropertyName password -NotePropertyValue $password -Force;$settings|Add-Member -NotePropertyName bearerToken -NotePropertyValue $bearer -Force
$passwordHash=[Security.Cryptography.MD5]::Create();try{$admin=[IO.File]::ReadAllText('C:\ETL\Server\private\etlforge-dev-studio-admin-password.txt').Trim();$hash=([BitConverter]::ToString($passwordHash.ComputeHash([Text.Encoding]::UTF8.GetBytes($admin)))).Replace('-','').ToLowerInvariant()}finally{$passwordHash.Dispose();$admin=$null}
$request=@{action='cloud.save';revision=[long]$record.revision;settings=$settings}|ConvertTo-Json -Depth 20 -Compress
$body='<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><m:auditRequest xmlns:m="urn:org.etlforge.integration.server.implementation.webservice.admin.Admin"><in0>Admin</in0><in1>'+$hash+'</in1><in2>'+[Security.SecurityElement]::Escape($request)+'</in2></m:auditRequest></s:Body></s:Envelope>'
$response=Invoke-WebRequest -UseBasicParsing -Uri $Endpoint -Method Post -ContentType 'text/xml; charset=utf-8' -Headers @{SOAPAction='""'} -Body ([Text.Encoding]::UTF8.GetBytes($body));if($response.StatusCode -ne 200){throw 'Cloud credential migration request failed'}
$password=$null;$bearer=$null;$settings=$null;$request=$null
Write-Output 'Migrated cloud logging credentials from DPAPI to portable AES-256-GCM.'
