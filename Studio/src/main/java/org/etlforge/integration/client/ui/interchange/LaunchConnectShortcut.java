package org.etlforge.integration.client.ui.interchange;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowKeyDispatcher;

final class LaunchConnectShortcut implements GlobalShortcut {
   private final Action openConsoleAction;

   public LaunchConnectShortcut(Action var1) {
      this.openConsoleAction = var1;
   }

   @Override
   public void install(ApplicationWindow var1) {
      KeyStroke var2 = ApplicationUi.requestGlobalKeyStroke(KeyStroke.getKeyStroke("ctrl shift pressed C"), LaunchConnectShortcut.class);
      if (var2 != null) {
         var1.addGlobalKeyDispatcher(var2, new LaunchConnectShortcut.Dispatcher(this.openConsoleAction));
      }
   }

   private static final class Dispatcher implements WindowKeyDispatcher {
      private final Action action;

      public Dispatcher(Action var1) {
         this.action = var1;
      }

      @Override
      public boolean dispatch(KeyEvent var1, ApplicationWindow var2) {
         this.triggerAction();
         return true;
      }

      private void triggerAction() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Dispatcher.this.action.actionPerformed(null);
            }
         });
      }

      @Override
      public String getId() {
         return this.getClass().getName();
      }
   }
}
