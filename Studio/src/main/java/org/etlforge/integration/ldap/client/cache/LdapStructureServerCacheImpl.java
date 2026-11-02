package org.etlforge.integration.ldap.client.cache;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.util.crypto.DigestUtils;

final class LdapStructureServerCacheImpl implements LdapStructureServerCache {
   private final Folder root;
   private final Map<String, LdapStructureCache> caches;

   public LdapStructureServerCacheImpl(File var1) {
      this.root = new Folder(var1, "org.etlforge.integration.client.component.ldap.cache.LdapStructureCache");
      this.root.createOnDisk();
      this.caches = Maps.newHashMap();
      this.init();
   }

   @Override
   public LdapStructureCache getCache(String var1) {
      synchronized (this.caches) {
         LdapStructureCache var3 = this.caches.get(var1);
         if (var3 == null) {
            String var4 = this.getServerId(var1);
            var3 = new LdapStructureFileCache(this.root.getFile(var4));
            this.caches.put(var4, var3);
         }

         return var3;
      }
   }

   @Override
   public String getID() {
      return "org.etlforge.integration.client.component.ldap.cache.LdapStructureCache";
   }

   private void init() {
      for (File var4 : this.root.listFiles(FileUtils.DIRECTORIES)) {
         this.caches.put(var4.getName(), new LdapStructureFileCache(var4));
      }
   }

   private String getServerId(String var1) {
      return DigestUtils.getMd5DigestAsHexString(var1);
   }
}
