import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import org.etlforge.integration.client.project.sourcecontrol.GitProjectRepository;
public final class PublishETLForgeProject {
 public static void main(String[] args)throws Exception {
  Path project=Paths.get(args[0]);
  GitProjectRepository repository=GitProjectRepository.connect(project,args[1]);
  repository.run("config","--local","user.name",args[2]);
  repository.run("config","--local","user.email",args[3]);
  Files.write(Paths.get("C:/ETL/Reports/source-control/first-publish-files.txt"),repository.status().getBytes(StandardCharsets.UTF_8));
  String revision=repository.commit("Initial ETLForge Test Operations project",false,true);
  Files.write(Paths.get("C:/ETL/Reports/source-control/first-publish-revision.txt"),revision.getBytes(StandardCharsets.UTF_8));
  Files.write(Paths.get("C:/ETL/Reports/source-control/first-publish-tracked-files.txt"),repository.run("ls-files").getBytes(StandardCharsets.UTF_8));
  System.out.println("Published revision: "+revision);
  System.out.println("Working tree clean: "+repository.status().isEmpty());
 }
}
