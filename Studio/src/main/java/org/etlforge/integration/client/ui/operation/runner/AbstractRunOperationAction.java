package org.etlforge.integration.client.ui.operation.runner;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.List;
import org.etlforge.application.DebugMode;
import org.etlforge.application.ui.widget.TextFieldInput;
import org.etlforge.application.ui.widget.TextFieldInputValidator;
import org.etlforge.application.ui.window.ApplicationKeyLogger;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractActiveObjectAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.operation.OperationMonitorDialog;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.system.callback.Receiver;

public abstract class AbstractRunOperationAction extends AbstractActiveObjectAction {
   private final ApplicationWindow appWin;
   private final OperationRunnerFactory factory;
   private Optional<OperationMonitorApis> monitorApis = Optional.absent();
   private boolean switchToEditor = true;

   public AbstractRunOperationAction(String var1, String var2, ApplicationWindow var3, OperationRunnerFactory var4) {
      super(var1, var3.getActiveObjectService(), var2, ClientIcons.RUN_OPERATION_16);
      this.putValue("KongaMediumIconKey", ClientIcons.RUN_OPERATION_24);
      this.appWin = var3;
      this.factory = var4;
   }

   public void setSwitchToEditor(boolean var1) {
      this.switchToEditor = var1;
   }

   public final void setMonitorApis(OperationMonitorApis var1) {
      this.monitorApis = Optional.of(var1);
   }

   protected final Editor getEditorFor(IntegrationEntity var1) {
      return UiCompanion.of(var1).findOpenPage();
   }

   @Override
   protected final void updateEnabledState(List<? extends Object> var1) {
      this.setEnabled(var1 != null && var1.size() == 1);
   }

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractRunOperationAction.this.submitOperation();
         }
      });
   }

   private void submitOperation() {
      Operation var1 = this.getOperationToRun();
      if (var1 != null) {
         int var2 = this.getTimesToRun();
         if (var2 > 0) {
            OperationRunner var3 = this.factory.newRunner();
            Receiver var4 = this.createCallback(var1);
            var3.run(var1, var2, var4);
         }
      }
   }

   private Receiver<Long> createCallback(Operation var1) {
      if (this.monitorApis.isPresent()) {
         OperationMonitor var2 = new OperationMonitor(this.monitorApis.get());
         return new AbstractRunOperationAction.MonitorLauncher(var1, var2);
      } else {
         return OperationRunner.NO_CALLBACK;
      }
   }

   protected abstract Operation getOperationToRun();

   private int getTimesToRun() {
      return DebugMode.IS_ENABLED && this.isDebugTriggerOn() ? this.getTimesToRunImpl() : 1;
   }

   private int getTimesToRunImpl() {
      try {
         TextFieldInput var1 = new TextFieldInput("How many times?", "How many times do you want to run the operation?");
         var1.setInitialValue("1");
         var1.setSelectAllWhenFocused(true);
         var1.setValidator(TextFieldInputValidator.INTEGER);
         String var2 = var1.showInDialog(this.appWin.getFrame());
         return Math.max(1, Integer.valueOf(var2));
      } catch (HeadlessException var3) {
         return -1;
      } catch (NumberFormatException var4) {
         return -1;
      }
   }

   private boolean isDebugTriggerOn() {
      return ApplicationKeyLogger.getLogger().isCtrlShiftDown();
   }

   protected abstract Editor getPageForObjectToRun();

   private class MonitorLauncher implements Receiver<Long> {
      private final OperationMonitor monitor;
      private final Operation op;

      public MonitorLauncher(Operation var2, OperationMonitor var3) {
         this.op = var2;
         this.monitor = var3;
      }

      public void handle(final Long var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MonitorLauncher.this.openMonitor(var1);
            }
         });
      }

      private void openMonitor(long var1) {
         OperationMonitorSite var3 = this.getMonitorSite();
         if (var3 != null) {
            var3.openActivityMonitor(var1);
         }
      }

      private OperationMonitorSite getMonitorSite() {
         if (AbstractRunOperationAction.this.switchToEditor) {
            Editor var1 = AbstractRunOperationAction.this.getPageForObjectToRun();
            if (var1 instanceof OperationMonitorSite) {
               AbstractRunOperationAction.this.appWin.getEditorService().openEditor(var1);
               return (OperationMonitorSite)var1;
            }
         }

         return new OperationMonitorDialog(this.op, this.monitor, AbstractRunOperationAction.this.appWin.getFrame());
      }
   }
}
