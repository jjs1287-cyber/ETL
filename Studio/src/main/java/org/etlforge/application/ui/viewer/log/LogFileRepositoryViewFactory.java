package org.etlforge.application.ui.viewer.log;

import org.etlforge.application.log.LogFileRepository;
import org.etlforge.application.ui.window.WindowSection;

public abstract class LogFileRepositoryViewFactory {
   public abstract LogFileRepositoryView create(LogFileRepository var1, WindowSection var2);

   public static LogFileRepositoryViewFactory newInstance() {
      return new LogFileRepositoryViewFactory() {
         @Override
         public LogFileRepositoryView create(LogFileRepository var1, WindowSection var2) {
            return new LogFileRepositoryViewImpl(var1, var2);
         }
      };
   }
}
