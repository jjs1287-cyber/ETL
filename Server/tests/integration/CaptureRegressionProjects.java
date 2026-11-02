import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.etlforge.application.Application;
import org.etlforge.integration.client.main.IntegrationMain;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import com.etlforge.integration.client.admin.project.*;
public final class CaptureRegressionProjects {
 public static void main(String[] args)throws Exception {
  Path root=Paths.get("C:/ETL/Reports/regression-suite");
  IntegrationMain.main(new String[]{"-d",root.resolve("capture-workspace").toString()});
  ClientPlatform.waitForInitialization(60,TimeUnit.SECONDS); Thread.sleep(3000); javax.swing.SwingUtilities.invokeAndWait(()->{});
  System.setOut(new PrintStream(root.resolve("capture.txt").toFile(),"UTF-8"));
  String password=new String(Files.readAllBytes(Paths.get("C:/ETL/Server/private/etlforge-dev-studio-admin-password.txt")),StandardCharsets.UTF_8).trim();
  Application.getBean("platform.LoginService",org.etlforge.integration.client.server.login.LoginService.class).login(new org.etlforge.integration.client.server.login.LoginCredentials(org.etlforge.integration.client.server.ServerAddressFactory.usingPorts(47100,47101),"localhost:47100","Admin",password,false,false));
  ProjectDownloader downloader=Application.getBean("server.ProjectDownloader",ProjectDownloader.class);
  int failed=0;
  for(String line:Files.readAllLines(root.resolve("projects.txt"),StandardCharsets.UTF_8)){
   String[] p=line.replace("\uFEFF","").split("\\|",3);if(p.length!=3||p[0].equals("4"))continue;
   Path folder=root.resolve("downloads/"+p[0]);Files.createDirectories(folder);
   final Throwable[] error={null};final boolean[] complete={false};
   downloader.downloadProject(new IntegrationProjectId(p[1]),folder.toFile(),new ProjectDownloaderCallback.Adapter(){
    public void caught(Throwable e){error[0]=e;}
    public void downloadCompleted(ProjectLocation location){complete[0]=true;}
   });
   if(error[0]!=null||!complete[0]){System.out.println("FAIL "+p[0]+" "+p[2]+" "+(error[0]==null?"not completed":error[0].getClass().getName()+": "+error[0].getMessage()));failed++;}
   else System.out.println("PASS "+p[0]+" "+p[2]);System.out.flush();
  }
  System.out.println("Download failures="+failed);System.exit(failed==0?0:1);
 }
}

