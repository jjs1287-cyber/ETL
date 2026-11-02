package org.etlforge.application.log;

import java.io.File;
import java.util.Date;

public interface LogFileRepository {
   LogFileRepository.Folder[] getSessionLogs();

   boolean isSystemOut(File var1);

   boolean isSystemErr(File var1);

   interface Folder {
      Date getSessionDate();

      String getName();

      File[] getLogFiles();

      File getSystemOut();

      File getSystemErr();
   }
}
