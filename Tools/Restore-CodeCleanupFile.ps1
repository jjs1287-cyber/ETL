[CmdletBinding(SupportsShouldProcess=$true)]
param(
    [Parameter(Mandatory=$true)][string]$RelativePath,
    [string]$ArchiveDirectory = (Get-Content C:\ETL\Reports\code-cleanup-latest-archive.txt -Raw).Trim()
)
$ErrorActionPreference='Stop'
$root=[IO.Path]::GetFullPath('C:\ETL\')
$destination=[IO.Path]::GetFullPath((Join-Path $root $RelativePath))
if (!$destination.StartsWith($root,[StringComparison]::OrdinalIgnoreCase)) { throw 'Restore path must be inside C:\ETL.' }
$archive=Join-Path $ArchiveDirectory 'source-before-cleanup.zip'
$expected=Import-Csv (Join-Path $ArchiveDirectory 'archive-sha256.csv')
if ((Get-FileHash -LiteralPath $archive).Hash -ne $expected.Hash) { throw 'Archive hash mismatch.' }
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip=[IO.Compression.ZipFile]::OpenRead($archive)
try {
    $entryName=$destination.Substring($root.Length).Replace('\','/')
    $entry=$zip.GetEntry($entryName)
    if ($null -eq $entry) { throw "File absent from archive: $entryName" }
    if ($PSCmdlet.ShouldProcess($destination,'Restore original archived source file')) {
        $backup=$destination+'.before-restore-'+(Get-Date -Format 'yyyyMMddHHmmss')
        if (Test-Path -LiteralPath $destination) { Copy-Item -LiteralPath $destination -Destination $backup }
        [IO.Directory]::CreateDirectory([IO.Path]::GetDirectoryName($destination)) | Out-Null
        [IO.Compression.ZipFileExtensions]::ExtractToFile($entry,$destination,$true)
        Get-FileHash -LiteralPath $destination
    }
} finally { $zip.Dispose() }
