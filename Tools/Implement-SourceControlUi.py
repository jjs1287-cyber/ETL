from pathlib import Path
root=Path('C:/ETL/Studio');base=root/'src/main/java/org/etlforge/integration/client/ui/project/sourcecontrol';base.mkdir(parents=True,exist_ok=True)
(base/'SourceControlUi.java').write_text(r'''package org.etlforge.integration.client.ui.project.sourcecontrol;

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
      String files=repo.status();String branch=repo.branch();
      String comment=onUi(()->promptComment(files,branch,deployment));
      if(comment==null)return false;
      repo.commit(comment,deployment,true);return true;
   }
   public static String promptComment(String files,String branch,boolean deployment){
      JPanel panel=new JPanel(new BorderLayout(8,8));JTextArea list=new JTextArea(files.trim().isEmpty()?"No file changes. A deployment checkpoint will be committed.":files,12,75);list.setEditable(false);
      JPanel top=new JPanel(new GridLayout(0,1));top.add(new JLabel("Branch: "+branch));top.add(new JLabel("Commit and push to GitHub"+(deployment?" before deployment":"")));top.add(new JLabel("Review the files below. Saved credentials in project files will be included."));panel.add(top,BorderLayout.NORTH);panel.add(new JScrollPane(list),BorderLayout.CENTER);
      JTextArea comment=new JTextArea(4,75);JPanel bottom=new JPanel(new BorderLayout());bottom.add(new JLabel("Commit comment (required):"),BorderLayout.NORTH);bottom.add(new JScrollPane(comment),BorderLayout.CENTER);panel.add(bottom,BorderLayout.SOUTH);
      while(JOptionPane.showConfirmDialog(null,panel,deployment?"Commit before deployment":"Commit and Push",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){if(!comment.getText().trim().isEmpty())return comment.getText().trim();JOptionPane.showMessageDialog(null,"Enter a commit comment.");}return null;
   }
   public static void background(Component parent,Callable<String> task){
      JDialog progress=new JDialog((Frame)null,"Source Control",true);progress.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);progress.add(new JLabel("Working with Git. Please wait...",SwingConstants.CENTER));progress.setSize(380,110);progress.setLocationRelativeTo(parent);
      new SwingWorker<String,Void>(){protected String doInBackground()throws Exception{return task.call();}protected void done(){progress.dispose();try{String result=get();if(result!=null)JOptionPane.showMessageDialog(parent,result,"Source Control",JOptionPane.INFORMATION_MESSAGE);}catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();JOptionPane.showMessageDialog(parent,cause.getMessage(),"Source Control failed",JOptionPane.ERROR_MESSAGE);}}}.execute();progress.setVisible(true);
   }
}
''',encoding='utf-8')
(base/'SourceControlAction.java').write_text(r'''package org.etlforge.integration.client.ui.project.sourcecontrol;

import java.awt.event.ActionEvent;
import javax.swing.*;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.project.sourcecontrol.GitProjectRepository;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;

public final class SourceControlAction extends AbstractApplicationAction {
   private final ApplicationWindow window;private final CurrentProjectService current;private final SaveAllGate save;private final String mode;
   private final CurrentProjectServiceListener listener=new CurrentProjectServiceListener.Adapter(){public void projectOpened(CurrentProjectEvent e){setEnabledSafely(true);}public void projectClosed(CurrentProjectEvent e){setEnabledSafely(false);}};
   public SourceControlAction(ApplicationWindow window,CurrentProjectService current,SaveAllGate save,String mode,String title){super("action.sourcecontrol."+mode,title,null);this.window=window;this.current=current;this.save=save;this.mode=mode;current.addListener(listener);setEnabled(current.getManagedProject()!=null);}
   public void execute(ActionEvent e){save.saveAndRun(()->{ManagedProject project=current.getManagedProject();if(project==null)return;
      if(mode.equals("connect")){String url=JOptionPane.showInputDialog(window.getFrame(),"Existing GitHub repository URL (use an empty repository):");if(url==null)return;SourceControlUi.background(window.getFrame(),()->{GitProjectRepository.connect(project.getLocation().toPath(),url);return "Repository connected. Choose Commit and Push to publish the saved project.";});}
      else SourceControlUi.background(window.getFrame(),()->{if(mode.equals("commit"))return SourceControlUi.commit(project,false)?"Committed and pushed successfully.":null;GitProjectRepository repo=new GitProjectRepository(project.getLocation().toPath());if(mode.equals("push")){repo.push();return "Push completed.";}String text=mode.equals("history")?repo.history():repo.status();SourceControlUi.onUi(()->{JTextArea area=new JTextArea(text.isEmpty()?"Working tree is clean.":text,22,85);area.setEditable(false);JOptionPane.showMessageDialog(window.getFrame(),new JScrollPane(area),"Git "+mode,JOptionPane.PLAIN_MESSAGE);return null;});return null;});});}
   public void dispose(){current.removeListener(listener);super.dispose();}
}
''',encoding='utf-8')
(base/'SourceControlMenu.java').write_text(r'''package org.etlforge.integration.client.ui.project.sourcecontrol;

import java.util.*;
import javax.swing.*;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.*;
public final class SourceControlMenu implements Menu {
   private final java.util.List<ApplicationAction> actions;
   public SourceControlMenu(java.util.List<ApplicationAction> actions){this.actions=actions;}
   public String getID(){return "menu.SourceControl";}
   public boolean isSupported(){return true;}
   public void addExtension(MenuExtensionPoint p,MenuContribution c){}
   public void removeExtension(MenuExtensionPoint p,String id){}
   public void removeAllExtensions(){}
   public JMenu createJMenu(MenuDictionary dictionary){JMenu menu=new JMenu("Source Control");populate(menu);return menu;}
   public void populate(JMenu menu){menu.removeAll();for(ApplicationAction action:actions)menu.add(new JMenuItem(action));}
   public void populate(JPopupMenu menu){menu.removeAll();for(ApplicationAction action:actions)menu.add(new JMenuItem(action));}
}
''',encoding='utf-8')
(base/'GitCloneLaunchOption.java').write_text(r'''package org.etlforge.integration.client.ui.project.sourcecontrol;

import java.awt.*;
import java.nio.file.*;
import javax.swing.*;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.project.sourcecontrol.GitProjectRepository;
import org.etlforge.integration.client.ui.interchange.component.launcher.AbstractLaunchOption;
import org.etlforge.integration.client.ui.project.loading.LoadingIssuesPresenter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;

public final class GitCloneLaunchOption extends AbstractLaunchOption {
   private final ProjectManager manager;private final JPanel panel=new JPanel(new BorderLayout(10,10));private final JTextField url=new JTextField(55),branch=new JTextField(),folder=new JTextField(55);private final JButton clone=new JButton("Clone and Open"),browse=new JButton("Browse...");private final JLabel status=new JLabel("One project per repository, with its .jip file at the repository root.");private IntegrationProject project;private boolean busy;
   public GitCloneLaunchOption(ProjectManager manager){super("Open from Source Control",UIManager.getIcon("FileView.directoryIcon"));this.manager=manager;JPanel fields=new JPanel(new GridLayout(0,1,4,4));fields.add(new JLabel("GitHub repository URL:"));fields.add(url);fields.add(new JLabel("Branch (blank uses default):"));fields.add(branch);fields.add(new JLabel("New local checkout folder:"));JPanel row=new JPanel(new BorderLayout());row.add(folder);row.add(browse,BorderLayout.EAST);fields.add(row);fields.add(new JLabel("Authenticate beforehand with Git Credential Manager or your SSH agent."));fields.add(new JLabel("The complete repository downloads locally. Deployment requires commit and push."));panel.add(fields,BorderLayout.NORTH);JPanel buttons=new JPanel(new BorderLayout());buttons.add(status,BorderLayout.CENTER);buttons.add(clone,BorderLayout.EAST);panel.add(buttons,BorderLayout.SOUTH);browse.addActionListener(e->{JFileChooser chooser=new JFileChooser();chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);chooser.setApproveButtonText("Use folder name");if(chooser.showSaveDialog(panel)==JFileChooser.APPROVE_OPTION)folder.setText(chooser.getSelectedFile().getAbsolutePath());});clone.addActionListener(e->start());}
   private void start(){if(busy)return;final String remote=url.getText(),ref=branch.getText(),local=folder.getText();busy=true;setEditable(false);status.setText("Cloning repository...");new SwingWorker<IntegrationProject,Void>(){protected IntegrationProject doInBackground()throws Exception{Path root=GitProjectRepository.cloneGitHub(remote,ref,Paths.get(local));Path file=GitProjectRepository.projectFile(root);return manager.loadProject(file.getFileName().toString().replaceFirst("\\.jip$",""),root.toFile(),new LoadingIssuesPresenter());}protected void done(){busy=false;setEditable(true);try{project=get();status.setText("Clone complete.");launchProject();}catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();status.setText("Clone/open failed. The local checkout is retained if it was created.");JOptionPane.showMessageDialog(panel,cause.getMessage(),"Source Control",JOptionPane.ERROR_MESSAGE);}}}.execute();}
   private void setEditable(boolean value){url.setEnabled(value);branch.setEnabled(value);folder.setEnabled(value);clone.setEnabled(value);browse.setEnabled(value);}
   public void activated(){url.requestFocusInWindow();}
   public void requestFocus(){url.requestFocusInWindow();}
   public void decorate(UiCanvas canvas){canvas.display(panel);}
   public IntegrationProject getProject(){return project;}
}
''',encoding='utf-8')
# native Git does all repository work; Java adapts Studio UI only
q=root/'src/main/java/org/etlforge/integration/client/ui/deploy/DeployDataImpl.java';s=q.read_text(encoding='utf-8').replace('   @Override\n   public FileStore getFileStore()', '   public ManagedProject getManagedProject() { return this.managedProject; }\n\n   @Override\n   public FileStore getFileStore()');q.write_text(s,encoding='utf-8')
q=root/'src/main/java/org/etlforge/integration/client/ui/deploy/DeployJob.java';s=q.read_text(encoding='utf-8');needle='      this.deployManager.deploy(this.data, this);';s=s.replace(needle,'''      try {
         if (this.data instanceof DeployDataImpl && !org.etlforge.integration.client.ui.project.sourcecontrol.SourceControlUi.commit(((DeployDataImpl)this.data).getManagedProject(), true)) {
            this.cancelled(); return;
         }
      } catch (Exception ex) { this.caught(ex); return; }
'''+needle);q.write_text(s,encoding='utf-8')
# Spring wiring
res=root/'src/main/resources/etlforge-client-config'
q=res/'ApplicationContext.Core.Common.xml';s=q.read_text(encoding='utf-8').replace('<ref bean="menu.Actions" />','<ref bean="menu.Actions" />\n                <ref bean="menu.SourceControl" />',1)
s=s.replace('<ref bean="action.core.Login" />','<ref bean="action.sourcecontrol.commit" />\n                            <ref bean="action.core.Login" />',1)
beans='''\n<bean id="menu.SourceControl" class="org.etlforge.integration.client.ui.project.sourcecontrol.SourceControlMenu"><constructor-arg><list>'''
for mode in ['connect','commit','push','status','history']:beans+='<ref bean="action.sourcecontrol.'+mode+'" />'
beans+='</list></constructor-arg></bean>\n'
for mode,title in [('connect','Connect to GitHub...'),('commit','Commit and Push...'),('push','Push'),('status','Changed Files...'),('history','Commit History...')]:
 beans+=f'<bean id="action.sourcecontrol.{mode}" class="org.etlforge.integration.client.ui.project.sourcecontrol.SourceControlAction"><constructor-arg ref="ui.AppWin"/><constructor-arg ref="core.CurrentProject"/><constructor-arg ref="ic.SaveAllGate"/><constructor-arg value="{mode}"/><constructor-arg value="{title}"/></bean>\n'
s=s.replace('</beans>',beans+'</beans>');q.write_text(s,encoding='utf-8')
q=res/'ApplicationContext.Interchange.Enterprise.xml';s=q.read_text(encoding='utf-8');marker='\t\t\t\t<ref bean="interchange.OpenLocalProject" />';s=s.replace(marker,marker+'\n                <bean class="org.etlforge.integration.client.ui.project.sourcecontrol.GitCloneLaunchOption"><constructor-arg ref="platform.ProjectManager" /></bean>');q.write_text(s,encoding='utf-8')
