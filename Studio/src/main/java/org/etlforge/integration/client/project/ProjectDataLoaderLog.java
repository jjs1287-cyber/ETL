package org.etlforge.integration.client.project;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.etlforge.application.log.ApplicationLog;

public final class ProjectDataLoaderLog {
   public static final Logger LOG = ApplicationLog.getLogger(ProjectDataLoader.class, Level.INFO, true);

   private ProjectDataLoaderLog() {
   }
}
