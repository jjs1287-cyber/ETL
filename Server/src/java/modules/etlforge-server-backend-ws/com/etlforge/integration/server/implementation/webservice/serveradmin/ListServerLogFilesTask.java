package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.io.File;
import org.etlforge.integration.server.log.ServerLogDirectory;

final class ListServerLogFilesTask extends ListFilesTask {
   public ListServerLogFilesTask(String var1, String var2) {
      super("listServerLogFiles", var1, var2);
   }

   @Override
   protected File[] listFiles() throws Exception {
      ServerLogDirectory var1 = new ServerLogDirectory();
      return var1.getLogFiles();
   }
}
