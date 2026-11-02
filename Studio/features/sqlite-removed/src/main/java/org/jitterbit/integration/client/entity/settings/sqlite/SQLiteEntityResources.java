package org.jitterbit.integration.client.entity.settings.sqlite;

import com.google.common.base.Preconditions;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.lang.StringUtils;
import org.jitterbit.application.errorlog.ErrorLog;
import org.jitterbit.db.KongaDbUtils;
import org.jitterbit.integration.client.entity.settings.CloseableEntityResources;
import org.jitterbit.integration.client.entity.settings.EntityResources;
import org.jitterbit.integration.client.entity.settings.EntitySettings;
import org.jitterbit.integration.client.entity.settings.FileResource;
import org.jitterbit.integration.data.entity.IntegrationEntity;
import org.jitterbit.integration.data.entity.IntegrationEntityId;
import org.jitterbit.io.Folder;
import org.jitterbit.util.persist.sqlite.PersistorValuesTable;
import org.jitterbit.util.persist.sqlite.PersistorsTable;

public final class SQLiteEntityResources implements CloseableEntityResources {
   public static final String SQL_LITE_SETTINGS_DB = "settings.db";
   public static final String SQL_LITE_FILE_RESOURCE_DB = "resources.db";
   private final EntitySettings settings;
   private final FileResourceManager fileResourceManager;
   private volatile Connection settingsConnection;
   private volatile Connection resourcesConnection;

   public static SQLiteEntityResources inMemory() {
      return new SQLiteEntityResources(null, null);
   }

   public static SQLiteEntityResources inFolder(Folder var0) {
      var0.createOnDisk();
      File var1 = var0.getFile("settings.db");
      File var2 = var0.getFile("resources.db");
      return new SQLiteEntityResources(var1, var2);
   }

   public SQLiteEntityResources(File var1, File var2) {
      ReentrantReadWriteLock var3 = new ReentrantReadWriteLock();
      if (this.isSQLiteAvailable()) {
         this.settings = this.createSettings(var1, var3);
         this.fileResourceManager = this.createFileResourceManager(var2, var1, var3);
      } else {
         this.settings = new FallbackEntitySettings();
         this.fileResourceManager = null;
      }
   }

   private boolean isSQLiteAvailable() {
      try {
         Class.forName("org.sqlite.JDBC");
         return true;
      } catch (ClassNotFoundException var2) {
         ErrorLog.log(this.getClass(), "Configuration error: The SQLite library is not on the classpath.", var2);
         return false;
      }
   }

   private EntitySettings createSettings(File var1, ReadWriteLock var2) {
      SQLiteEntityResources.EntitySettingsFactory var3 = new SQLiteEntityResources.EntitySettingsFactory(var1, var2);
      EntitySettings var4 = var3.createSettings();
      this.settingsConnection = var3.connection;
      return var4;
   }

   private static String getUrl(File var0) {
      String var1 = "jdbc:sqlite:";
      if (var0 != null) {
         var1 = var1 + var0.getAbsolutePath();
      }

      return var1;
   }

   private FileResourceManager createFileResourceManager(File var1, File var2, ReadWriteLock var3) {
      if (isSameStorageMedia(var2, var1)) {
         SQLiteEntityResources.SharedFileResourcesFactory var6 = new SQLiteEntityResources.SharedFileResourcesFactory(this.settingsConnection, var3);
         return var6.createManager();
      } else {
         SQLiteEntityResources.SeparateFileResourcesFactory var4 = new SQLiteEntityResources.SeparateFileResourcesFactory(var1, new ReentrantReadWriteLock());
         FileResourceManager var5 = var4.createManager();
         this.resourcesConnection = var4.connection;
         return var5;
      }
   }

