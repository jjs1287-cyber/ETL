from pathlib import Path
p=Path('C:/ETL/Studio/src/main/java/org/etlforge/integration/client/project/sourcecontrol/GitProjectRepository.java');s=p.read_text(encoding='utf-8')
s=s.replace('return Files.exists(directory.resolve(".git"),LinkOption.NOFOLLOW_LINKS);','return Files.exists(directory.resolve(".git"),LinkOption.NOFOLLOW_LINKS);')
s=s.replace('new String(Files.readAllBytes(file),StandardCharsets.UTF_8)','decodeUtf8(Files.readAllBytes(file))')
s=s.replace('new String(output.toByteArray(),StandardCharsets.UTF_8)','decodeUtf8(output.toByteArray())')
s=s.replace('   public static String execute(Path cwd,List<String> args)throws IOException {','''   private static String decodeUtf8(byte[] bytes)throws IOException { return StandardCharsets.UTF_8.newDecoder().decode(java.nio.ByteBuffer.wrap(bytes)).toString(); }
   private static String gitExecutable(){String configured=System.getProperty("etlforge.git.executable");if(configured!=null&&!configured.trim().isEmpty())return configured;Path portable=Paths.get("C:/ETL/Tools/Git/cmd/git.exe");return Files.isRegularFile(portable)?portable.toString():"git";}
   public static String execute(Path cwd,List<String> args)throws IOException {''')
s=s.replace('Arrays.asList("git","-c"','Arrays.asList(gitExecutable(),"-c"')
s=s.replace('builder.environment().put("GIT_SSH_COMMAND","ssh -oBatchMode=yes -oStrictHostKeyChecking=yes");','''Path executable=Paths.get(gitExecutable()).toAbsolutePath();Path gitRoot=executable.getParent()==null?null:executable.getParent().getParent();Path ssh=gitRoot==null?null:gitRoot.resolve("usr/bin/ssh.exe");String sshCommand=ssh!=null&&Files.isRegularFile(ssh)?"\\\""+ssh.toString().replace('\\\\','/')+"\\\"":"ssh";builder.environment().put("GIT_SSH_COMMAND",sshCommand+" -oBatchMode=yes -oStrictHostKeyChecking=yes");''')
p.write_text(s,encoding='utf-8')
p=Path('C:/ETL/Studio/src/main/java/org/etlforge/integration/client/ui/project/sourcecontrol/SourceControlUi.java');s=p.read_text(encoding='utf-8').replace('      repo.commit(comment,deployment,true);','      if(!repo.status().equals(files))throw new IOException("Project files changed after the preview. Review and commit again.");\n      repo.commit(comment,deployment,true);');p.write_text(s,encoding='utf-8')
