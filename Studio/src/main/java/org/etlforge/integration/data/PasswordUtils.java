package org.etlforge.integration.data;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.etlforge.integration.data.location.LoginProtectedResource;

public final class PasswordUtils {
   public static Optional<Password> getPassword(IntegrationData var0, String var1) {
      String var2;
      synchronized (var0.getDataLock()) {
         var2 = var0.getProperty(var1);
      }

      return Strings.isNullOrEmpty(var2) ? Optional.absent() : Optional.of(Password.decrypt(var2));
   }

   public static void setPassword(IntegrationData var0, String var1, Optional<Password> var2) {
      String var3 = var0.getProperty(var1);
      Optional var4;
      if (Strings.isNullOrEmpty(var3)) {
         var4 = Optional.absent();
      } else {
         var4 = Optional.of(Password.decrypt(var3));
      }

      if (!var4.equals(var2)) {
         String var5 = var2.isPresent() ? ((Password)var2.get()).encrypted() : null;
         var0.setProperty(var1, var5);
         var0.firePropertyChange(var1, var4, var2);
      }
   }

   public static String getClearTextPassword(LoginProtectedResource var0) {
      Optional var1 = var0.getPassword();
      return getClearTextPassword(var1);
   }

   public static String getClearTextPassword(Optional<Password> var0) {
      return var0.isPresent() ? ((Password)var0.get()).clearText() : null;
   }

   private PasswordUtils() {
   }
}
