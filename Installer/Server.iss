#define ProductVersion GetEnv("ETLFORGE_INSTALLER_VERSION")
#define StageRoot GetEnv("ETLFORGE_SERVER_STAGE")
#define OutputRoot GetEnv("ETLFORGE_INSTALLER_OUTPUT")
#define JavaUrl "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u504-b01/OpenJDK8U-jdk_x64_windows_hotspot_8u504b01.zip"
#define JavaHash "ea43d46ede95b51e44a12c66711706cddc762e0a766c54bccea18954e902b2aa"
#define PgUrl "https://get.enterprisedb.com/postgresql/postgresql-18.6-5-windows-x64-binaries.zip"
#define PgHash GetEnv("ETLFORGE_POSTGRESQL_HASH")

[Setup]
AppId={{270509DA-64E8-4680-8D48-8272973E9708}
AppName=ETLForge Server
AppVersion={#ProductVersion}
AppVerName=ETLForge Server {#ProductVersion}
VersionInfoVersion={#ProductVersion}
DefaultDirName={autopf}\ETLForge\Server
DefaultGroupName=ETLForge
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
PrivilegesRequired=admin
OutputDir={#OutputRoot}
OutputBaseFilename=ETLForge-Server-{#ProductVersion}-win-x64
Compression=lzma2/max
SolidCompression=yes
WizardStyle=modern
UninstallDisplayName=ETLForge Server {#ProductVersion}
DisableProgramGroupPage=yes
SetupLogging=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "{#StageRoot}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Dirs]
Name: "{commonappdata}\ETLForge\default\configuration"
Name: "{commonappdata}\ETLForge\default\private"
Name: "{commonappdata}\ETLForge\default\data"
Name: "{commonappdata}\ETLForge\default\temp"
Name: "{autopf}\ETLForge\Log\default"

[Icons]
Name: "{group}\Configure ETLForge Server"; Filename: "powershell.exe"; Parameters: "-NoProfile -ExecutionPolicy Bypass -File ""{app}\Configure-Server.ps1"""; WorkingDir: "{app}"

[Code]
var
  DownloadPage: TDownloadWizardPage;

function JavaIsInstalled(): Boolean;
begin
  Result := FileExists(ExpandConstant('{app}\runtime\java\bin\java.exe'));
end;

function PgIsInstalled(): Boolean;
begin
  Result := FileExists(ExpandConstant('{app}\runtime\postgresql-18\pgsql\bin\postgres.exe'));
end;

procedure InitializeWizard;
begin
  DownloadPage := CreateDownloadPage('Downloading server prerequisites',
    'Downloads are version-pinned and verified before extraction.', nil);
end;

function NextButtonClick(CurPageID: Integer): Boolean;
begin
  Result := True;
  if CurPageID = wpReady then
  begin
    DownloadPage.Clear;
    if not JavaIsInstalled() then DownloadPage.Add('{#JavaUrl}', 'java.zip', '{#JavaHash}');
    if not PgIsInstalled() then DownloadPage.Add('{#PgUrl}', 'postgresql.zip', '{#PgHash}');
    if (not JavaIsInstalled()) or (not PgIsInstalled()) then
    begin
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
end;

procedure CurStepChanged(CurStep: TSetupStep);
var
  ResultCode: Integer;
  Command, JavaZip, PgZip: String;
begin
  if CurStep = ssPostInstall then
  begin
    JavaZip := ExpandConstant('{tmp}\java.zip');
    PgZip := ExpandConstant('{tmp}\postgresql.zip');
    ForceDirectories(ExpandConstant('{app}\runtime'));
    if (not JavaIsInstalled()) and (JavaZip <> '') then
    begin
      Command := '-NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath ''' + JavaZip +
        ''' -DestinationPath ''' + ExpandConstant('{app}\runtime') + ''' -Force; $d=Get-ChildItem -LiteralPath ''' +
        ExpandConstant('{app}\runtime') + ''' -Directory | Where-Object Name -Like ''jdk*'' | Select-Object -First 1; Move-Item -LiteralPath $d.FullName -Destination ''' +
        ExpandConstant('{app}\runtime\java') + '''"';
      if not Exec(ExpandConstant('{sys}\WindowsPowerShell\v1.0\powershell.exe'), Command, '', SW_HIDE, ewWaitUntilTerminated, ResultCode) or (ResultCode <> 0) then
        RaiseException('The verified Java runtime could not be extracted.');
    end;
    if (not PgIsInstalled()) and (PgZip <> '') then
    begin
      ForceDirectories(ExpandConstant('{app}\runtime\postgresql-18'));
      Command := '-NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath ''' + PgZip +
        ''' -DestinationPath ''' + ExpandConstant('{app}\runtime\postgresql-18') + ''' -Force"';
      if not Exec(ExpandConstant('{sys}\WindowsPowerShell\v1.0\powershell.exe'), Command, '', SW_HIDE, ewWaitUntilTerminated, ResultCode) or (ResultCode <> 0) then
        RaiseException('The verified PostgreSQL runtime could not be extracted.');
    end;
    if not Exec(ExpandConstant('{sys}\WindowsPowerShell\v1.0\powershell.exe'),
      '-NoProfile -ExecutionPolicy Bypass -File "' + ExpandConstant('{app}\ServerPostInstall.ps1') + '" -InstallRoot "' +
      ExpandConstant('{app}') + '" -DataRoot "' + ExpandConstant('{commonappdata}\ETLForge\default') +
      '" -LogRoot "' + ExpandConstant('{autopf}\ETLForge\Log\default') + '"', '', SW_HIDE, ewWaitUntilTerminated, ResultCode) or (ResultCode <> 0) then
      RaiseException('ETLForge Server directory configuration failed.');
  end;
end;
