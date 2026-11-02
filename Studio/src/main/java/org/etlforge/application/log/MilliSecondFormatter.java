package org.etlforge.application.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

final class MilliSecondFormatter extends Formatter {
   private final Date date = new Date();
   private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

   @Override
   public synchronized String format(LogRecord var1) {
      StringBuilder var2 = new StringBuilder();
      this.date.setTime(var1.getMillis());
      var2.append(this.formatter.format(this.date));
      var2.append(" ");
      if (var1.getSourceClassName() != null) {
         var2.append(var1.getSourceClassName());
      } else {
         var2.append(var1.getLoggerName());
      }

      if (var1.getSourceMethodName() != null) {
         var2.append(" ");
         var2.append(var1.getSourceMethodName());
      }

      var2.append(SystemUtils.LINE_SEPARATOR);
      String var3 = this.formatMessage(var1);
      var2.append(var1.getLevel().getLocalizedName());
      var2.append(": ");
      var2.append(var3);
      var2.append(SystemUtils.LINE_SEPARATOR);
      if (var1.getThrown() != null) {
         String var4 = ExceptionUtils.getStackTrace(var1.getThrown());
         var2.append(var4);
      }

      return var2.toString();
   }
}
