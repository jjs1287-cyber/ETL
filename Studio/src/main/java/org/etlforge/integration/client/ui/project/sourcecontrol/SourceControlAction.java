package org.etlforge.integration.client.ui.project.sourcecontrol;

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
      if(mode.equals("identity")){JTextField name=new JTextField(),email=new JTextField();JPanel fields=new JPanel(new java.awt.GridLayout(0,1));fields.add(new JLabel("Commit author name:"));fields.add(name);fields.add(new JLabel("Commit author email:"));fields.add(email);if(JOptionPane.showConfirmDialog(window.getFrame(),fields,"Git identity for this project",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;if(name.getText().trim().isEmpty()||email.getText().trim().isEmpty()){JOptionPane.showMessageDialog(window.getFrame(),"Name and email are required.");return;}SourceControlUi.background(window.getFrame(),()->{GitProjectRepository repo=new GitProjectRepository(project.getLocation().toPath());repo.run("config","--local","user.name",name.getText().trim());repo.run("config","--local","user.email",email.getText().trim());return "Git identity saved for this project.";});}
      else if(mode.equals("connect")){String url=JOptionPane.showInputDialog(window.getFrame(),"Existing GitHub repository URL (use an empty repository):");if(url==null)return;SourceControlUi.background(window.getFrame(),()->{GitProjectRepository.connect(project.getLocation().toPath(),url);return "Repository connected. Choose Commit and Push to publish the saved project.";});}
      else SourceControlUi.background(window.getFrame(),()->{if(mode.equals("commit"))return SourceControlUi.commit(project,false)?"Committed and pushed successfully.":null;GitProjectRepository repo=new GitProjectRepository(project.getLocation().toPath());if(mode.equals("push")){repo.push();return "Push completed.";}String text=mode.equals("history")?repo.history():repo.status();SourceControlUi.onUi(()->{JTextArea area=new JTextArea(text.isEmpty()?"Working tree is clean.":text,22,85);area.setEditable(false);JOptionPane.showMessageDialog(window.getFrame(),new JScrollPane(area),"Git "+mode,JOptionPane.PLAIN_MESSAGE);return null;});return null;});});}
   public void dispose(){current.removeListener(listener);super.dispose();}
}
