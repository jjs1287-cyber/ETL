from pathlib import Path
import shutil
root=Path('C:/ETL/Studio'); arc=Path('C:/ETL/Archives/source-control-20261029')
files=['src/main/resources/etlforge-client-config/ApplicationContext.Core.Common.xml','src/main/resources/etlforge-client-config/ApplicationContext.Interchange.Common.xml','src/main/resources/etlforge-client-config/ApplicationContext.Interchange.Enterprise.xml','src/main/java/org/etlforge/integration/client/ui/deploy/DeployJob.java','src/main/java/org/etlforge/integration/client/ui/deploy/DeployDataImpl.java','metadata/includes/etlforge-integration-client-project.includes','metadata/includes/etlforge-integration-client-interchange.includes']
for f in files:
 p=arc/f;p.parent.mkdir(parents=True,exist_ok=True);shutil.copy2(root/f,p)
p=root/'src/main/java/org/etlforge/integration/client/project/sourcecontrol/GitProjectRepository.java';p.parent.mkdir(parents=True,exist_ok=True)
p.write_text(r'''package org.etlforge.integration.client.project.sourcecontrol;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public final class GitProjectRepository {
   private final Path root;
   private static final String IGNORE="\n# ETLForge local working files\n.project.lock\n/cache/\n/log/\n/logs/\n/temp/\n/tmp/\n*.tmp\n*.lck\n";
   public GitProjectRepository(Path directory) throws IOException {
      root=directory.toRealPath();
      if(!Files.isDirectory(root.resolve(".git"),LinkOption.NOFOLLOW_LINKS)) throw new IOException("This project is not a standalone Git checkout.");
      String actual=run("rev-parse","--show-toplevel").trim();
      if(!Paths.get(actual).toRealPath().equals(root)) throw new IOException("Use one project at the repository root.");
      if(!run("ls-files","-u").trim().isEmpty()) throw new IOException("Resolve Git merge conflicts before continuing.");
   }
   public static boolean isControlled(Path directory) { return Files.exists(directory.resolve(".git"),LinkOption.NOFOLLOW_LINKS); }
   public Path getRoot(){return root;}
   public static String validateGitHubUrl(String value) throws IOException {
      String url=value.trim();
      if(!url.matches("https://github\\.com/[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+(?:\\.git)?/?") && !url.matches("git@github\\.com:[A-Za-z0-9_.-]+/[A-Za-z0-9_.-]+(?:\\.git)?")) throw new IOException("Enter https://github.com/owner/repository.git or git@github.com:owner/repository.git. Credentials must use Git Credential Manager or your SSH agent.");
      return url;
   }
   public static Path cloneGitHub(String url,String branch,Path destination) throws IOException {
      validateGitHubUrl(url);
      if(Files.exists(destination)) throw new IOException("Choose a new local folder that does not exist.");
      Path parent=destination.toAbsolutePath().getParent();
      if(parent==null||!Files.isDirectory(parent))throw new IOException("The parent folder must exist.");
      List<String> args=new ArrayList<>(Arrays.asList("clone","--no-recurse-submodules"));
      if(!branch.trim().isEmpty()){if(!branch.matches("[A-Za-z0-9][A-Za-z0-9_./-]*")||branch.contains(".."))throw new IOException("Invalid branch name.");args.add("--branch");args.add(branch.trim());}
      args.add("--");args.add(url.trim());args.add(destination.toAbsolutePath().toString());
      execute(parent,args);
      GitProjectRepository repo=new GitProjectRepository(destination);repo.validateProject();repo.prepareIgnore();return repo.root;
   }
   public static GitProjectRepository connect(Path directory,String url) throws IOException {
      url=validateGitHubUrl(url);
      if(isControlled(directory))throw new IOException("This project already has a Git repository.");
      validateProjectDirectory(directory);
      execute(directory,Arrays.asList("init","--initial-branch=main"));
      GitProjectRepository repo=new GitProjectRepository(directory);
      repo.run("remote","add","origin",url);repo.prepareIgnore();return repo;
   }
   public static Path projectFile(Path directory) throws IOException {
      List<Path> files=new ArrayList<>();try(DirectoryStream<Path> stream=Files.newDirectoryStream(directory,"*.jip")){for(Path p:stream)files.add(p);}
      if(files.size()!=1)throw new IOException("The repository root must contain exactly one ETLForge .jip project file.");return files.get(0);
   }
   private static void validateProjectDirectory(Path dir) throws IOException {projectFile(dir);}
   private void validateProject() throws IOException {validateProjectDirectory(root);assertSafeFiles();}
   public void prepareIgnore() throws IOException {
      validateProject();Path file=root.resolve(".gitignore");
      if(Files.isSymbolicLink(file))throw new IOException("Linked ignore files are not supported.");
      String old=Files.exists(file)?new String(Files.readAllBytes(file),StandardCharsets.UTF_8):"";
      if(!old.contains("# ETLForge local working files"))Files.write(file,(old+IGNORE).getBytes(StandardCharsets.UTF_8));
      String tracked=run("ls-files","--",".project.lock","cache","log","logs","temp","tmp").trim();
      if(!tracked.isEmpty())throw new IOException("Local cache/lock/log files are already tracked. Remove them from the Git index before committing: "+tracked);
   }
   private void assertSafeFiles() throws IOException {
      Files.walkFileTree(root,new SimpleFileVisitor<Path>(){
         public FileVisitResult preVisitDirectory(Path path,java.nio.file.attribute.BasicFileAttributes attrs)throws IOException{
            if(!path.equals(root)&&path.getFileName().toString().equals(".git"))return FileVisitResult.SKIP_SUBTREE;
            if(attrs.isSymbolicLink()||attrs.isOther()||Files.isSymbolicLink(path))throw new IOException("Linked project directories are not supported: "+path.getFileName());return FileVisitResult.CONTINUE;
         }
         public FileVisitResult visitFile(Path path,java.nio.file.attribute.BasicFileAttributes attrs)throws IOException{if(!attrs.isRegularFile())throw new IOException("Linked/special project files are not supported: "+path.getFileName());return FileVisitResult.CONTINUE;}
      });
      if(!run("ls-files","--stage").lines().noneMatch(s->s.startsWith("160000 ")||s.startsWith("120000 ")))throw new IOException("Submodules and symbolic links are not supported in project repositories.");
   }
   public String status() throws IOException {return run("status","--short","--untracked-files=all");}
   public String branch() throws IOException {String branch=run("symbolic-ref","--short","HEAD").trim();if(branch.isEmpty())throw new IOException("Check out a branch before committing.");return branch;}
   public String history()throws IOException{return run("log","-20","--oneline","--no-decorate");}
   public String commit(String comment,boolean checkpoint,boolean push) throws IOException {
      if(comment==null||comment.trim().isEmpty())throw new IOException("A commit comment is required.");
      prepareIgnore();branch();
      if(run("config","user.name").trim().isEmpty()||run("config","user.email").trim().isEmpty())throw new IOException("Configure your Git user.name and user.email first.");
      run("add","-A","--",".");
      boolean changed=!run("diff","--cached","--name-only").trim().isEmpty();
      if(changed||checkpoint){Path message=Files.createTempFile("etlforge-commit-",".txt");try{Files.write(message,comment.trim().getBytes(StandardCharsets.UTF_8));List<String>args=new ArrayList<>(Arrays.asList("commit","--no-gpg-sign","--cleanup=verbatim","--file",message.toString()));if(checkpoint)args.add("--allow-empty");execute(root,args);}finally{Files.deleteIfExists(message);}}
      String revision=run("rev-parse","HEAD").trim();if(push)push();return revision;
   }
   public void push()throws IOException {String b=branch();String remote=run("remote","get-url","origin").trim();validateGitHubUrl(remote);run("push","--set-upstream","origin","HEAD:refs/heads/"+b);}
   public String run(String...args)throws IOException{return execute(root,Arrays.asList(args));}
   public static String execute(Path cwd,List<String> args)throws IOException {
      List<String> command=new ArrayList<>(Arrays.asList("git","-c","core.quotepath=false","-c","core.hooksPath=NUL","-c","core.autocrlf=false","-c","core.safecrlf=false","-c","protocol.file.allow=never","-c","protocol.ext.allow=never"));command.addAll(args);
      ProcessBuilder builder=new ProcessBuilder(command).directory(cwd.toFile()).redirectErrorStream(true);
      builder.environment().put("GIT_TERMINAL_PROMPT","0");builder.environment().put("GCM_INTERACTIVE","never");builder.environment().put("GIT_SSH_COMMAND","ssh -oBatchMode=yes -oStrictHostKeyChecking=yes");
      Process process=builder.start();ByteArrayOutputStream output=new ByteArrayOutputStream();
      Thread reader=new Thread(()->{try(InputStream input=process.getInputStream()){byte[] buffer=new byte[8192];int n;while((n=input.read(buffer))!=-1){synchronized(output){if(output.size()<2*1024*1024)output.write(buffer,0,Math.min(n,2*1024*1024-output.size()));}}}catch(IOException ignored){}},"ETLForge Git output");reader.setDaemon(true);reader.start();
      try{if(!process.waitFor(120,TimeUnit.SECONDS)){process.destroyForcibly();throw new IOException("Git timed out. No deployment was started.");}reader.join(3000);String text; synchronized(output){text=new String(output.toByteArray(),StandardCharsets.UTF_8);}if(process.exitValue()!=0)throw new IOException("Git failed: "+text.trim());return text;}catch(InterruptedException ex){process.destroyForcibly();Thread.currentThread().interrupt();throw new IOException("Git was interrupted.",ex);}
   }
}
'''.replace('if(!run("ls-files","--stage").lines().noneMatch(s->s.startsWith("160000 ")||s.startsWith("120000 ")))','if(java.util.Arrays.stream(run("ls-files","--stage").split("\\n")).anyMatch(s->s.startsWith("160000 ")||s.startsWith("120000 ")))'),encoding='utf-8')
for name,pattern in [('etlforge-integration-client-project.includes','org/etlforge/integration/client/project/sourcecontrol/**'),('etlforge-integration-client-interchange.includes','org/etlforge/integration/client/ui/project/sourcecontrol/**')]:
 q=root/'metadata/includes'/name;q.write_text(q.read_text(encoding='utf-8')+'\n'+pattern+'\n',encoding='utf-8')
