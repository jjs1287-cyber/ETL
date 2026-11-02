import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.etlforge.integration.client.project.sourcecontrol.GitProjectRepository;
public final class GitProjectRepositoryTest {
 static int count;
 static void check(boolean value,String label){if(!value)throw new AssertionError(label);System.out.println("PASS "+label);count++;}
 interface Task{void run()throws Exception;}
 static void rejects(Task task,String label)throws Exception{boolean rejected=false;try{task.run();}catch(IOException expected){rejected=true;}check(rejected,label);}
 public static void main(String[]args)throws Exception{
  Path root=Paths.get(args[0]),project=root.resolve("project");Files.createDirectory(project);Files.write(project.resolve("Fixture.jip"),"fixture".getBytes(StandardCharsets.UTF_8));Files.createDirectories(project.resolve("Data"));Files.write(project.resolve("Data/unicode.xml"),"<value>café 中文 🚀</value>".getBytes(StandardCharsets.UTF_8));byte[] binary=new byte[256];for(int i=0;i<256;i++)binary[i]=(byte)i;Files.write(project.resolve("Data/binary.bin"),binary);
  check(!GitProjectRepository.isControlled(project),"ordinary projects remain uncontrolled");rejects(()->new GitProjectRepository(project),"missing repository rejected");
  for(String bad:new String[]{"http://github.com/a/b","https://user:secret@github.com/a/b","https://github.com.evil/a/b","-option","file:///C:/temp","ssh://localhost/path"})rejects(()->GitProjectRepository.validateGitHubUrl(bad),"unsafe URL rejected");
  check(GitProjectRepository.validateGitHubUrl("git@github.com:team/repo.git").startsWith("git@"),"SSH GitHub URL accepted");check(GitProjectRepository.validateGitHubUrl("https://github.com/team/repo.git").startsWith("https"),"HTTPS GitHub URL accepted");
  GitProjectRepository repo=GitProjectRepository.connect(project,"https://github.com/etlforge-tests/project.git");check(repo.branch().equals("main"),"new repository uses main branch");
  Files.createDirectories(project.resolve("cache"));Files.write(project.resolve("cache/local.bin"),binary);Files.write(project.resolve(".project.lock"),binary);check(!repo.status().contains(".project.lock")&&!repo.status().contains("local.bin"),"cache and project locks excluded");
  rejects(()->repo.commit("   ",false,false),"blank commit comment rejected");
  String revision=repo.commit("Initial UTF-8 project",false,true);check(revision.matches("[a-f0-9]{40,64}"),"commit returns revision");check(repo.status().isEmpty(),"commit leaves clean tree");check(repo.run("ls-files").contains("Data/binary.bin"),"project assets tracked");check(!repo.run("ls-files").contains("cache/"),"cache not staged");
  Path checkout=root.resolve("checkout");GitProjectRepository.cloneGitHub("https://github.com/etlforge-tests/project.git","main",checkout);check(Arrays.equals(binary,Files.readAllBytes(checkout.resolve("Data/binary.bin"))),"clone preserves all binary bytes");check(Arrays.equals(Files.readAllBytes(project.resolve("Data/unicode.xml")),Files.readAllBytes(checkout.resolve("Data/unicode.xml"))),"clone preserves UTF-8 bytes");check(Files.exists(GitProjectRepository.projectFile(checkout)),"clone includes root project");
  rejects(()->GitProjectRepository.cloneGitHub("https://github.com/etlforge-tests/project.git","main",checkout),"clone refuses existing folder");rejects(()->GitProjectRepository.cloneGitHub("https://github.com/etlforge-tests/project.git","../bad",root.resolve("bad")),"unsafe branch rejected");
  String checkpoint=repo.commit("Deployment checkpoint",true,true);check(!checkpoint.equals(revision),"unchanged deploy creates checkpoint commit");check(repo.history().contains("Deployment checkpoint"),"history includes required comment");
  GitProjectRepository second=new GitProjectRepository(checkout);Files.write(checkout.resolve("Data/unicode.xml"),"other branch work".getBytes(StandardCharsets.UTF_8));second.commit("Diverging change",false,false);rejects(()->second.push(),"non-fast-forward push rejected without force");check(second.history().contains("Diverging change"),"failed push retains local commit");
  repo.run("remote","set-url","origin","https://github.com/etlforge-tests/missing.git");rejects(()->repo.push(),"missing remote fails push");repo.run("remote","set-url","origin","https://github.com/etlforge-tests/project.git");
  Files.delete(project.resolve("Data/unicode.xml"));repo.commit("Delete file",false,true);check(!repo.run("ls-files").contains("unicode.xml"),"deletions committed");
  String unchanged=repo.commit("No changes",false,false);check(unchanged.equals(repo.run("rev-parse","HEAD").trim()),"manual unchanged commit does not duplicate history");
  Files.write(project.resolve("Second.jip"),new byte[0]);rejects(()->repo.prepareIgnore(),"multiple projects in root rejected");Files.delete(project.resolve("Second.jip"));
  repo.run("add","-f",".project.lock");rejects(()->repo.prepareIgnore(),"already tracked lock blocks commit");repo.run("reset","HEAD","--",".project.lock");
  repo.run("checkout","--detach");rejects(()->repo.branch(),"detached HEAD blocks committing");repo.run("checkout","main");
  Files.createDirectories(root.resolve("empty"));rejects(()->GitProjectRepository.projectFile(root.resolve("empty")),"missing project file rejected");
  Path nested=project.resolve("nested");Files.createDirectories(nested);rejects(()->new GitProjectRepository(nested),"nested project is not silently attached to parent repo");
  Files.createDirectories(nested.resolve(".git"));rejects(()->repo.prepareIgnore(),"nested repositories cannot become embedded gitlinks");Files.delete(nested.resolve(".git"));
  Path ignore=project.resolve(".gitignore");byte[] original=Files.readAllBytes(ignore);Files.write(ignore,new byte[]{(byte)0xc3,(byte)0x28});rejects(()->repo.prepareIgnore(),"malformed UTF-8 ignore file rejected");Files.write(ignore,original);
  String fingerprint=repo.fingerprint();Files.write(project.resolve("Data/binary.bin"),new byte[]{1,2,3});check(!repo.fingerprint().equals(fingerprint),"content fingerprint detects changes to already modified files");Files.write(project.resolve("Data/binary.bin"),binary);check(repo.fingerprint().equals(fingerprint),"content fingerprint is deterministic");Files.write(project.resolve("cache/local.bin"),new byte[]{4,5,6});check(repo.fingerprint().equals(fingerprint),"ignored cache does not alter content fingerprint");
  System.out.println("PASSED "+count);
 }
}
