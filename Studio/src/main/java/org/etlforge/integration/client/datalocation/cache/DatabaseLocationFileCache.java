package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.io.Folder;

final class DatabaseLocationFileCache extends AbstractDataLocationCache<DatabaseLocation> implements DatabaseLocationCache {
   private static final int SIZE = 100;
   private final DatabaseLocationFileCache.ServerNameCache serverCache;
   private final DatabaseLocationFileCache.DatabaseNameCache databaseCache;
   private final LoginNameCacheParticle<DatabaseLocation> loginCache;

   DatabaseLocationFileCache(File var1) {
      super(ID, DatabaseLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.serverCache = new DatabaseLocationFileCache.ServerNameCache(var2);
      this.databaseCache = new DatabaseLocationFileCache.DatabaseNameCache(var2);
      this.loginCache = new LoginNameCacheParticle<>("DatabaseLocationLogin", var2, 100);
      this.addStringCache(this.serverCache);
      this.addStringCache(this.databaseCache);
      this.addStringCache(this.loginCache);
   }

   @Override
   public String[] getServerNames() {
      return this.serverCache.getValues();
   }

   @Override
   public boolean removeServerName(String var1) {
      return this.serverCache.remove(var1);
   }

   @Override
   public String[] getDatabaseNames() {
      return this.databaseCache.getValues();
   }

   @Override
   public boolean removeDatabaseName(String var1) {
      return this.databaseCache.remove(var1);
   }

   @Override
   public String[] getLoginNames() {
      return this.loginCache.getValues();
   }

   @Override
   public boolean removeLoginName(String var1) {
      return this.loginCache.remove(var1);
   }

   private static class DatabaseNameCache extends StringCacheParticle<DatabaseLocation> {
      public DatabaseNameCache(Folder var1) {
         super("DatabaseLocationDatabase", var1.getFile("databases.dat"), 100, false);
      }

      protected String getValueFromObject(DatabaseLocation var1) {
         return var1.getDatabase();
      }
   }

   private static class ServerNameCache extends StringCacheParticle<DatabaseLocation> {
      public ServerNameCache(Folder var1) {
         super("DatabaseLocationServer", var1.getFile("servers.dat"), 100, false);
      }

      protected String getValueFromObject(DatabaseLocation var1) {
         return var1.getServer();
      }
   }
}
