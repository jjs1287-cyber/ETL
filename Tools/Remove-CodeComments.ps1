param([switch]$Apply)
$ErrorActionPreference = 'Stop'
$archiveDirectory = (Get-Content C:\ETL\Reports\code-cleanup-latest-archive.txt -Raw).Trim()
$archive = Join-Path $archiveDirectory 'source-before-cleanup.zip'
if (!(Test-Path -LiteralPath (Join-Path $archiveDirectory 'archive-sha256.csv'))) { throw 'Completed source archive required.' }
$expected = Import-Csv (Join-Path $archiveDirectory 'archive-sha256.csv')
if ((Get-FileHash -LiteralPath $archive).Hash -ne $expected.Hash) { throw 'Source archive hash mismatch.' }
Add-Type -TypeDefinition @'
using System;
using System.Text;
using System.Text.RegularExpressions;
public static class EtlForgeCommentCleanup {
 public static string Rewrite(string text, out int removed, out int retained) {
  removed=0; retained=0;
  var output=text.ToCharArray();
  for(int i=0;i<text.Length;) {
   if(text[i]=='R' && i+1<text.Length && text[i+1]=='"') {
    int open=text.IndexOf('(',i+2);
    if(open>=0 && open-i<=18) {
     string end=")"+text.Substring(i+2,open-i-2)+"\"";
     int close=text.IndexOf(end,open+1,StringComparison.Ordinal);
     if(close<0) throw new Exception("Unterminated raw string");
     i=close+end.Length; continue;
    }
   }
   if(text[i]=='"' || text[i]=='\'') {
    char quote=text[i++];
    while(i<text.Length) {
     if(text[i]=='\\') { i=Math.Min(i+2,text.Length); continue; }
     if(text[i++]==quote) break;
    }
    continue;
   }
   if(i+1<text.Length && text[i]=='/' && (text[i+1]=='/' || text[i+1]=='*')) {
    int start=i; bool line=text[i+1]=='/'; i+=2;
    if(line) {
     while(i<text.Length) {
      if(text[i]=='\r' || text[i]=='\n') {
       if(i>start && text[i-1]=='\\') { if(text[i]=='\r' && i+1<text.Length && text[i+1]=='\n') i++; i++; continue; }
       break;
      }
      i++;
     }
    } else {
     int close=text.IndexOf("*/",i,StringComparison.Ordinal);
     if(close<0) throw new Exception("Unterminated block comment");
     i=close+2;
    }
    string comment=text.Substring(start,i-start);
    if(Regex.IsMatch(comment,@"copyright|SPDX-License-Identifier|licensed under|permission is hereby granted|redistribution and use|all rights reserved",RegexOptions.IgnoreCase)) { retained++; continue; }
    for(int j=start;j<i;j++) if(output[j]!='\r' && output[j]!='\n') output[j]=' ';
    removed++; continue;
   }
   i++;
  }
  return new string(output);
 }
}
'@
$encoding = [Text.Encoding]::GetEncoding(28591)
$changes = foreach ($root in @('C:\ETL\Studio\src','C:\ETL\Server\src')) {
 foreach ($file in Get-ChildItem -LiteralPath $root -Recurse -File | Where-Object Extension -in @('.java','.cpp','.c','.h','.hpp','.cxx','.cc')) {
  $source = $encoding.GetString([IO.File]::ReadAllBytes($file.FullName))
  if ($source -match '\\u+(?:002[fFaA]|005[cC]|0022|0027)') {
   [pscustomobject]@{Path=$file.FullName;Removed=0;Retained=0;Status='Review Unicode lexical escapes'}; continue
  }
  $removed=0; $retained=0
  $cleaned=[EtlForgeCommentCleanup]::Rewrite($source,[ref]$removed,[ref]$retained)
  if ($Apply -and $removed -gt 0) { [IO.File]::WriteAllBytes($file.FullName,$encoding.GetBytes($cleaned)) }
  if ($removed -gt 0 -or $retained -gt 0) { [pscustomobject]@{Path=$file.FullName;Removed=$removed;Retained=$retained;Status=$(if ($Apply) {'Applied'} else {'Preview'})} }
 }
}
$report = 'C:\ETL\Reports\code-cleanup\comment-cleanup-' + $(if ($Apply) {'applied'} else {'preview'}) + '.csv'
$changes | Export-Csv -LiteralPath $report -NoTypeInformation
[pscustomobject]@{Files=@($changes).Count;CommentsRemoved=($changes | Measure-Object Removed -Sum).Sum;AttributionCommentsRetained=($changes | Measure-Object Retained -Sum).Sum;Applied=[bool]$Apply;Report=$report}
