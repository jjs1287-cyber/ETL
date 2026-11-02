package org.etlforge.integration.client.ui.core.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractApplicationWindowAction;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.ui.info.KnownServersViewFactory;

public final class ShowKnownServersAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.core.KnownServers";
   private final ShowKnownServersAction.ViewDisplayer displayer;

   public ShowKnownServersAction(ApplicationWindow var1, ServerManager var2) {
      super(var1, "action.core.KnownServers", ClientWindowActionDictionary.DICTIONARY);
      this.displayer = new ShowKnownServersAction.ViewDisplayer(var2);
      this.setEnabled(true);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowKnownServersAction.this.displayer.display();
         }
      });
   }

   private class ViewDisplayer {
      private final ServerManager serverManager;

      public ViewDisplayer(ServerManager var2) {
         this.serverManager = var2;
      }

      public void display() {
         KnownServersViewFactory var1 = new KnownServersViewFactory(this.serverManager);
         ShowKnownServersAction.this.getWindow().displayContent(var1, ApplicationWindow.SOUTH, null, null);
      }
   }
}
