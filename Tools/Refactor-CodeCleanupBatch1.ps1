$ErrorActionPreference = 'Stop'
$report = 'C:\ETL\Reports\code-cleanup'
$encoding = New-Object Text.UTF8Encoding($false)
$changes = @()
$candidates = Import-Csv "$report\private-single-reference-review.csv"
$approved = @('createTextArea','setupRowSelector','showInOverlay','drawFolderNodeInPartialMode','getRandomName','printCrom','defineBeginAndEndQuote')
foreach ($candidate in $candidates | Where-Object Name -in $approved) {
    $source = [IO.File]::ReadAllText($candidate.Path)
    $pattern = '(?ms)^   private [^\r\n]*\b' + [regex]::Escape($candidate.Name) + '\([^\r\n]*\)\s*\{\r?\n.*?^   \}\r?\n'
    $matches = [regex]::Matches($source, $pattern)
    if ($matches.Count -ne 1) { throw "Expected one method: $($candidate.Path) :: $($candidate.Name)" }
    $source = [regex]::Replace($source, $pattern, '')
    [IO.File]::WriteAllText($candidate.Path,$source,$encoding)
    $changes += [pscustomobject]@{Path=$candidate.Path;OldName=$candidate.Name;NewName='';Action='Removed unused private method'}
}
$renames = @{
    'C:\ETL\Studio\src\main\java\org\etlforge\application\ui\look\LookAndFeelCustomizer.java' = @{
        getUseSystemLF='shouldUseSystemAppearance';installDarkClassic='installEtlForgeDarkAppearance';customizeDefaultLF='installConfiguredAppearance';installLookAndFeel='installSelectedAppearance';customizeUiResources='configureEtlForgeUiResources';customizeToolTipManager='configureEtlForgeTooltips'
    }
    'C:\ETL\Studio\src\main\java\org\etlforge\integration\client\main\LookAndFeelInstaller.java' = @{
        installEDT='installAppearanceOnEventThread';applyWindowTheme='applyEtlForgeWindowAppearance';installWindowTitleColors='installEtlForgeWindowColors'
    }
    'C:\ETL\Studio\src\main\native\WindowTheme.cpp' = @{
        Java_org_etlforge_integration_client_main_LookAndFeelInstaller_applyWindowTheme='Java_org_etlforge_integration_client_main_LookAndFeelInstaller_applyEtlForgeWindowAppearance'
    }
}
foreach ($path in $renames.Keys) {
    $source = [IO.File]::ReadAllText($path)
    foreach ($oldName in $renames[$path].Keys) {
        $pattern = '\b' + [regex]::Escape($oldName) + '\b'
        if (![regex]::IsMatch($source,$pattern)) { throw "Missing rename input: $oldName" }
        $newName = $renames[$path][$oldName]
        $source = [regex]::Replace($source,$pattern,$newName)
        $changes += [pscustomobject]@{Path=$path;OldName=$oldName;NewName=$newName;Action='Renamed declaration and local callers'}
    }
    [IO.File]::WriteAllText($path,$source,$encoding)
}
$changes | Export-Csv "$report\batch1-symbol-changes.csv" -NoTypeInformation
$changes | Group-Object Action | Select-Object Name,Count
