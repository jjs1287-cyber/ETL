package org.etlforge.integration.client.ui.structure.editor.db;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProviderFactory;
import org.etlforge.integration.client.database.serverapi.GetTableInfoCallback;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

final class GetManualSqlStatementDetailsJob extends UiJob {
   private final String sql;
   private final ConnectionParams connectionParams;
   private final SourceId sourceId;
   private final GetManualSqlStatementDetailsJob.Callback callback;
   private ManualSqlStatement details;
   private Receiver<ManualSqlStatement> receiver;

   public GetManualSqlStatementDetailsJob(String var1, ConnectionParams var2, SourceId var3) {
      super("Testing the SQL statement");
      this.sql = var1;
      this.connectionParams = var2;
      this.sourceId = var3;
      this.callback = new GetManualSqlStatementDetailsJob.Callback();
      this.receiver = NoReceiver.create();
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      if (this.isSupportedLocation()) {
         DatabaseInfoProvider var1 = DatabaseInfoProviderFactory.createProvider(this.connectionParams);
         DatabaseObject[] var2 = new DatabaseObject[]{new ManualSqlStatement(this.sql)};
         var1.getTableInfo(this.connectionParams, this.sourceId, null, var2, this.callback);
      }
   }

   private boolean isSupportedLocation() {
      DriverDescriptor var1 = this.connectionParams.getDriver();
      boolean var2 = var1 != null && var1.isManualSqlStatementSupported();
      if (!var2) {
         String var3 = "Manual SQL statements are not supported for the selected database. Have you just changed its driver?";
         this.callback.caught(new Exception(var3));
      }

      return var2;
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   public ManualSqlStatement getDetails() {
      this.callback.waitFor();
      return this.details;
   }

   public void setReceiver(Receiver<ManualSqlStatement> var1) {
      this.receiver = var1;
   }

   private final class Callback implements GetTableInfoCallback {
      private final CountDownLatch latch = new CountDownLatch(1);

      private Callback() {
      }

      public void waitFor() {
         try {
            this.latch.await();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }
      }

      @Override
      public void tableInfoRetrieved(DatabaseObjectContainer[] var1) {
         if (!GetManualSqlStatementDetailsJob.this.isCancelled()) {
            for (DatabaseObjectContainer var5 : var1) {
               if (var5.getObjectType() == DatabaseObjectType.MANUAL_SQL) {
                  List var6 = var5.getAllObjects();
                  if (var6.size() == 1) {
                     this.setDetails((ManualSqlStatement)var6.get(0));
                     return;
                  }
               }
            }
         }
      }

      private void setDetails(ManualSqlStatement var1) {
         GetManualSqlStatementDetailsJob.this.details = var1;
         this.latch.countDown();
         GetManualSqlStatementDetailsJob.this.receiver.handle(var1);
      }

      @Override
      public void cancelled() {
         this.setDetails(null);
      }

      @Override
      public void caught(Throwable var1) {
         if (!GetManualSqlStatementDetailsJob.this.isCancelled()) {
            this.setDetails(null);
            ErrorLog.attention(GetManualSqlStatementDetailsJob.class, "An error occurred. Make sure that the SQL statement is valid.", var1);
         }
      }
   }
}
