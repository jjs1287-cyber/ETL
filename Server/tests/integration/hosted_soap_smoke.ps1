$ErrorActionPreference='Stop'
$pw=[IO.File]::ReadAllText('C:\ETL\Server\private\etlforge-dev-studio-admin-password.txt').Trim()
$auth=[Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes('Admin:'+$pw))
$body='<?xml version="1.0" encoding="UTF-8"?><soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:e="urn:etlforge:echo"><soap:Body><e:Echo><e:Text>Hosted ETLForge test 日本 &amp; XML</e:Text></e:Echo></soap:Body></soap:Envelope>'
$r=Invoke-WebRequest -Uri 'http://127.0.0.1:47100/etlforge/soap?id=42000000-0000-4000-8000-000000000007' -Method Post -Headers @{Authorization='Basic '+$auth;SOAPAction='"urn:etlforge:echo/Echo"'} -ContentType 'text/xml; charset=utf-8' -Body ([Text.Encoding]::UTF8.GetBytes($body)) -TimeoutSec 30
$r.Content | Set-Content C:\ETL\Reports\hosted-echo-response.xml -Encoding utf8
[xml]$xml=$r.Content
$n=$xml.SelectNodes("//*[local-name()='EchoResponse']/*[local-name()='Text']")
if($r.StatusCode -ne 200 -or $n.Count -ne 1 -or $n[0].InnerText -cne 'Hosted ETLForge test 日本 & XML' -or $xml.SelectNodes("//*[local-name()='Fault']").Count -ne 0){throw 'Hosted SOAP response validation failed'}
$wsdl=Invoke-WebRequest -Uri 'http://127.0.0.1:47100/etlforge/soap/wsdl?id=42000000-0000-4000-8000-000000000007' -Headers @{Authorization='Basic '+$auth} -TimeoutSec 15
[xml]$definition=$wsdl.Content
if($definition.DocumentElement.LocalName -ne 'definitions'){throw 'WSDL download failed'}
$unauth=Invoke-WebRequest -Uri 'http://127.0.0.1:47100/etlforge/soap?id=42000000-0000-4000-8000-000000000007' -Method Post -Body $body -ContentType 'text/xml' -SkipHttpErrorCheck
if($unauth.StatusCode -ne 401 -or !$unauth.Headers['WWW-Authenticate']){throw 'Authentication challenge failed'}
'PASS: Hosted SOAP echo, Unicode, escaped XML, WSDL download and authentication challenge'
$r.Content

