package org.etlforge.integration.client.ui.interchange;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;
import org.etlforge.integration.client.project.ProjectEventLogger;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.util.KeyUtils;

final class ProjectEventLogSwitch implements WindowKeyDispatcher, GlobalShortcut {
   private static final KeyStroke KEY_STROKE = KeyUtils.getCtrlShiftDown(112);
   private final ProjectManager projectManager;
   private boolean isOn;

   public ProjectEventLogSwitch(ProjectManager var1) {
      this.projectManager = var1;
   }

   @Override
   public void install(ApplicationWindow var1) {
      KeyStroke var2 = ApplicationUi.requestGlobalKeyStroke(KEY_STROKE, ProjectEventLogSwitch.class);
      if (var2 != null) {
         var1.addGlobalKeyDispatcher(var2, new ProjectEventLogSwitch(this.projectManager));
      }
   }

   @Override
   public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
      this.isOn = !this.isOn;
      if (this.isOn) {
         this.switchOn(var2);
      } else {
         this.switchOff(var2);
      }

      return true;
   }

   private void switchOn(ApplicationWindow var1) {
      ProjectEventLogger var2 = this.projectManager.getProjectEventLogger();
      var2.switchOn();
      this.postMessage(PackageResources.ProjectEventLogSwitch.ON, var1);
   }

   private void switchOff(ApplicationWindow var1) {
      ProjectEventLogger var2 = this.projectManager.getProjectEventLogger();
      var2.switchOff();
      this.postMessage(PackageResources.ProjectEventLogSwitch.OFF, var1);
   }

   @Override
   public String getId() {
      return this.getClass().getName();
   }

   private void postMessage(String var1, ApplicationWindow var2) {
      ApplicationMessageEvent var3 = new ApplicationMessageEvent(this, var1);
      var2.postApplicationMessageEvent(var3);
   }
}
