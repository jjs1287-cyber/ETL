param(
 [string]$LegacyDirectory='C:\ETL\Server\instances\dev\private\ssh-keys',
 [string]$Endpoint='http://127.0.0.1:47100/axis/konga_admin'
)
$ErrorActionPreference='Stop'
function Invoke-KeyVault([hashtable]$Request){
 $password=[IO.File]::ReadAllText('C:\ETL\Server\private\etlforge-dev-studio-admin-password.txt').Trim()
 $md5=[Security.Cryptography.MD5]::Create()
 try{$hash=([BitConverter]::ToString($md5.ComputeHash([Text.Encoding]::UTF8.GetBytes($password)))).Replace('-','').ToLowerInvariant()}finally{$md5.Dispose();$password=$null}
 $json=ConvertTo-Json $Request -Depth 10 -Compress
 $body='<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><m:auditRequest xmlns:m="urn:org.etlforge.integration.server.implementation.webservice.admin.Admin"><in0>Admin</in0><in1>'+$hash+'</in1><in2>'+[Security.SecurityElement]::Escape($json)+'</in2></m:auditRequest></s:Body></s:Envelope>'
 $response=Invoke-WebRequest -UseBasicParsing -Uri $Endpoint -Method Post -ContentType 'text/xml; charset=utf-8' -Headers @{SOAPAction='""'} -Body ([Text.Encoding]::UTF8.GetBytes($body))
 $xml=[xml]$response.Content;$node=$xml.SelectSingleNode('//*[local-name()="auditRequestReturn"]');if(!$node){throw 'Key-vault response missing'}
 ConvertFrom-Json $node.InnerText
}
if(!(Test-Path -LiteralPath $LegacyDirectory)){Write-Output 'No legacy key directory exists.';exit 0}
$files=@(Get-ChildItem -LiteralPath $LegacyDirectory -Filter *.key -File)
foreach($file in $files){
 $hex=[IO.File]::ReadAllText($file.FullName).Trim();$bytes=New-Object byte[] ($hex.Length/2)
 for($i=0;$i -lt $bytes.Length;$i++){$bytes[$i]=[Convert]::ToByte($hex.Substring($i*2,2),16)}
 $plain=[Security.Cryptography.ProtectedData]::Unprotect($bytes,$null,[Security.Cryptography.DataProtectionScope]::LocalMachine)
 try{$record=ConvertFrom-Json ([Text.Encoding]::UTF8.GetString($plain));$request=@{action='ssh.save';id=$record.id;type=$record.type;name=$record.name;privateKey=$record.privateKey;publicKey=$record.publicKey;passphrase=$record.passphrase};$null=Invoke-KeyVault $request}
 finally{[Array]::Clear($plain,0,$plain.Length);[Array]::Clear($bytes,0,$bytes.Length);$record=$null;$request=$null}
}
$result=Invoke-KeyVault @{action='ssh.list'}
foreach($file in $files){if(!($result.keys.id -contains $file.BaseName)){throw "Database verification failed for legacy key ID $($file.BaseName)"}}
if($files.Count){$archive=$LegacyDirectory+'.migrated-'+(Get-Date -Format yyyyMMdd-HHmmss);Move-Item -LiteralPath $LegacyDirectory -Destination $archive;New-Item -ItemType Directory -Path $LegacyDirectory | Out-Null;Write-Output "Migrated and verified $($files.Count) key records; legacy encrypted files archived at $archive"}
else{Write-Output 'No legacy key records required migration.'}
