package org.etlforge.integration.client.database;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProviderFactory;
import org.etlforge.integration.client.database.serverapi.GetTableInfoCallback;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.DatabaseTable;
import org.etlforge.integration.database.info.InformixUtils;

public final class DbColumnInfoCreator {
   public static final int SUCCESS = 0;
   public static final int CANCELLED = 1;
   public static final int EXCEPTION = 2;
   public static final int WAITING = 3;
   public static final int UNCAUGHT_EXCEPTION = 4;
   private final ConnectionParams connectionParams;

   public DbColumnInfoCreator(ConnectionParams var1) {
      this.connectionParams = var1;
   }

   public DbColumnInfoCreator.ColumnInfo createColumnInfo(String[][] var1, SourceId var2, TargetId var3, boolean var4, boolean var5) {
      DatabaseObject[] var6 = new DatabaseObject[var1.length];

      for (int var7 = 0; var7 < var1.length; var7++) {
         var6[var7] = new DatabaseTable(var1[var7][1], var1[var7][0]);
      }

      return this.createColumnInfo(var6, var2, var3, var4, var5);
   }

   public DbColumnInfoCreator.ColumnInfo createColumnInfo(DatabaseObject[] var1, SourceId var2, TargetId var3, boolean var4, boolean var5) {
      DbColumnInfoCreator.ColumnInfo var6 = new DbColumnInfoCreator.ColumnInfo(var4, var5);
      var6.get(var1, var2, var3);
      return var6;
   }

   public final class ColumnInfo implements GetTableInfoCallback {
      private final EnumSet<DatabaseObjectType> dbTypeSet = EnumSet.noneOf(DatabaseObjectType.class);
      private DatabaseObjectContainer[] container_array;
      private CountDownLatch latch;
      private volatile int status;
      private Throwable throwable;

      private ColumnInfo(boolean var2, boolean var3) {
         if (var2) {
            this.dbTypeSet.add(DatabaseObjectType.TABLE);
            this.dbTypeSet.add(DatabaseObjectType.SYSTEM_TABLE);
         }

         if (var3) {
            this.dbTypeSet.add(DatabaseObjectType.VIEW);
         }
      }

      public void removeInformixRowIdColumn() {
         for (DatabaseObjectContainer var4 : this.container_array) {
            for (Object __etlItem67 : var4.getAllObjects()) {
               DatabaseObject var6 = (DatabaseObject) __etlItem67;
               InformixUtils.removeRowIdColumn(var6);
            }
         }
      }

      public List<DatabaseColumn> getTableColumnInfo(String var1, boolean var2) {
         String var3 = var1;
         String var4 = "";
         if (!var2) {
            String[] var5 = var1.split(":");
            if (var5.length == 2) {
               var4 = var5[0];
               var3 = var5[1];
            }
         }

         for (DatabaseObjectContainer var8 : this.container_array) {
            for (Object __etlItem85 : var8.getAllObjects()) {
               DatabaseObject var10 = (DatabaseObject) __etlItem85;
               if (var10.getName().equals(var3) && (var4.length() == 0 || var10.getSchema().equals(var4))) {
                  return var10.getAllColumns();
               }
            }
         }

         return null;
      }

      public DatabaseObjectContainer[] get(DatabaseObject[] var1, SourceId var2, TargetId var3) {
         this.latch = new CountDownLatch(1);
         this.status = 3;
         DatabaseInfoProvider var4 = DatabaseInfoProviderFactory.createProvider(DbColumnInfoCreator.this.connectionParams);
         var4.getTableInfo(DbColumnInfoCreator.this.connectionParams, var2, var3, var1, this);

         try {
            this.latch.await();
         } catch (InterruptedException var6) {
            Thread.currentThread().interrupt();
            var6.printStackTrace();
         }

         return this.container_array;
      }

      @Override
      public void cancelled() {
         this.container_array = null;
         this.status = 1;
         this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         this.container_array = null;
         this.status = 2;
         this.throwable = var1;
         this.latch.countDown();
      }

      public int getStatus() {
         return this.status;
      }

      public Throwable getException() {
         return this.throwable;
      }

      @Override
      public void tableInfoRetrieved(DatabaseObjectContainer[] var1) {
         this.container_array = var1;
         this.status = 0;
         this.latch.countDown();
      }
   }
}
