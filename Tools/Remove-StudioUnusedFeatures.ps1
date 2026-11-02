$ErrorActionPreference='Stop'
$root='C:\ETL\Studio'
function Edit($relative,$from,$to){$p=Join-Path $root $relative;$s=[IO.File]::ReadAllText($p);if(!$s.Contains($from)){throw "Missing text in $p"};[IO.File]::WriteAllText($p,$s.Replace($from,$to))}
Edit 'src\main\java\org\jitterbit\integration\client\info\ClientInfo.java' 'return ApplicationConfiguration.getBoolean("DataLoader", false);' '// ETLForge supports the full Studio workflow only, regardless of legacy configuration.
      return false;'
$p="$root\src\main\resources\jitterbit-client-config\ApplicationContext.Core.Common.xml"
$s=[IO.File]::ReadAllText($p);$s=[regex]::Replace($s,'(?s)\s*<bean id="DataLoaderMigration".*?</bean>','');[IO.File]::WriteAllText($p,$s)
$paths=@('org\jitterbit\integration\client\dl\migration','org\jitterbit\integration\client\entity\settings\sqlite','org\jitterbit\util\persist\sqlite','org\jitterbit\integration\client\runtime\SQLiteTempFileFix.java')
foreach($relative in $paths){$source=Join-Path "$root\src\main\java" $relative;$feature=if($relative -like '*dl\migration*'){'dataloader-removed'}else{'sqlite-removed'};foreach($file in @(Get-ChildItem $source -Recurse -File)){$rel=$file.FullName.Substring(("$root\src\main\java\").Length);$dest=Join-Path "$root\features\$feature\src\main\java" $rel;New-Item -ItemType Directory -Force (Split-Path $dest)|Out-Null;Move-Item -LiteralPath $file.FullName -Destination $dest}}
Edit 'src\main\java\org\jitterbit\integration\client\runtime\ClientPlatform.java' '            SQLiteTempFileFix.fix();' ''
Edit 'src\main\java\org\jitterbit\integration\client\project\ManagedProjectImpl.java' 'import org.jitterbit.integration.client.entity.settings.sqlite.SQLiteEntityResources;' ''
Edit 'src\main\java\org\jitterbit\integration\client\project\ManagedProjectImpl.java' 'return SQLiteEntityResources.inFolder(var2.subFolder("Entity"));' 'return new EntityResourcesImpl(var2.subFolder("Entity"));'
$old='C:\ETL\Reference\v4\jitterbit-code-r45309-branches-v4-4.1.x\integration\java\client\project-manager\src\org\jitterbit\integration\client\project\EntityResourcesImpl.java'
$s=[IO.File]::ReadAllText($old).Replace('org.jitterbit.util.file.Folder','org.jitterbit.io.Folder').Replace('implements EntityResources','implements org.jitterbit.integration.client.entity.settings.CloseableEntityResources').Replace('void cleanup(', 'void close(')
[IO.File]::WriteAllText("$root\src\main\java\org\jitterbit\integration\client\project\EntityResourcesImpl.java",$s)
Add-Content "$root\metadata\includes\jitterbit-integration-client-project-manager.includes" "`norg/jitterbit/integration/client/project/EntityResourcesImpl.class`norg/jitterbit/integration/client/project/EntityResourcesImpl`$*.class"
$p="$root\src\main\java\org\jitterbit\integration\project\upgrade\v_5_0_0_1\ResourceMigrator.java"
Copy-Item $p "$root\features\sqlite-removed\ResourceMigrator.original.java"
[IO.File]::WriteAllText($p,@"
package org.jitterbit.integration.project.upgrade.v_5_0_0_1;
import org.jitterbit.integration.client.project.*;
import org.jitterbit.integration.data.project.IntegrationProject;
import org.jitterbit.integration.project.upgrade.DefaultPreLoader;
/** Retains version checks; XML settings remain the supported store in ETLForge. */
final class ResourceMigrator implements ProjectVersionHandler {
 public PreLoadingResult preLoading(String version,ProjectFile file,UpgradeStrategy strategy){
  DefaultPreLoader loader=new DefaultPreLoader(version,file,strategy);loader.run();return loader.getResult();
 }
 public boolean postLoading(IntegrationProject project,ProjectFile file){return true;}
}
"@)
New-Item -ItemType Directory -Force "$root\features\sqlite-removed\lib"|Out-Null
Get-ChildItem "$root\lib" -Filter '*sqlite*.jar'|Move-Item -Destination "$root\features\sqlite-removed\lib"
$p="$root\.classpath";$s=[IO.File]::ReadAllText($p);$s=[regex]::Replace($s,'(?m)^.*sqlite.*\r?\n','');[IO.File]::WriteAllText($p,$s)
$p="$root\src\test\java\etlforge\studio\StudioRecoverySmoke.java";$s=[IO.File]::ReadAllText($p);$s=[regex]::Replace($s,'(?s)  Class.forName\("org.sqlite.JDBC"\);.*?  ClassLoader loader=', '  ClassLoader loader=');$s=$s.Replace('  if(rebuilt){','  if(rebuilt){
   check(!org.jitterbit.integration.client.info.ClientInfo.isDataLoader(),"Data Loader disabled");
   try{Class.forName("org.sqlite.JDBC");throw new AssertionError("SQLite driver present");}catch(ClassNotFoundException expected){}
   try{Class.forName("org.jitterbit.integration.client.dl.migration.ProjectMigrator");throw new AssertionError("Data Loader migration present");}catch(ClassNotFoundException expected){}');[IO.File]::WriteAllText($p,$s)
$p='C:\ETL\Tools\Prepare-StudioProject.ps1';$s=[IO.File]::ReadAllText($p).Replace('$_.Name -notlike ''jitterbit*''','$_.Name -notlike ''jitterbit*'' -and $_.Name -notlike ''*sqlite*''');[IO.File]::WriteAllText($p,$s)
'Feature removal applied'
