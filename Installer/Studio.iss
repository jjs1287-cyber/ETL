#define ProductVersion GetEnv("ETLFORGE_INSTALLER_VERSION")
#define StageRoot GetEnv("ETLFORGE_STUDIO_STAGE")
#define OutputRoot GetEnv("ETLFORGE_INSTALLER_OUTPUT")
#define JavaUrl "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u504-b01/OpenJDK8U-jdk_x64_windows_hotspot_8u504b01.zip"
#define JavaHash "ea43d46ede95b51e44a12c66711706cddc762e0a766c54bccea18954e902b2aa"

[Setup]
AppId={{137042D2-FA63-4C97-BA5F-68E3564BFA62}
AppName=ETLForge Studio
AppVersion={#ProductVersion}
AppVerName=ETLForge {#ProductVersion}
VersionInfoVersion={#ProductVersion}
DefaultDirName={autopf}\ETLForge\Studio
DefaultGroupName=ETLForge
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
PrivilegesRequired=admin
OutputDir={#OutputRoot}
OutputBaseFilename=ETLForge-Studio-{#ProductVersion}-win-x64
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern
UninstallDisplayName=ETLForge Studio {#ProductVersion}
ChangesAssociations=yes
DisableProgramGroupPage=yes
SetupLogging=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "Create a desktop shortcut"; GroupDescription: "Shortcuts:"; Flags: unchecked

[Files]
Source: "{#StageRoot}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\ETLForge"; Filename: "{app}\ETLForge.cmd"; WorkingDir: "{app}"
Name: "{autodesktop}\ETLForge"; Filename: "{app}\ETLForge.cmd"; WorkingDir: "{app}"; Tasks: desktopicon

[Registry]
Root: HKA; Subkey: "Software\Classes\.etlforge"; ValueType: string; ValueData: "ETLForge.Project"; Flags: uninsdeletevalue
Root: HKA; Subkey: "Software\Classes\ETLForge.Project"; ValueType: string; ValueData: "ETLForge Project"; Flags: uninsdeletekey
Root: HKA; Subkey: "Software\Classes\ETLForge.Project\shell\open\command"; ValueType: string; ValueData: """{app}\ETLForge.cmd"" ""%1"""

[Code]
var
  DownloadPage: TDownloadWizardPage;

function JavaIsInstalled(): Boolean;
begin
  Result := FileExists(ExpandConstant('{app}\runtime\java\bin\javaw.exe'));
end;

procedure InitializeWizard;
begin
  DownloadPage := CreateDownloadPage('Downloading required runtime',
    'ETLForge Studio uses a private, pinned 64-bit Java runtime.', nil);
end;

function NextButtonClick(CurPageID: Integer): Boolean;
begin
  Result := True;
  if (CurPageID = wpReady) and not JavaIsInstalled() then
  begin
    DownloadPage.Clear;
    DownloadPage.Add('{#JavaUrl}', 'java.zip', '{#JavaHash}');
    DownloadPage.Show;
    try
      try
        DownloadPage.Download;
      except
        SuppressibleMsgBox(AddPeriod(GetExceptionMessage), mbCriticalError, MB_OK, IDOK);
        Result := False;
      end;
    finally
      DownloadPage.Hide;
    end;
  end;
end;

procedure CurStepChanged(CurStep: TSetupStep);
var
  ResultCode: Integer;
  Command: String;
begin
  if (CurStep = ssPostInstall) and not JavaIsInstalled() then
  begin
    ForceDirectories(ExpandConstant('{app}\runtime'));
    Command := '-NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath ''' +
      ExpandConstant('{tmp}\java.zip') + ''' -DestinationPath ''' + ExpandConstant('{app}\runtime') +
      ''' -Force; $d=Get-ChildItem -LiteralPath ''' + ExpandConstant('{app}\runtime') +
      ''' -Directory | Where-Object Name -Like ''jdk*'' | Select-Object -First 1; Move-Item -LiteralPath $d.FullName -Destination ''' +
      ExpandConstant('{app}\runtime\java') + '''"';
    if not Exec(ExpandConstant('{sys}\WindowsPowerShell\v1.0\powershell.exe'), Command, '', SW_HIDE, ewWaitUntilTerminated, ResultCode) or (ResultCode <> 0) then
      RaiseException('The verified Java runtime could not be extracted.');
  end;
end;

[Run]
Filename: "{app}\ETLForge.cmd"; Description: "Launch ETLForge"; Flags: postinstall nowait skipifsilent runasoriginaluser
