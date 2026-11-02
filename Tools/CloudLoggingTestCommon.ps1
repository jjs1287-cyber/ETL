$ErrorActionPreference='Stop'
function Invoke-CloudAdmin([hashtable]$Request){
 $password=[IO.File]::ReadAllText('C:\ETL\Server\private\etlforge-dev-studio-admin-password.txt').Trim()
 $md5=[Security.Cryptography.MD5]::Create()
 try{$hash=[Convert]::ToHexString($md5.ComputeHash([Text.Encoding]::UTF8.GetBytes($password))).ToLowerInvariant()}finally{$md5.Dispose();$password=$null}
 $json=ConvertTo-Json -InputObject $Request -Depth 12 -Compress
 $body='<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><m:auditRequest xmlns:m="urn:org.etlforge.integration.server.implementation.webservice.admin.Admin"><in0>Admin</in0><in1>'+$hash+'</in1><in2>'+[Security.SecurityElement]::Escape($json)+'</in2></m:auditRequest></s:Body></s:Envelope>'
 $response=Invoke-WebRequest -Uri 'http://127.0.0.1:47100/axis/konga_admin' -Method Post -ContentType 'text/xml; charset=utf-8' -Headers @{SOAPAction='""'} -Body ([Text.Encoding]::UTF8.GetBytes($body))
 $xml=[xml]$response.Content
 $result=$xml.SelectSingleNode('//*[local-name()="auditRequestReturn"]')
 if(!$result){throw 'Cloud logging admin response missing.'}
 return ConvertFrom-Json $result.InnerText -AsHashtable
}
function Save-CloudTestSettings([hashtable]$Settings){
 $current=Invoke-CloudAdmin @{action='cloud.settings'}
 Invoke-CloudAdmin @{action='cloud.save';revision=$current.revision;settings=$Settings}
}
function Invoke-CloudExample {
 $classpath='C:\ETL\Server\build-probe\etlforge-server-java\test-classes;C:\ETL\Studio\build\classes;C:\ETL\Studio\lib\*;C:\ETL\Dependencies\java\salesforce\*'
 & C:\ETL\Tools\jdk8u504-b01\bin\java.exe '-Dfile.encoding=UTF-8' -cp $classpath CloudLoggingExamples run
 if($LASTEXITCODE -ne 0){throw 'Cloud logging producer operation failed.'}
}
