param([switch]$ImportOnly, [string]$WorkspacePath)
$ErrorActionPreference = 'Stop'
. "$PSScriptRoot\DevEnvironment.ps1"
$root = Join-Path $PSScriptRoot 'EclipseWorkspaceSetup'
$install = if ($env:ECLIPSE_HOME) { $env:ECLIPSE_HOME } else { Join-Path $PSScriptRoot 'EclipseIDE\eclipse' }
if (!$WorkspacePath) { $WorkspacePath = Join-Path $script:ETLRoot 'EclipseWorkspace' }
$jre = Get-ChildItem "$install\plugins" -Directory -Filter 'org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_*' | Sort-Object Name -Descending | Select-Object -First 1 -ExpandProperty FullName
if (!$jre) { throw "Eclipse's bundled Java runtime was not found under $install" }
$vm = Join-Path $jre 'jre\bin\java.exe'
$ecj = Get-ChildItem "$install\plugins" -Filter 'org.eclipse.jdt.core.compiler.batch_*.jar' | Select-Object -First 1 -ExpandProperty FullName
$plugins = Get-ChildItem "$install\plugins" -Filter *.jar | Where-Object { $_.Name -match '^(org.eclipse.(core.(runtime|resources|jobs|contenttype)|equinox.(common|app|registry|preferences)|jdt.(core|launching)|debug.core)_|org.osgi.service.prefs_)' }
$cp = $plugins.FullName -join ';'
New-Item -ItemType Directory -Force "$root\bin" | Out-Null
& $vm -jar $ecj -source 21 -target 21 -encoding UTF-8 -classpath $cp -d "$root\bin" "$root\src\etlforge\eclipse\setup\ImportStudio.java" 2>&1 | Tee-Object "$root\compiler.log"
if ($LASTEXITCODE -ne 0) { throw 'Eclipse workspace helper compilation failed' }
Copy-Item "$root\plugin.xml" "$root\bin\plugin.xml" -Force
& "$env:JAVA_HOME\bin\jar.exe" cfm "$root\etlforge.eclipse.setup.jar" "$root\MANIFEST.MF" -C "$root\bin" .
if ($LASTEXITCODE -ne 0) { throw 'Eclipse workspace helper packaging failed' }
$config = Join-Path $root 'configuration-local'
New-Item -ItemType Directory -Force "$config\org.eclipse.equinox.simpleconfigurator" | Out-Null
$bundleFile = Join-Path $config 'org.eclipse.equinox.simpleconfigurator\bundles.info'
$bundles = [IO.File]::ReadAllText("$install\configuration\org.eclipse.equinox.simpleconfigurator\bundles.info")
$bundles += "`netlforge.eclipse.setup,1.0.0,$(([Uri]"$root\etlforge.eclipse.setup.jar").AbsoluteUri),4,false`n"
[IO.File]::WriteAllText($bundleFile, $bundles, [Text.UTF8Encoding]::new($false))
$ini = [IO.File]::ReadAllText("$install\configuration\config.ini")
$ini = $ini -replace '(?m)^org.eclipse.equinox.simpleconfigurator.configUrl=.*\r?\n?', ''
$ini = $ini -replace '(?m)^osgi.install.area=.*\r?\n?', ''
$ini += "`nosgi.install.area=$(([Uri]($install+'\')).AbsoluteUri)`norg.eclipse.equinox.simpleconfigurator.configUrl=$(([Uri]$bundleFile).AbsoluteUri)`n"
[IO.File]::WriteAllText("$config\config.ini", $ini, [Text.UTF8Encoding]::new($false))
$arguments = @('-nosplash', '-clean', '-vm', $vm, '-configuration', $config, '-data', $WorkspacePath, '-application', 'etlforge.eclipse.setup.importStudio', '-consoleLog')
if (!$ImportOnly) { $arguments += '--build' }
$arguments += @('-vmargs', "-Detlforge.root=$script:ETLRoot", "-Detlforge.jdk8=$env:JAVA_HOME")
& "$install\eclipsec.exe" @arguments 2>&1 | Tee-Object (Join-Path $script:ETLRoot 'Reports\eclipse-studio-import.log')
if ($LASTEXITCODE -ne 0) { throw 'Eclipse validation failed. Close Eclipse if it is using the same workspace, or pass -WorkspacePath with a separate directory.' }
