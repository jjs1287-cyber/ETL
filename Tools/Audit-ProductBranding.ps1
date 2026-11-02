$ErrorActionPreference='Stop'
$roots=@('C:\ETL\Studio\src','C:\ETL\Studio\configuration','C:\ETL\Server\src','C:\ETL\Server\tests','C:\ETL\Server\cmake')
$hits=foreach($root in $roots){
 foreach($file in Get-ChildItem -LiteralPath $root -File -Recurse){
  if($file.Name -match '(?i)jitterbit'){[pscustomobject]@{Path=$file.FullName;Line=0;Kind='filename';Text=$file.Name}}
 }
 $matches=& rg -n -i --text -g '*.java' -g '*.cpp' -g '*.h' -g '*.xml' -g '*.properties' -g '*.sql' -g '*.cmake' -g '*.ini' -g '*.conf' 'jitterbit' $root
 foreach($match in $matches){if($match -match '^(.*?):(\d+):(.*)$'){
  $kind=if($Matches[3] -match '(?i)copyright'){ 'upstream legal notice' }else{'remaining text'}
  [pscustomobject]@{Path=$Matches[1];Line=$Matches[2];Kind=$kind;Text=$Matches[3]}
 }}
}
$hits|Export-Csv C:\ETL\Reports\remaining-product-branding.csv -NoTypeInformation
$hits|Group-Object Kind|Select-Object Name,Count
