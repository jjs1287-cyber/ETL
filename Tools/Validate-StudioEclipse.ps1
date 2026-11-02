param([switch]$ImportOnly)
$ErrorActionPreference='Stop'
$root='C:\ETL\Tools\EclipseWorkspaceSetup'
$install='C:\Program Files\eclipse'
$jre=Get-ChildItem "$install\plugins" -Directory -Filter 'org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_*' | Sort-Object Name -Descending | Select-Object -First 1 -ExpandProperty FullName
$vm=Join-Path $jre 'jre\bin\java.exe'
$ecj=Get-ChildItem "$install\plugins" -Filter 'org.eclipse.jdt.core.compiler.batch_*.jar' | Select-Object -First 1 -ExpandProperty FullName
$plugins=Get-ChildItem "$install\plugins" -Filter *.jar | Where-Object {$_.Name -match '^(org.eclipse.(core.(runtime|resources|jobs|contenttype)|equinox.(common|app|registry|preferences)|jdt.(core|launching)|debug.core)_|org.osgi.service.prefs_)'}
$cp=$plugins.FullName -join ';'
& $vm -jar $ecj -source 21 -target 21 -encoding UTF-8 -classpath $cp -d "$root\bin" "$root\src\etlforge\eclipse\setup\ImportStudio.java" 2>&1 | Tee-Object "$root\compiler.log"
if($LASTEXITCODE -ne 0){throw 'Eclipse workspace helper compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\jar.exe cfm "$root\etlforge.eclipse.setup.jar" "$root\MANIFEST.MF" -C "$root\bin" .
$argsList=@('-nosplash','-clean','-vm',$vm,'-configuration',"$root\configuration",'-data','C:\ETL\EclipseWorkspace','-application','etlforge.eclipse.setup.importStudio','-consoleLog')
if(!$ImportOnly){$argsList+='--build'}
& "$install\eclipsec.exe" @argsList 2>&1 | Tee-Object C:\ETL\Reports\eclipse-studio-import.log
if($LASTEXITCODE -ne 0){throw 'Eclipse project validation failed. See Reports/eclipse-studio-validation.txt'}
