package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.database.ui.DatabaseColumnsTable;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;

final class TestManualSqlStatementAction extends AbstractAction {
   private final DatabaseLocation dbLocation;
   private final SourceId sourceId;
   private final Supplier<String> sqlSupplier;
   private WaitService waitService = WaitService.NONE;
   private Receiver<ManualSqlStatement> externalCallback;

   public TestManualSqlStatementAction(DatabaseLocation var1, SourceId var2, Supplier<String> var3) {
      super("Check Synta&x...");
      this.dbLocation = var1;
      this.sourceId = var2;
      this.sqlSupplier = var3;
      this.externalCallback = NoReceiver.create();
      this.putValue("ShortDescription", "Check the syntax of the entered SQL statement, and view information about the resulting columns");
      this.putValue("AcceleratorKey", KeyUtils.F5);
      this.setEnabled(!((String)var3.get()).isEmpty());
   }

   public void setCallback(Receiver<ManualSqlStatement> var1) {
      this.externalCallback = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            TestManualSqlStatementAction.this.setEnabled(false);
            TestManualSqlStatementAction.this.submitJob();
         }
      });
   }

   private void submitJob() {
      String var1 = this.sqlSupplier.get();
      if (StringUtils.isEmpty(var1)) {
         Alert.error("Please enter a SQL statement to test.", "No SQL Statement");
      } else {
         ConnectionParams var2 = this.dbLocation.getConnectionParams();
         GetManualSqlStatementDetailsJob var3 = new GetManualSqlStatementDetailsJob(var1, var2, this.sourceId);
         var3.setReceiver(new TestManualSqlStatementAction.Callback());
         var3.submit(this.waitService, 0);
      }
   }

   private final class Callback implements Receiver<ManualSqlStatement> {
      private Callback() {
      }

      public void handle(ManualSqlStatement var1) {
         ActionUtils.setEnabledSafely(TestManualSqlStatementAction.this, true);
         TestManualSqlStatementAction.this.externalCallback.handle(var1);
         if (var1 != null) {
            this.displayColumnInfo(var1);
         }
      }

      private void displayColumnInfo(final ManualSqlStatement var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DatabaseColumnsTable var1x = new DatabaseColumnsTable(var1);
               UiProviderDialogDisplayer.display(UiUtils.getActiveFrame(), var1x, "Column Information");
            }
         });
      }
   }
}
