$ErrorActionPreference='Stop'
$ns='urn:org.etlforge.integration.server.implementation.webservice.admin.Admin'
$password=[IO.File]::ReadAllText('C:\ETL\Server\private\etlforge-dev-studio-admin-password.txt').Trim()
$md5=[Security.Cryptography.MD5]::Create()
try{$hash=[Convert]::ToHexString($md5.ComputeHash([Text.Encoding]::UTF8.GetBytes($password))).ToLowerInvariant()}finally{$md5.Dispose();$password=$null}
function Soap([string]$Method,[string]$Extra=''){
 $body='<s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"><s:Body><m:'+$Method+' xmlns:m="'+$ns+'"><user>Admin</user><password>'+$hash+'</password>'+$Extra+'</m:'+$Method+'></s:Body></s:Envelope>'
 $response=Invoke-WebRequest 'http://127.0.0.1:47100/axis/konga_admin' -Method Post -ContentType 'text/xml; charset=utf-8' -Headers @{SOAPAction=$Method} -Body ([Text.Encoding]::UTF8.GetBytes($body))
 return [xml]$response.Content
}
function Settings([xml]$Response){
 $result=$Response.SelectSingleNode('//*[local-name()="getProxySettingsReturn"]');$values=@{}
 foreach($field in @('Enabled','Host','Port','UserName','Password','TrustAllCertificates')){$values[$field]=$result.SelectSingleNode('*[local-name()="'+$field+'"]').InnerText}
 $values.Exceptions=@($result.SelectNodes('*[local-name()="Exceptions"]/*[local-name()="item"]') | ForEach-Object {$_.InnerText})
 return $values
}
function Save([hashtable]$Values){
 $xml='<proxySettings>'
 foreach($field in @('Enabled','Host','Port','UserName','Password','TrustAllCertificates')){$xml+='<'+$field+'>'+[Security.SecurityElement]::Escape([string]$Values[$field])+'</'+$field+'>'}
 $xml+='<Exceptions>';foreach($item in $Values.Exceptions){$xml+='<item>'+[Security.SecurityElement]::Escape($item)+'</item>'};$xml+='</Exceptions></proxySettings>'
 Soap 'setProxySettings' $xml | Out-Null
}
$original=Settings (Soap 'getProxySettings')
if($original.Enabled -ne 'false' -and $original.Enabled -ne '0'){throw 'This test requires proxy usage to be disabled.'}
try {
 $test=$original.Clone();$test.Password='unicode-test-'+[char]233+[char]::ConvertFromUtf32(128512);$test.UserName='DOMAIN\unicode-test';$test.Host='';$test.Port='0';$test.Enabled='false'
 Save $test
 $loaded=Settings (Soap 'getProxySettings')
 if($loaded.Password -cne $test.Password){throw 'Supplementary Unicode proxy password changed.'}
 'PASS: native proxy SOAP handles a valid UTF-8 supplementary-character password; proxy usage remained disabled.'
}finally{Save $original}
