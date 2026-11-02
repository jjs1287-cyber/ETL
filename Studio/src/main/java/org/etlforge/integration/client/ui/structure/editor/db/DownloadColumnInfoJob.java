package org.etlforge.integration.client.ui.structure.editor.db;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProviderFactory;
import org.etlforge.integration.client.database.serverapi.GetTableInfoCallback;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;

final class DownloadColumnInfoJob extends UiJob {
   private final DatabaseObject[] emptyObjects;
   private final DatabaseLocation dbLocation;
   private final SourceId sourceId;
   private final TargetId targetId;
   private final GetTableInfoCallback callback;
   private final CountDownLatch latch;
   private DatabaseObjectContainer[] downloadedData;

   public DownloadColumnInfoJob(TableSelectionModel var1, DatabaseObject[] var2) {
      super("Download column information for the selected tables and views");
      this.emptyObjects = var2;
      this.dbLocation = var1.getDbLocation();
      this.sourceId = var1.getSourceId();
      this.targetId = var1.getTargetId();
      this.callback = new DownloadColumnInfoJob.Callback();
      this.latch = new CountDownLatch(1);
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   public DatabaseObject[] getDownloadedTables() {
      try {
         this.latch.await();
         if (this.downloadedData != null) {
            List var1 = getTablesFromContainers(this.downloadedData);
            return (DatabaseObject[]) var1.toArray(new DatabaseObject[var1.size()]);
         }
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         var2.printStackTrace();
      }

      return null;
   }

   private static List<DatabaseObject> getTablesFromContainers(DatabaseObjectContainer[] var0) {
      ArrayList var1 = Lists.newArrayList();

      for (DatabaseObjectContainer var5 : var0) {
         var1.addAll(var5.getAllObjects());
      }

      return var1;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Asking the server for column information");
      ConnectionParams var1 = this.dbLocation.getConnectionParams();
      DatabaseInfoProvider var2 = DatabaseInfoProviderFactory.createProvider(var1);
      var2.getTableInfo(var1, this.sourceId, this.targetId, this.emptyObjects, this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private final class Callback implements GetTableInfoCallback {
      private Callback() {
      }

      @Override
      public void tableInfoRetrieved(DatabaseObjectContainer[] var1) {
         DownloadColumnInfoJob.this.downloadedData = var1;
         DownloadColumnInfoJob.this.latch.countDown();
      }

      @Override
      public void cancelled() {
         DownloadColumnInfoJob.this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         DownloadColumnInfoJob.this.latch.countDown();
         ErrorLog.attention(DownloadAvailableObjectsJob.class, "An error occurred when downloading the column information: " + var1.getMessage(), var1);
      }
   }
}
