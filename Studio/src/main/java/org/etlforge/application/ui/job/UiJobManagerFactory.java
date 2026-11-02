package org.etlforge.application.ui.job;

import org.etlforge.application.config.ApplicationConfigurationException;

public final class UiJobManagerFactory {
   public static UiJobManager createManager() throws ApplicationConfigurationException {
      return new DefaultJobManager();
   }

   private UiJobManagerFactory() {
   }
}
