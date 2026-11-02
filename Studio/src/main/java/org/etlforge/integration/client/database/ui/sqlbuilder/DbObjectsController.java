package org.etlforge.integration.client.database.ui.sqlbuilder;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.GetDatabaseObjectCallback;
import org.etlforge.integration.client.database.serverapi.GetTableInfoCallback;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;

public class DbObjectsController {
   private final DatabaseInfoProvider infoProvider;
   private DbObjectsView view;
   private UiJob downloadTablesJob;
   private UiJob downloadColumnsJob;

   public DbObjectsController(DatabaseInfoProvider var1) {
      this.infoProvider = var1;
   }

   public void setView(DbObjectsView var1) {
      this.view = var1;
   }

   public void downloadTables(TableSearchFilter var1) {
      this.cancelDownload();
      this.downloadTablesJob = new DbObjectsController.DownloadTablesJob(this.view, this.infoProvider, var1);
      this.downloadTablesJob.submit();
      this.view.setDownloadingTables(true);
   }

   public void downloadColumns(DatabaseObject var1) {
      this.cancelDownload();
      this.downloadColumnsJob = new DbObjectsController.DownloadColumnsJob(this.view, this.infoProvider, var1);
      this.downloadColumnsJob.submit();
      this.view.setDownloadingColumns(var1, true);
   }

   public void cancelDownload() {
      if (this.downloadTablesJob != null) {
         this.downloadTablesJob.cancel();
         this.downloadTablesJob = null;
      }

      if (this.downloadColumnsJob != null) {
         this.downloadColumnsJob.cancel();
         this.downloadColumnsJob = null;
      }

      this.view.setDownloadingTables(false);
   }

   private static class DownloadColumnsJob extends UiJob {
      private final DbObjectsView view;
      private final DatabaseInfoProvider infoProvider;
      private final DatabaseObject table;
      private final DbObjectsController.DownloadColumnsJob.Callback callback;

      public DownloadColumnsJob(DbObjectsView var1, DatabaseInfoProvider var2, DatabaseObject var3) {
         super("Download columns for \"" + var3.getName() + "\"");
         this.view = var1;
         this.infoProvider = var2;
         this.table = var3;
         this.callback = new DbObjectsController.DownloadColumnsJob.Callback();
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
         this.setMayBeInterrupted(true);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         DbObjectsModel var1 = this.view.getModel();
         DatabaseObject[] var2 = new DatabaseObject[]{this.table};
         this.infoProvider.getTableInfo(this.connectionParams(var1), var1.getSourceId(), var1.getTargetId(), var2, this.callback);
      }

      private ConnectionParams connectionParams(DbObjectsModel var1) {
         return var1.getDatabase().getConnectionParams();
      }

      private class Callback implements GetTableInfoCallback {
         private Callback() {
         }

         @Override
         public void tableInfoRetrieved(DatabaseObjectContainer[] var1) {
            if (!DownloadColumnsJob.this.isCancelled()) {
               DownloadColumnsJob.this.view.setDownloadingColumns(DownloadColumnsJob.this.table, false);
               this.updateModel(var1);
            }
         }

         private void updateModel(DatabaseObjectContainer[] var1) {
            for (DatabaseObjectContainer var5 : var1) {
               if (var5.getObjectType() == DownloadColumnsJob.this.table.getType()) {
                  for (Object __etlItem101 : var5.getAllObjects()) {
                     DatabaseObject var7 = (DatabaseObject) __etlItem101;
                     if (var7.getName().equals(DownloadColumnsJob.this.table.getName())) {
                        DbObjectsModel var8 = DownloadColumnsJob.this.view.getModel();
                        var8.setColumns(DownloadColumnsJob.this.table, var7.getAllColumns());
                     }
                  }
               }
            }
         }

         @Override
         public void caught(Throwable var1) {
            if (!DownloadColumnsJob.this.isCancelled()) {
               DownloadColumnsJob.this.view.setDownloadingColumns(DownloadColumnsJob.this.table, false);
               ErrorLog.attention(DbObjectsController.class, null, var1);
            }
         }

         @Override
         public void cancelled() {
            DownloadColumnsJob.this.view.setDownloadingColumns(DownloadColumnsJob.this.table, false);
         }
      }
   }

   private static class DownloadTablesJob extends UiJob {
      private final DbObjectsView view;
      private final DatabaseInfoProvider infoProvider;
      private final TableSearchFilter filter;
      private final GetDatabaseObjectCallback callback;

      public DownloadTablesJob(DbObjectsView var1, DatabaseInfoProvider var2, TableSearchFilter var3) {
         super("Download Tables and Views");
         this.view = var1;
         this.infoProvider = var2;
         this.filter = var3;
         this.callback = new DbObjectsController.DownloadTablesJob.Callback();
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(this.callback);
         this.setMayBeInterrupted(true);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         DbObjectsModel var1 = this.view.getModel();
         ConnectionParams var2 = var1.getDatabase().getConnectionParams();
         this.infoProvider.getDatabaseObjects(var2, var1.getSourceId(), var1.getTargetId(), this.filter, this.callback);
      }

      private class Callback implements GetDatabaseObjectCallback {
         private Callback() {
         }

         @Override
         public void objectsRetrieved(DatabaseObjectContainer[] var1, BeginEndQuote var2) {
            if (!DownloadTablesJob.this.isCancelled()) {
               DownloadTablesJob.this.setMayBeInterrupted(false);
               DownloadTablesJob.this.view.setDownloadingTables(false);
               this.updateModel(var1, var2);
            }
         }

         private void updateModel(DatabaseObjectContainer[] var1, BeginEndQuote var2) {
            DbObjectsModel var3 = DownloadTablesJob.this.view.getModel();
            ArrayList var4 = Lists.newArrayList();

            for (DatabaseObjectContainer var8 : var1) {
               for (Object __etlItem168 : var8.getAllObjects()) {
                  DatabaseObject var10 = (DatabaseObject) __etlItem168;
                  var4.add(var10);
               }
            }

            var3.setTables(var4);
            var3.setBeginEndQuotes(var2);
         }

         @Override
         public void caught(Throwable var1) {
            if (!DownloadTablesJob.this.isCancelled()) {
               DownloadTablesJob.this.view.setDownloadingTables(false);
               ErrorLog.attention(DbObjectsController.class, null, var1);
            }
         }

         @Override
         public void cancelled() {
            DownloadTablesJob.this.view.setDownloadingTables(false);
         }
      }
   }
}
