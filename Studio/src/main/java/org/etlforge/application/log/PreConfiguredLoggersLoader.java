package org.etlforge.application.log;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URL;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.FilteredLineProcessor;

final class PreConfiguredLoggersLoader {
   private static final String RESOURCE = "ApplicationLog.Loggers";

   public static void load() {
      try {
         if (checkIfThereArePreconfiguredLoggers()) {
            URL var0 = Resources.getResource("ApplicationLog.Loggers");
            Resources.readLines(var0, Charsets.UTF_8, new PreConfiguredLoggersLoader.Processor());
         }
      } catch (Exception var1) {
         ErrorLog.log(PreConfiguredLoggersLoader.class, "Failed to properly load the pre-configured application loggers.", var1);
      }
   }

   private static boolean checkIfThereArePreconfiguredLoggers() {
      return PreConfiguredLoggersLoader.class.getClassLoader().getResource("ApplicationLog.Loggers") != null;
   }

   private static final class Processor extends FilteredLineProcessor<Void> {
      private Processor() {
         super(HASHMARK_COMMENT, true);
      }

      @Override
      protected boolean handleAcceptedLine(String var1) throws IOException {
         try {
            Class.forName(var1);
         } catch (ClassNotFoundException var3) {
            ErrorLog.log(PreConfiguredLoggersLoader.class, null, var3);
         }

         return true;
      }

      public Void getResult() {
         return null;
      }
   }
}
