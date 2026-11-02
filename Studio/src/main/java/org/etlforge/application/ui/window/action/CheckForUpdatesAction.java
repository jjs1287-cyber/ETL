package org.etlforge.application.ui.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.update.CheckForUpdatesViewProvider;
import org.etlforge.application.ui.window.ApplicationWindow;

public final class CheckForUpdatesAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.CheckForUpdates";
   private final CheckForUpdatesViewProvider viewProvider;

   public CheckForUpdatesAction(ApplicationWindow var1, CheckForUpdatesViewProvider var2) {
      super(var1, "action.CheckForUpdates", WindowActionDictionary.DICTIONARY);
      this.viewProvider = var2;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CheckForUpdatesAction.this.checkForUpdates();
         }
      });
   }

   private void checkForUpdates() {
      this.viewProvider.openAndCheck(this.getWindow());
   }
}
