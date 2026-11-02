package org.etlforge.integration.client.datalocation.cache;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.io.Folder;

final class LdapLocationFileCache extends AbstractDataLocationCache<LdapLocation> implements LdapLocationCache {
   private static final int SIZE = 100;
   private final LdapLocationFileCache.ServerNameCache serverCache;
   private final LdapLocationFileCache.BaseNameCache baseNameCache;
   private final LoginNameCacheParticle<LdapLocation> loginCache;

   LdapLocationFileCache(File var1) {
      super(ID, LdapLocation.class);
      Preconditions.checkArgument(var1.isDirectory(), "location must be a directory");
      Folder var2 = this.createParent(var1);
      this.serverCache = new LdapLocationFileCache.ServerNameCache(var2);
      this.baseNameCache = new LdapLocationFileCache.BaseNameCache(var2);
      this.loginCache = new LoginNameCacheParticle<>("LdapLocationLogin", var2, 100);
      this.addStringCache(this.serverCache);
      this.addStringCache(this.baseNameCache);
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
   public String[] getBaseNames() {
      return this.baseNameCache.getValues();
   }

   @Override
   public boolean removeBaseName(String var1) {
      return this.baseNameCache.remove(var1);
   }

   @Override
   public String[] getLoginNames() {
      return this.loginCache.getValues();
   }

   @Override
   public boolean removeLoginName(String var1) {
      return this.loginCache.remove(var1);
   }

   private static class BaseNameCache extends StringCacheParticle<LdapLocation> {
      public BaseNameCache(Folder var1) {
         super("LdapLocationBaseName", var1.getFile("basenames.dat"), 100, false);
      }

      protected String getValueFromObject(LdapLocation var1) {
         return var1.getBaseName();
      }
   }

   private static class ServerNameCache extends StringCacheParticle<LdapLocation> {
      public ServerNameCache(Folder var1) {
         super("LdapLocationServer", var1.getFile("servers.dat"), 100, false);
      }

      protected String getValueFromObject(LdapLocation var1) {
         return var1.getServer();
      }
   }
}
