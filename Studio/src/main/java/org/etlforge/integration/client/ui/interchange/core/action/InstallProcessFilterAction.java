package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.interchange.ProcessFilterInstaller;
import org.etlforge.integration.data.entity.Operation;

public final class InstallProcessFilterAction extends AbstractSingleActiveObjectAction<Operation> {
   public static final String ID = "action.ic.InstallProcessFilter";
   private final ProcessFilterInstaller installer;

   public InstallProcessFilterAction(ApplicationWindow var1, ProcessFilterInstaller var2) {
      super(var1.getActiveObjectService(), "action.ic.InstallProcessFilter", InterchangeActionDictionary.DICTIONARY, Operation.class);
      this.installer = var2;
   }

   protected void runOnObject(final Operation var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            InstallProcessFilterAction.this.installFilter(var1);
         }
      });
   }

   public void installFilter(Operation var1) {
      this.installer.installFilter(var1);
   }
}
