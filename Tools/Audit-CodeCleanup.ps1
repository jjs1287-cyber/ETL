param([string]$Root = 'C:\ETL')
$ErrorActionPreference = 'Stop'
$report = Join-Path $Root 'Reports\code-cleanup'
New-Item -ItemType Directory -Path $report -Force | Out-Null
$extensions = @('.java','.cpp','.c','.h','.hpp','.cxx','.cc')
$files = foreach ($component in @('Studio','Server')) {
    Get-ChildItem (Join-Path $Root "$component\src") -File -Recurse |
        Where-Object { $_.Extension -in $extensions }
}
$inventory = foreach ($file in $files) {
    [pscustomobject]@{Path=$file.FullName; Bytes=$file.Length; SHA256=(Get-FileHash -LiteralPath $file.FullName -Algorithm SHA256).Hash}
}
$inventory | Export-Csv (Join-Path $report 'source-manifest.csv') -NoTypeInformation
$candidates = foreach ($file in $files | Where-Object Extension -eq '.java') {
    $source = [IO.File]::ReadAllText($file.FullName)
    $pattern = '(?m)^\s*private\s+(?:(?:static|final|synchronized|native|strictfp)\s+)*(?:[\w.$<>?\[\],]+\s+)+(?<name>[A-Za-z_$][\w$]*)\s*\('
    foreach ($match in [regex]::Matches($source, $pattern)) {
        $name = $match.Groups['name'].Value
        $count = [regex]::Matches($source, '\b' + [regex]::Escape($name) + '\b').Count
        if ($count -eq 1) {
            [pscustomobject]@{Path=$file.FullName;Name=$name;Disposition='Review required: reflection, serialization, JNI and configuration may call this method'}
        }
    }
}
$candidates | Export-Csv (Join-Path $report 'private-single-reference-review.csv') -NoTypeInformation
[pscustomobject]@{SourceFiles=$inventory.Count;PrivateReviewCandidates=@($candidates).Count;Report=$report}
