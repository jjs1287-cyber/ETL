package org.etlforge.util.properties;

import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;

public class PropertiesReader {
   public static Properties read(File var0) throws IOException {
      return read(Files.newInputStreamSupplier(var0));
   }

   public static Properties read(InputSupplier<? extends InputStream> var0) throws IOException {
      InputStream var1 = (InputStream)var0.getInput();

      try {
         Properties var2 = new Properties();
         var2.load(var1);
         return var2;
      } finally {
         Closeables.closeQuietly(var1);
      }
   }

   public static Properties readResource(String var0) throws IOException {
      URL var1 = Resources.getResource(var0);
      InputSupplier var2 = Resources.newInputStreamSupplier(var1);
      return read(var2);
   }

   public static Properties readFromString(String var0) {
      StringReader var1 = new StringReader(var0);
      Properties var2 = new Properties();

      try {
         var2.load(var1);
         return var2;
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   private PropertiesReader() {
   }
}
