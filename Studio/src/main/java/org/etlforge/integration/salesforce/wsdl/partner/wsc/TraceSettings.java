package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sforce.ws.ConnectorConfig;
import java.io.File;
import java.io.FileNotFoundException;

public final class TraceSettings {
   private final boolean trace;
   private final Optional<File> traceFile;

   public static TraceSettings noTracing() {
      return new TraceSettings(false, Optional.absent());
   }

   public static TraceSettings toConsole() {
      return new TraceSettings(true, Optional.absent());
   }

   public static TraceSettings toFile(File var0) {
      return new TraceSettings(true, Optional.of(var0));
   }

   private TraceSettings(boolean var1, Optional<File> var2) {
      this.trace = var1;
      this.traceFile = Preconditions.checkNotNull(var2);
   }

   public void applyTo(ConnectorConfig var1) {
      try {
         var1.setTraceMessage(this.trace);
         if (this.traceFile.isPresent()) {
            String var2 = this.traceFile.get().getAbsolutePath();
            var1.setTraceFile(var2);
         }
      } catch (FileNotFoundException var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public String toString() {
      if (this.trace) {
         return this.traceFile.isPresent() ? "Trace to file " + this.traceFile.get() : "Trace to console";
      } else {
         return "No tracing";
      }
   }
}
