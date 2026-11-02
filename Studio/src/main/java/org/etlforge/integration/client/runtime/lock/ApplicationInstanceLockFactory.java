package org.etlforge.integration.client.runtime.lock;

import java.io.File;
import org.apache.commons.lang.SystemUtils;
import org.springframework.context.ApplicationContext;

public final class ApplicationInstanceLockFactory {
   private ApplicationInstanceLockFactory() {
   }

   public static ApplicationInstanceLock getLock(ApplicationContext var0, File var1) {
      if (Boolean.getBoolean("AllowMultipleInstances")) {
         return new ApplicationInstanceLockFactory.DummyLock();
      }

      if (SystemUtils.IS_OS_WINDOWS && Boolean.getBoolean("UseSocketLock")) {
         String var2 = "platform.InstanceLock";
         if (var0.containsBean(var2)) {
            return (ApplicationInstanceLock)var0.getBean(var2);
         }
      }

      return new FileLock(var1);
   }

   private static final class DummyLock implements ApplicationInstanceLock {
      private DummyLock() {
      }

      @Override
      public boolean lock(String[] var1) {
         return true;
      }
   }
}
