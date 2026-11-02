package org.etlforge.integration.client.ui.interchange.component.page.entity;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.application.DebugMode;
import org.etlforge.application.log.ApplicationLog;

public class IntegrationEntityPageLog {
   public static final Logger LOG = createLogger();

   private static Logger createLogger() {
      Level var0 = DebugMode.IS_ENABLED ? Level.INFO : Level.OFF;
      return ApplicationLog.getLogger(IntegrationEntityPageLog.class, var0, true);
   }

   private IntegrationEntityPageLog() {
   }
}
