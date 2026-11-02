package org.etlforge.application.filestore;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Map.Entry;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.properties.PropertiesWriter;

abstract class MapPersistor {
   private final File mapFile;

   public MapPersistor(File var1) {
      this.mapFile = var1;
   }

   public void load() {
      if (this.mapFile.exists()) {
         Properties var1 = this.loadProperties();

         for (Entry var3 : var1.entrySet()) {
            this.addEntry(var3);
         }
      } else {
         try {
            this.mapFile.createNewFile();
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }
   }

   private Properties loadProperties() {
      try {
         return PropertiesReader.read(this.mapFile);
      } catch (IOException var2) {
         var2.printStackTrace();
         return new Properties();
      }
   }

   protected abstract void addToMap(Object var1, Object var2);

   private void addEntry(Entry<Object, Object> var1) {
      this.addToMap(var1.getKey(), var1.getValue());
   }

   public void store() {
      Properties var1 = this.createProperties();
      this.write(var1);
   }

   private Properties createProperties() {
      Properties var1 = new Properties();
      this.populateProperties(var1);
      return var1;
   }

   protected abstract void populateProperties(Properties var1);

   private void write(Properties var1) {
      try {
         PropertiesWriter.write(var1, this.mapFile);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }
}
