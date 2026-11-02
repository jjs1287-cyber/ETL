package org.etlforge.integration.jitterpack.impl;

import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;

public final class JitterPackLog {
   private static final Logger logger = ApplicationLog.getLogger(JitterPackLog.class, true);

   static Logger getLogger() {
      return logger;
   }

   private JitterPackLog() {
   }
}
