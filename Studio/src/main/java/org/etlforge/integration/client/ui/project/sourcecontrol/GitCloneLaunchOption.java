package org.etlforge.integration.client.ui.project.sourcecontrol;

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
   private void start(){if(busy)return;final String remote=url.getText(),ref=branch.getText(),local=folder.getText();busy=true;setEditable(false);status.setText("Cloning repository...");new SwingWorker<IntegrationProject,Void>(){protected IntegrationProject doInBackground()throws Exception{Path root=GitProjectRepository.cloneGitHub(remote,ref,Paths.get(local));Path file=GitProjectRepository.projectFile(root);org.etlforge.integration.data.project.IntegrationProjectLock.acquire();try{return manager.loadProject(file.getFileName().toString().replaceFirst("\\.jip$",""),root.toFile(),new LoadingIssuesPresenter());}finally{org.etlforge.integration.data.project.IntegrationProjectLock.release();}}protected void done(){busy=false;setEditable(true);try{project=get();status.setText("Clone complete.");launchProject();}catch(Exception ex){Throwable cause=ex.getCause()==null?ex:ex.getCause();status.setText("Clone/open failed. The local checkout is retained if it was created.");JOptionPane.showMessageDialog(panel,cause.getMessage(),"Source Control",JOptionPane.ERROR_MESSAGE);}}}.execute();}
   private void setEditable(boolean value){url.setEnabled(value);branch.setEnabled(value);folder.setEnabled(value);clone.setEnabled(value);browse.setEnabled(value);}
   public void activated(){url.requestFocusInWindow();}
   public void requestFocus(){url.requestFocusInWindow();}
   public void decorate(UiCanvas canvas){canvas.display(panel);}
   public IntegrationProject getProject(){return project;}
}
