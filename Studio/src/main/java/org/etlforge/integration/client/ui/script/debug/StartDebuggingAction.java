package org.etlforge.integration.client.ui.script.debug;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import org.etlforge.integration.client.ui.debug.DebugIcons;

public final class StartDebuggingAction extends AbstractAction {
   private final ScriptDebugModel model;

   public StartDebuggingAction(ScriptDebugModel var1) {
      super("", DebugIcons.START_16);
      this.model = var1;
      this.putValue("ShortDescription", "Start testing");
      this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("pressed F11"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Debugger var1x = new Debugger(StartDebuggingAction.this.model);
            var1x.start();
         }
      });
   }
}
