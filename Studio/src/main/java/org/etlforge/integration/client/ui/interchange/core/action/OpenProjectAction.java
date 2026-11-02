package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.component.launcher.InterchangeLauncher;
import org.etlforge.integration.client.ui.interchange.component.launcher.ProjectLauncherViewer;

public final class OpenProjectAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.Open";
   private final InterchangeLauncher launcher;

   public OpenProjectAction(InterchangeLauncher var1) {
      super("action.ic.Open", InterchangeActionDictionary.DICTIONARY);
      this.launcher = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OpenProjectAction.this.executeImpl();
         }
      });
   }

   private void executeImpl() {
      ProjectLauncherViewer var1 = new ProjectLauncherViewer(this.launcher);
      var1.open();
   }
}
