$ErrorActionPreference='Stop'
$source='C:\ETL\Server\build-probe\native-xslt\bin'
$target='C:\ETL\Server\runtime\xslt-native'
New-Item -ItemType Directory -Force $target | Out-Null
foreach($file in @('ETLForgeXsltWorker.exe','saxonc-he.dll','saxonc-core-he.dll')) {
    Copy-Item -LiteralPath "$source\$file" -Destination $target
}
Copy-Item -LiteralPath C:\ETL\Dependencies\saxonc-12.10.1\notices -Destination $target -Recurse -Force
Copy-Item -LiteralPath C:\ETL\Dependencies\saxonc-12.10.1\README.md -Destination "$target\SaxonC-README.md"
Get-FileHash C:\ETL\Dependencies\saxonc-12.10.1.zip | Export-Csv "$target\package-sha256.csv" -NoTypeInformation
