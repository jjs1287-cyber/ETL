package org.etlforge.integration.loadsource.client;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.etlforge.io.Folder;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.properties.PropertiesWriter;

public final class LatestFile {
   private static final String PROPERTIES_FILE_NAME = ".latest.properties";
   private static final String KEY = "name";
   private final Folder parent;
   private final Properties properties;

   public LatestFile(Folder var1) {
      this.parent = var1;
      this.properties = this.loadProperties();
   }

   private Properties loadProperties() {
      try {
         File var1 = this.getPropertiesFile();
         if (var1.canRead()) {
            return PropertiesReader.read(var1);
         }
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      return new Properties();
   }

   private File getPropertiesFile() {
      return this.parent.getFile(".latest.properties");
   }

   public boolean isPropertyFile(File var1) {
      return this.parent.contains(var1) && ".latest.properties".equals(var1.getName());
   }

   public FileUi getLatest() {
      String var1 = this.properties.getProperty("name");
      if (var1 != null) {
         File var2 = this.parent.getFile(var1);
         return new FileUi(var2, this.parent.asJavaFile());
      } else {
         return null;
      }
   }

   public void update(File var1) {
      this.setLatestFileName(var1.getName());
   }

   private void setLatestFileName(String var1) {
      try {
         if (var1 != null) {
            this.properties.setProperty("name", var1);
         } else {
            this.properties.remove("name");
         }

         File var2 = this.getPropertiesFile();
         PropertiesWriter.write(this.properties, var2);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public void deleteFile(File var1) {
      if (var1.getName().equals(this.properties.getProperty("name"))) {
         this.setLatestFileName(null);
      }
   }
}
