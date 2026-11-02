package com.etlforge.integration.server.sfdc;

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import java.io.File;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.TraceSettings;
import org.etlforge.integration.server.conf.ServerConfig;

public abstract class TraceSettingsSupplier implements Supplier<TraceSettings> {
   public static TraceSettingsSupplier defaultSupplier() {
      return new TraceSettingsSupplier.ToFile();
   }

   private static class ToFile extends TraceSettingsSupplier {
      private ToFile() {
      }

      public TraceSettings get() {
         String var1 = ServerConfig.getOption("Debug", "SfdcTraceFile", null);
         if (!Strings.isNullOrEmpty(var1)) {
            File var2 = new File(var1);
            if (!var2.isDirectory()) {
               return TraceSettings.toFile(var2);
            }

            System.err.println("The path " + var1 + " points to a directory. It must point to a file.");
         }

         return TraceSettings.noTracing();
      }
   }
}
