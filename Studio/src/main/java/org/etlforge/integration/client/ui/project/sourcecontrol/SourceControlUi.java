package org.etlforge.integration.client.ui.project.sourcecontrol;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.sourcecontrol.GitProjectRepository;
import org.etlforge.integration.data.project.IntegrationProjectLock;

public final class SourceControlUi {
   private SourceControlUi(){}
   public static <T>T onUi(Callable<T> work)throws Exception{
      if(EventQueue.isDispatchThread())return work.call();AtomicReference<T> value=new AtomicReference<>();AtomicReference<Exception> error=new AtomicReference<>();
      EventQueue.invokeAndWait(()->{try{value.set(work.call());}catch(Exception ex){error.set(ex);}});if(error.get()!=null)throw error.get();return value.get();
   }
   public static boolean commit(ManagedProject project,boolean deployment)throws Exception{
      if(project==null)return !deployment;
      Path root=project.getLocation().toPath();
      if(!GitProjectRepository.isControlled(root)){if(deployment)return true;throw new IOException("Connect this project using Source Control > Connect to GitHub first.");}
      IntegrationProjectLock.acquire();try{project.saveToDisk();}finally{IntegrationProjectLock.release();}
      GitProjectRepository repo=new GitProjectRepository(root);repo.prepareIgnore();
      String files=repo.status();String branch=repo.branch();String fingerprint=repo.fingerprint();
      String comment=onUi(()->promptComment(files,branch,deployment));
      if(comment==null)return false;
      if(!repo.status().equals(files)||!repo.branch().equals(branch)||!repo.fingerprint().equals(fingerprint))throw new IOException("Project files changed after the preview. Review and commit again.");
      repo.commit(comment,deployment,true);return true;
   }
   public static String promptComment(String files,String branch,boolean deployment){
      JPanel panel=new JPanel(new BorderLayout(8,8));JTextArea list=new JTextArea(files.trim().isEmpty()?(deployment?"No file changes. A deployment checkpoint will be committed.":"No file changes. Existing commits will be pushed."):files,12,75);list.setEditable(false);
      JPanel top=new JPanel(new GridLayout(0,1));top.add(new JLabel("Branch: "+branch));top.add(new JLabel("Commit and push to GitHub"+(deployment?" before deployment":"")));top.add(new JLabel("Review the files below. Saved credentials in project files will be included."));panel.add(top,BorderLayout.NORTH);panel.add(new JScrollPane(list),BorderLayout.CENTER);
      JTextArea comment=new JTextArea(4,75);JPanel bottom=new JPanel(new BorderLayout());bottom.add(new JLabel("Commit comment (required):"),BorderLayout.NORTH);bottom.add(new JScrollPane(comment),BorderLayout.CENTER);panel.add(bottom,BorderLayout.SOUTH);
      while(JOptionPane.showConfirmDialog(null,panel,deployment?"Commit before deployment":"Commit and Push",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){if(!comment.getText().trim().isEmpty())return comment.getText().trim();JOptionPane.showMessageDialog(null,"Enter a commit comment.");}return null;
   }
   public static void background(Component parent,Callable<String> task){
      JDialog progress=new JDialog((Frame)null,"Source Control",true);progress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);progress.add(new JLabel("Working with Git. Please wait...",SwingConstants.CENTER));progress.setSize(380,110);progress.setLocationRelativeTo(parent);
      new SwingWorker<String,Void>(){protected String doInBackground()throws Exception{return task.call();}protected void done(){progress.dispose();try{String result=get();if(result!=null)JOptionPane.showMessageDialog(parent,result,"Source Control",JOptionPane.INFORMATION_MESSAGE);}catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();JOptionPane.showMessageDialog(parent,cause.getMessage(),"Source Control failed",JOptionPane.ERROR_MESSAGE);}}}.execute();progress.setVisible(true);
   }
}
