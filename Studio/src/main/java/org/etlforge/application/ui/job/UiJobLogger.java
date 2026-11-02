package org.etlforge.application.ui.job;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;

public final class UiJobLogger {
   static final Logger LOGGER = ApplicationLog.getLogger(UiJobManager.class, Level.OFF, true);

   private UiJobLogger() {
   }
}
