package org.etlforge.integration.client.ui.project.migration;

import java.awt.event.ActionEvent;
import javax.swing.*;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.*;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;

public final class MigrateProjectAction extends AbstractApplicationAction {
   private final ApplicationWindow window;
   private final CurrentProjectService current;
   private final ProjectManager manager;
   private final SaveAllGate save;
   private final CurrentProjectServiceListener listener=new CurrentProjectServiceListener.Adapter() {
      public void projectOpened(CurrentProjectEvent event) { setEnabledSafely(true); }
      public void projectClosed(CurrentProjectEvent event) { setEnabledSafely(false); }
   };
   public MigrateProjectAction(ApplicationWindow window,CurrentProjectService current,ProjectManager manager,SaveAllGate save) {
      super("action.ic.Migrate","Migrate...",null);
      this.window=window;this.current=current;this.manager=manager;this.save=save;
      current.addListener(listener);setEnabled(current.getManagedProject()!=null);
   }
   public void execute(ActionEvent event) {
      save.saveAndRun(()->{if(current.getManagedProject()==null)return;JDialog dialog=new JDialog(window.getFrame(),"Migrate Project",true);MigrateProjectPanel panel=new MigrateProjectPanel(manager,current.getManagedProject());dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);dialog.addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent event){if(!panel.isBusy())dialog.dispose();}});dialog.setContentPane(panel);dialog.setSize(900,570);dialog.setLocationRelativeTo(window.getFrame());dialog.setVisible(true);});
   }
   public void dispose() { current.removeListener(listener);super.dispose(); }
}
