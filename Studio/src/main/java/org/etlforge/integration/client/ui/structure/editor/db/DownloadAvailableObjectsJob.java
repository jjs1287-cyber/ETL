package org.etlforge.integration.client.ui.structure.editor.db;

import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProviderFactory;
import org.etlforge.integration.client.database.serverapi.GetDatabaseObjectCallback;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;

final class DownloadAvailableObjectsJob extends UiJob {
   private final ConnectionParams connectionParams;
   private final SourceId sourceId;
   private final TargetId targetId;
   private TableSearchFilter filter;
   private final GetDatabaseObjectCallback callback;
   private final CountDownLatch latch;
   private DatabaseObjectContainer[] downloadedObjects;
   private BeginEndQuote beginEndQuote;
   private boolean success;

   public DownloadAvailableObjectsJob(TableSelectionModel var1) {
      super("Download a list of available tables and views");
      this.connectionParams = var1.getDbLocation().getConnectionParams();
      this.sourceId = var1.getSourceId();
      this.targetId = var1.getTargetId();
      this.filter = TableSearchFilter.ALL_OBJECTS;
      this.callback = new DownloadAvailableObjectsJob.Callback();
      this.latch = new CountDownLatch(1);
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   public void setFilter(TableSearchFilter var1) {
      this.filter = var1;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage("Asking the server for list of tables and views");
      DatabaseInfoProvider var1 = DatabaseInfoProviderFactory.createProvider(this.connectionParams);
      var1.getDatabaseObjects(this.connectionParams, this.sourceId, this.targetId, this.filter, this.callback);
   }

   public boolean isSuccess() {
      try {
         this.latch.await();
         return this.success;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   public DatabaseObjectContainer[] getDownloadedObjects() {
      return this.downloadedObjects != null ? (DatabaseObjectContainer[])this.downloadedObjects.clone() : null;
   }

   public BeginEndQuote getBeginEndQuote() {
      return this.beginEndQuote;
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private void setDownloadedInfo(DatabaseObjectContainer[] var1) {
      this.success = var1 != null;
      this.downloadedObjects = var1;
      this.latch.countDown();
   }

   private final class Callback implements GetDatabaseObjectCallback {
      private Callback() {
      }

      @Override
      public void cancelled() {
         DownloadAvailableObjectsJob.this.setDownloadedInfo(null);
      }

      @Override
      public void caught(Throwable var1) {
         DownloadAvailableObjectsJob.this.setDownloadedInfo(null);
         ErrorLog.attention(
            DownloadAvailableObjectsJob.class, "An error occurred when downloading the list of available tables and views: " + var1.getMessage(), var1
         );
      }

      @Override
      public void objectsRetrieved(DatabaseObjectContainer[] var1, BeginEndQuote var2) {
         DownloadAvailableObjectsJob.this.setMessage(UiJobMessages.UPDATE_UI);
         DownloadAvailableObjectsJob.this.beginEndQuote = var2;
         DownloadAvailableObjectsJob.this.setDownloadedInfo(var1);
      }
   }
}
