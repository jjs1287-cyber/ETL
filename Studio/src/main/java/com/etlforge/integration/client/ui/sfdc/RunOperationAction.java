package com.etlforge.integration.client.ui.sfdc;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.callback.Receiver;

public final class RunOperationAction extends AbstractAction {
   private final Operation op;
   private final OperationMonitorSite monitorSite;
   private final OperationRunner runner;
   private final IntegrationEntityPage page;

   public RunOperationAction(String var1, Operation var2, OperationRunner var3, IntegrationEntityPage var4, OperationMonitorSite var5) {
      super("&Run " + var1, ClientIcons.RUN_OPERATION_16);
      this.op = var2;
      this.runner = var3;
      this.page = var4;
      this.monitorSite = var5;
      this.putValue("KongaMediumIconKey", ClientIcons.RUN_OPERATION_24);
      this.putValue("SwingLargeIconKey", ClientIcons.RUN_OPERATION_24);
      this.putValue("ShortDescription", "Run this " + var1);
      this.setEnabled(var3 != null && var2 != null);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            boolean var1x = RunOperationAction.this.savePageIfDirty();
            if (var1x) {
               RunOperationAction.this.runner.run(RunOperationAction.this.op, 1, RunOperationAction.this.new Callback());
            }
         }
      });
   }

   private boolean savePageIfDirty() {
      if (this.page.isDirty()) {
         try {
            this.page.save();
         } catch (SaveEditorException var2) {
            ErrorLog.attention(this.getClass(), null, var2);
            return false;
         }
      }

      return true;
   }

   private final class Callback implements Receiver<Long> {
      private Callback() {
      }

      public void handle(final Long var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               RunOperationAction.this.monitorSite.openActivityMonitor(var1);
            }
         });
      }
   }
}
