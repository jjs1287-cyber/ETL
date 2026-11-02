package org.etlforge.application.worker;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.config.ApplicationConfigurationException;

public final class ApplicationWorkerFactory {
   public static ApplicationWorker createWorker() throws ApplicationConfigurationException {
      try {
         Class var0 = ApplicationConfiguration.getClass("ApplicationWorker.Implementation", ApplicationWorker.class);
         return var0 != null ? (ApplicationWorker)var0.newInstance() : new DefaultApplicationWorker();
      } catch (ClassCastException var1) {
         throw new ApplicationConfigurationException(var1);
      } catch (InstantiationException var2) {
         throw new ApplicationConfigurationException(var2);
      } catch (IllegalAccessException var3) {
         throw new ApplicationConfigurationException(var3);
      }
   }

   private ApplicationWorkerFactory() {
   }
}
