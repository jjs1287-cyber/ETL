package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.ui.sfdc.wsdl.KnownWsdlsStoreFactory;
import com.etlforge.integration.salesforce.wsdl.KnownWsdlsStore;
import java.io.File;
import org.etlforge.application.Application;
import org.etlforge.application.appdir.ApplicationDirectory;
import org.etlforge.application.appdir.CacheDirectory;
import org.etlforge.application.worker.ApplicationWorker;

public class InterchangeKnownWsdlsStore {
   private static KnownWsdlsStore store;

   public static synchronized KnownWsdlsStore getStore() {
      if (store == null) {
         store = createStore();
      }

      return store;
   }

   private static KnownWsdlsStore createStore() {
      ApplicationDirectory var0 = Application.getApplicationDirectory();
      CacheDirectory var1 = var0.getCacheDirectory();
      File var2 = new File(var1.getLocation(), KnownWsdlsStore.class.getName());
      KnownWsdlsStore var3 = KnownWsdlsStoreFactory.fileBasedImplementation(var2);
      preloadWsdls(var3);
      return var3;
   }

   private static void preloadWsdls(final KnownWsdlsStore var0) {
      assert store == null;
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            var0.getKnownWsdls();
         }
      });
   }

   private InterchangeKnownWsdlsStore() {
   }
}
