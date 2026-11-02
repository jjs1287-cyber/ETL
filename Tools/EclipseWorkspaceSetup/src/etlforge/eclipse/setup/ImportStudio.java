package etlforge.eclipse.setup;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.*;
public final class ImportStudio implements IApplication {
 public Object start(IApplicationContext context) throws Exception {
  boolean build=Arrays.asList((String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS)).contains("--build");
  IWorkspace ws=ResourcesPlugin.getWorkspace();
  IWorkspaceDescription wd=ws.getDescription(); wd.setAutoBuilding(false); ws.setDescription(wd);
  IProjectDescription pd=ws.loadProjectDescription(new Path("C:/ETL/Studio/.project"));
  pd.setLocation(new Path("C:/ETL/Studio"));
  IProject project=ws.getRoot().getProject(pd.getName());
  if(!project.exists()) project.create(pd,null);
  if(!project.isOpen()) project.open(null);
  IVMInstallType type=JavaRuntime.getVMInstallType("org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType");
  IVMInstall vm=type.findVMInstall("etlforge-jdk8");
  if(vm==null) vm=type.createVMInstall("etlforge-jdk8");
  vm.setName("ETLForge Java 8"); vm.setInstallLocation(new File("C:/ETL/Tools/jdk8u504-b01"));
  JavaRuntime.setDefaultVMInstall(vm,null); JavaRuntime.saveVMConfiguration();
  project.refreshLocal(IResource.DEPTH_INFINITE,null);
  IJavaProject jp=JavaCore.create(project);
  StringBuilder report=new StringBuilder("Project: "+project.getName()+"\nLocation: "+project.getLocation()+"\nJDK: "+vm.getInstallLocation()+"\n");
  report.append("Resolved classpath entries: ").append(jp.getResolvedClasspath(true).length).append('\n');
  if(build){
   project.build(IncrementalProjectBuilder.CLEAN_BUILD,null);
   project.build(IncrementalProjectBuilder.FULL_BUILD,null);
  }
  int errors=0;
  for(IMarker m: project.findMarkers(IMarker.PROBLEM,true,IResource.DEPTH_INFINITE)){
   if(m.getAttribute(IMarker.SEVERITY,0)==IMarker.SEVERITY_ERROR){
    ++errors;report.append(m.getResource().getProjectRelativePath()).append(':').append(m.getAttribute(IMarker.LINE_NUMBER,0)).append(": ").append(m.getAttribute(IMarker.MESSAGE,"")).append('\n');
   }
  }
  if(build && errors==0){IWorkspaceDescription ready=ws.getDescription();ready.setAutoBuilding(true);ws.setDescription(ready);}
  report.append("Automatic build enabled: ").append(ws.getDescription().isAutoBuilding()).append('\n');
  report.append("Build requested: ").append(build).append("\nError markers: ").append(errors).append('\n');
  Files.write(new File("C:/ETL/Reports/eclipse-studio-validation.txt").toPath(),report.toString().getBytes(StandardCharsets.UTF_8));
  System.out.print(report.toString()); ws.save(true,null);
  return Integer.valueOf(build && errors>0 ? 1 : 0);
 }
 public void stop() {}
}


