package org.etlforge.util.properties;

import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Properties;

public final class PropertiesWriter {
   public static void write(Properties var0, File var1) throws IOException {
      write(var0, Files.newOutputStreamSupplier(var1));
   }

   public static void write(Properties var0, OutputSupplier<? extends OutputStream> var1) throws IOException {
      OutputStream var2 = (OutputStream)var1.getOutput();

      try {
         var0.store(var2, null);
      } finally {
         var2.close();
      }
   }

   public static String writeToString(Properties var0) {
      try {
         StringWriter var1 = new StringWriter();
         var0.store(var1, null);
         return var1.toString();
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   private PropertiesWriter() {
   }
}
