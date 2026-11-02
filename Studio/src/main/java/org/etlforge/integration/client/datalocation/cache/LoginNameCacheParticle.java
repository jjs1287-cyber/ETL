package org.etlforge.integration.client.datalocation.cache;

import java.io.File;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.io.Folder;

class LoginNameCacheParticle<T extends LoginProtectedResource> extends StringCacheParticle<T> {
   private static final String FILENAME = "logins.dat";

   public LoginNameCacheParticle(String var1, Folder var2, int var3) {
      super(var1, var2.getFile("logins.dat"), var3, true);
   }

   public LoginNameCacheParticle(String var1, File var2, int var3) {
      super(var1, new File(var2, "logins.dat"), var3, true);
   }

   public LoginNameCacheParticle(String var1, File var2, int var3, boolean var4) {
      super(var1, var2, var3, var4);
   }

   protected String getValueFromObject(T var1) {
      return var1.getLogin();
   }
}