   private static boolean isSameStorageMedia(File var0, File var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   @Override
   public EntitySettings getSettings() {
      return this.settings;
   }

   @Override
   public FileResource getFileResource(IntegrationEntity var1, String var2) {
      Preconditions.checkNotNull(var1, "entity");
      Preconditions.checkArgument(!StringUtils.isBlank(var2), "name may not be null or blank");
      Preconditions.checkArgument(!var2.startsWith(".konga."), "name may not start with \".konga.\"");
      return this.fileResourceManager == null ? new FallbackFileResource() : this.fileResourceManager.getResource(var1, var2);
   }

   public void importFileResource(IntegrationEntityId var1, String var2, String var3) {
      if (this.fileResourceManager != null) {
         this.fileResourceManager.importResource(var1, var2, var3);
      }
   }

   @Override
   public void export(IntegrationEntity var1, EntityResources var2) {
      Preconditions.checkNotNull(var2, "target");
      if (var2 != this) {
         if (var2 instanceof SQLiteEntityResources) {
            this.exportImpl(var1, (SQLiteEntityResources)var2);
         } else {
            ErrorLog.log(
               this.getClass(), null, new Exception("I do not know how to export to an EntityResources instance of type " + var2.getClass().getName())
            );
         }
      }
   }

   private final void exportImpl(IntegrationEntity var1, SQLiteEntityResources var2) {
      if (this.settings instanceof SQLiteEntitySettings && var2.settings instanceof SQLiteEntitySettings) {
         ((SQLiteEntitySettings)this.settings).exportTo(var1, (SQLiteEntitySettings)var2.settings);
      }

      if (this.fileResourceManager != null && var2.fileResourceManager != null) {
         this.fileResourceManager.exportTo(var1, var2.fileResourceManager);
      }
   }

   @Override
   public void close(Collection<IntegrationEntity> var1) {
      for (IntegrationEntity var3 : var1) {
         this.settings.deleteSettings(var3);
      }

      if (this.fileResourceManager != null) {
         this.fileResourceManager.cleanup();
      }

      KongaDbUtils.close(this.settingsConnection);
      KongaDbUtils.close(this.resourcesConnection);
   }

   public void dumpFileResourcesToConsole() {
      if (this.fileResourceManager != null) {
         this.fileResourceManager.dumpResourcesToConsole();
      }
   }

   private static class EntitySettingsFactory {
      private final File file;
      private final ReadWriteLock lock;
      private Connection connection;

      public EntitySettingsFactory(File var1, ReadWriteLock var2) {
         this.file = var1;
         this.lock = var2;
      }

      public EntitySettings createSettings() {
         try {
            String var1 = SQLiteEntityResources.getUrl(this.file);
            boolean var2 = false;
            this.connection = DriverManager.getConnection(var1);

            try {
               PersistorsTable var3 = new PersistorsTable(this.connection);
               if (var3.exists() || var3.create()) {
                  PersistorValuesTable var4 = new PersistorValuesTable(this.connection);
                  if (var4.exists() || var4.create()) {
                     var2 = true;
                     return new SQLiteEntitySettings(var3, var4, this.lock);
                  }
               }
            } finally {
               if (!var2) {
                  KongaDbUtils.close(this.connection);
               }
            }
         } catch (SQLException var10) {
            ErrorLog.log(SQLiteEntityResources.class, "Could not create the SQLite Entity Settings DB: " + var10.getMessage(), var10);
         }

         this.connection = null;
         return new FallbackEntitySettings();
      }
   }

   private static class SeparateFileResourcesFactory {
      private final File file;
      private final ReadWriteLock lock;
      public Connection connection;

      public SeparateFileResourcesFactory(File var1, ReadWriteLock var2) {
         this.file = var1;
         this.lock = var2;
      }

      public FileResourceManager createManager() {
         try {
            String var1 = SQLiteEntityResources.getUrl(this.file);
            boolean var2 = false;
            this.connection = DriverManager.getConnection(var1);

            try {
               FileResourcesTable var3 = new FileResourcesTable(this.connection);
               if (var3.exists() || var3.create()) {
                  var2 = true;
                  return new FileResourceManager(var3, this.lock);
               }
            } finally {
               if (!var2) {
                  KongaDbUtils.close(this.connection);
               }
            }
         } catch (SQLException var9) {
            ErrorLog.log(SQLiteEntityResources.class, "Could not create the SQLite File Resources DB: " + var9.getMessage(), var9);
         }

         this.connection = null;
         return null;
      }
   }

   private static class SharedFileResourcesFactory {
      private final Connection connection;
      private final ReadWriteLock lock;

      public SharedFileResourcesFactory(Connection var1, ReadWriteLock var2) {
         this.connection = var1;
         this.lock = var2;
      }

      public FileResourceManager createManager() {
         if (this.connection == null) {
            return null;
         }

         boolean var1 = true;
         FileResourcesTable var2 = new FileResourcesTable(this.connection);
         if (!var2.exists()) {
            var1 = var2.create();
         }

         return var1 ? new FileResourceManager(var2, this.lock) : null;
      }
   }
}
