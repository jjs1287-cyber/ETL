package org.etlforge.application.errorlog;

import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.google.common.io.OutputSupplier;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import org.etlforge.system.SystemUtils;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class ErrorLogEntry {
   private final Object source;
   private final ErrorLevel level;
   private final String message;
   private final String details;
   private final Throwable cause;
   private final long timestamp;

   ErrorLogEntry(Object var1, ErrorLevel var2, String var3, String var4, Throwable var5) {
      this.source = var1 != null ? var1 : "[Unknown]";
      this.level = this.getEffectiveLevel(var2, var5);
      this.message = var3;
      this.details = var4;
      this.cause = var5;
      this.timestamp = System.currentTimeMillis();
   }

   public Object source() {
      return this.source;
   }

   public long timestamp() {
      return this.timestamp;
   }

   public String message() {
      return this.message;
   }

   public String details() {
      return this.details != null ? this.details : this.message;
   }

   public Throwable cause() {
      return this.cause;
   }

   public ErrorLevel level() {
      return this.level;
   }

   public void report(Writer var1) throws IOException {
      writeLine(var1, "Level: ", this.level);
      writeLine(var1, "Reported at: ", new Date(this.timestamp));
      writeLine(var1, "Reported from: ", this.source);
      writeLine(var1, "Message: ", this.message);
      writeLine(var1, "Details: ", this.details);
      if (this.cause != null) {
         String var2 = Throwables.getStackTraceAsString(this.cause);
         writeLine(var1, "Cause: ", var2);
      }

      writeLine(var1);
      writeLine(var1);
   }

   private static void writeLine(Writer var0, Object... var1) throws IOException {
      for (Object var5 : var1) {
         var0.write(var5.toString());
      }

      var0.write(SystemUtils.LINE_SEPARATOR);
   }

   public void report(OutputSupplier<? extends Writer> var1) throws IOException {
      Writer var2 = (Writer)var1.getOutput();
      boolean var3 = true;

      try {
         this.report(var2);
         var3 = false;
      } finally {
         Closeables.close(var2, var3);
      }
   }

   public String getReport() {
      try {
         StringBuilder var1 = new StringBuilder();
         Writer var2 = CharStreams.asWriter(var1);
         this.report(var2);
         return var1.toString();
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   private ErrorLevel getEffectiveLevel(ErrorLevel var1, Throwable var2) {
      ErrorLevel var3 = var1;
      if (var2 instanceof OutOfMemoryError && var1.compareTo(ErrorLevel.ATTENTION) < 0) {
         var3 = ErrorLevel.ATTENTION;
      }

      return var3;
   }

   public void copyToClipboard() {
      String var1 = this.getReport();
      ClipboardUtils.copyToSystemClipboard(var1);
   }

   public static String toString(ErrorLogEntry... var0) {
      return toString(Arrays.asList(var0));
   }

   public static String toString(Iterable<ErrorLogEntry> var0) {
      StringBuilder var1 = new StringBuilder();

      for (ErrorLogEntry var3 : var0) {
         String var4 = var3.getReport();
         var1.append(var4);
      }

      return var1.toString();
   }
}
