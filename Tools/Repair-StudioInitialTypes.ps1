$ErrorActionPreference='Stop'
$errors=Import-Csv C:\ETL\Reports\studio-compile-errors.csv
$root='C:\ETL\Studio\src\main\java'
$changes=@()
foreach($group in ($errors | Group-Object Path)){
 $path=$group.Name
 $lines=[Collections.Generic.List[string]]::new();$lines.AddRange([string[]](Get-Content -LiteralPath $path))
 foreach($e in ($group.Group | Sort-Object {[int]$_.Line} -Descending -Unique)){
  $i=[int]$e.Line-1;$line=$lines[$i];$new=$line;$kind=''
  if($e.Error -match '^incompatible types: Object\[\] cannot be converted to '){
   $new=[regex]::Replace($line,'([\w.$]+)\.toArray\((new ([\w.$]+)\[[^\r\n]*?\])\)',{param($m)'('+$m.Groups[3].Value+'[]) '+$m.Groups[1].Value+'.toArray('+$m.Groups[2].Value+')'})
   $kind='Restore typed array cast; toArray receives an array of the same type'
  }elseif($e.Error -match '^incompatible types: Object cannot be converted to (.+)$'){
   $target=$matches[1]
   if($line -match '^(\s*)for \((.+?) (\w+) : (.+)\) \{$'){
    $indent=$matches[1];$type=$matches[2];$variable=$matches[3];$iterable=$matches[4];$temp='__etlItem'+([int]$e.Line)
    $new=$indent+'for (Object '+$temp+' : '+$iterable+') {'+"`n"+$indent+'   '+$type+' '+$variable+' = ('+$type+') '+$temp+';'
    $kind='Restore per-element cast from raw iterable'
   }elseif($target -notmatch 'CAP#|\?|^(boolean|int|long|short|byte|char|float|double)$'){
    if($line -match '^(\s*return\s+)([^;]+);\s*$'){$new=$matches[1]+'('+$target+') ('+$matches[2]+');';$kind='Restore reference return cast'}
    elseif($line -match '^(\s*(?:final )?[\w.$<>?, \[\]]+\s+\w+\s*=\s*)([^;]+);\s*$'){$new=$matches[1]+'('+$target+') ('+$matches[2]+');';$kind='Restore reference assignment cast'}
   }
  }
  if($new -cne $line){$lines[$i]=$new;$changes+=[pscustomobject]@{File=$path;Line=$e.Line;Kind=$kind;Before=$line;After=$new}}
 }
 $text=$lines -join "`n"
 if($path.EndsWith('SoapBindingStub.java')){
  $updated=[regex]::Replace($text,'(?m)^(\s+)Class<[^\r\n;]+> (var\d+) =','$1Class<?> $2 =')
  if($updated -cne $text){$changes+=[pscustomobject]@{File=$path;Line=0;Kind='Restore heterogeneous Class local variables in generated SOAP stub';Before='Narrow Class<T> reused for different class literals';After='Class<?> local'};$text=$updated}
 }
 [IO.File]::WriteAllText($path,$text+"`n",[Text.UTF8Encoding]::new($false))
}
$changes | Export-Csv C:\ETL\Reports\studio-type-repairs-pass1.csv -NoTypeInformation
$changes | Group-Object Kind | Select-Object Name,Count
