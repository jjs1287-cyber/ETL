$ErrorActionPreference='Stop'
$original='C:\ETL\Reference\v5\server-windows\Jitterbit Integration Server\tomcat\webapps\axis'
$working='C:\ETL\Server\src\webapps\axis'
$output='C:\ETL\Server\dist\webapps\axis'
$utf8=[Text.UTF8Encoding]::new($false)
if(!(Test-Path -LiteralPath $working)){
 foreach($file in Get-ChildItem -LiteralPath $original -Recurse -File){
  if($file.Extension -in @('.jar','.class','.dll','.exe','.so')){continue}
  $relative=$file.FullName.Substring($original.Length+1)
  $dest=Join-Path $working $relative
  New-Item -ItemType Directory -Force (Split-Path $dest)|Out-Null
  Copy-Item -LiteralPath $file.FullName -Destination $dest
  if($file.Extension -in @('.xml','.wsdd','.properties','.html','.htm','.jsp','.txt','.css','.js','.xsd','.wsdl')){
   $text=[IO.File]::ReadAllText($dest)
   $lines=[regex]::Split($text,'(?<=\n)')
   $updated=($lines|ForEach-Object {if($_ -match '(?i)copyright'){$_}else{$_.Replace('JITTERBIT','ETLFORGE').Replace('Jitterbit','ETLForge').Replace('jitterbit','etlforge').Replace('etlforge.com','etlforge.invalid').Replace('etlforge.org','etlforge.invalid')}})-join ''
   [IO.File]::WriteAllText($dest,$updated,$utf8)
  }
 }
 # Services are deployed declaratively; omit the old Axis remote administrator and its default password.
 $web=[Xml.XmlDocument]::new();$web.XmlResolver=$null;$web.Load("$working\WEB-INF\web.xml")
 foreach($node in @($web.SelectNodes('/web-app/servlet[servlet-name="AdminServlet"] | /web-app/servlet-mapping[servlet-name="AdminServlet"]'))){[void]$node.ParentNode.RemoveChild($node)}
 $web.Save("$working\WEB-INF\web.xml")
 $deployment=[Xml.XmlDocument]::new();$deployment.Load("$working\WEB-INF\server-config.wsdd")
 foreach($node in @($deployment.SelectNodes('//*[local-name()="service" and @name="AdminService"] | //*[local-name()="globalConfiguration"]/*[local-name()="parameter" and @name="adminPassword"]'))){[void]$node.ParentNode.RemoveChild($node)}
 $deployment.Save("$working\WEB-INF\server-config.wsdd")
}
if(Test-Path -LiteralPath $output){throw 'Webapp already assembled. Stop its runtime and explicitly refresh the deployment before rebuilding.'}
New-Item -ItemType Directory -Force (Split-Path $output)|Out-Null
Copy-Item -LiteralPath $working -Destination $output -Recurse
New-Item -ItemType Directory -Force "$output\WEB-INF\lib"|Out-Null
$thirdParty=Get-ChildItem "$original\WEB-INF\lib" -File -Filter *.jar | Where-Object {$_.Name -notmatch '(?i)^jitterbit|sqlite|^postgresql|^force-wsc-|^sfdc-partner-wsdl-|^force-partner-api-|^(servlet-api|jsp-api|el-api|catalina|jasper|tomcat)[-.]|^(adapter_core|ebs_concurrent|sapcommon|sapidoc|sapidoc3|sapjco3|saprfc)\.jar$'}
foreach($file in $thirdParty){Copy-Item -LiteralPath $file.FullName -Destination "$output\WEB-INF\lib"}
foreach($file in Get-ChildItem C:\ETL\Server\dist\lib -File -Filter 'etlforge*.jar' | Where-Object Name -notin @('etlforge-jms-util.jar','etlforge-integration-server-jms.jar')){Copy-Item -LiteralPath $file.FullName -Destination "$output\WEB-INF\lib"}
Copy-Item -LiteralPath C:\ETL\Dependencies\java\postgresql-42.7.14.jar -Destination "$output\WEB-INF\lib"
Copy-Item -LiteralPath C:\ETL\Dependencies\java\jsr305-3.0.2.jar -Destination "$output\WEB-INF\lib"
Get-ChildItem C:\ETL\Dependencies\java\salesforce -Filter *.jar | Copy-Item -Destination "$output\WEB-INF\lib"
$jars=@(Get-ChildItem "$output\WEB-INF\lib" -Filter *.jar)
if(@($jars|Where-Object Name -like 'etlforge*.jar').Count -ne 29){throw 'Expected all 29 active rebuilt webapp modules'}
"Assembled ETLForge Java webapp: $($jars.Count) JARs; 29 rebuilt product modules; no original product JAR fallback."
