package org.etlforge.integration.jitterpack.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.etlforge.integration.client.project.jitterpack.IllegalDataFormatException;
import org.etlforge.io.FileUtils;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.zip.ZipUtils;

class V100_DescriptorDataFactory extends DescriptorDataFactory {
   private final String headerFileName;

   public V100_DescriptorDataFactory(String var1) {
      this.headerFileName = var1;
   }

   @Override
   public DescriptorData create(File var1) throws IOException, IllegalDataFormatException {
      ZipFile var2 = null;

      try {
         var2 = new ZipFile(var1);
         Properties var3 = this.loadDataFromZipFile(var2);
         String var4 = this.extractProjectFolderNameFromZipFile(var2);
         return new DescriptorData(var4, var3, false);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var12) {
               var12.printStackTrace();
            }
         }
      }
   }

   private Properties loadDataFromZipFile(ZipFile var1) throws IllegalDataFormatException, IOException {
      ZipEntry var2 = this.getHeaderZipEntry(var1);
      InputStream var3 = null;

      try {
         var3 = var1.getInputStream(var2);
         Properties var4 = new Properties();
         var4.load(var3);
         return var4;
      } finally {
         KongaIoUtils.close(var3);
      }
   }

   private String extractProjectFolderNameFromZipFile(ZipFile var1) throws IllegalDataFormatException {
      Enumeration var2 = var1.entries();
      if (var2.hasMoreElements()) {
         ZipEntry var3 = (ZipEntry)var2.nextElement();
         File var4 = new File(ZipUtils.getPlatformIndependentName(var3));
         return FileUtils.getRootFolder(var4).getName();
      } else {
         throw new IllegalDataFormatException(getErrorMessageHeader() + "Invalid file contents.");
      }
   }

   private ZipEntry getHeaderZipEntry(ZipFile var1) throws IllegalDataFormatException {
      Enumeration var2 = var1.entries();

      while (var2.hasMoreElements()) {
         ZipEntry var3 = (ZipEntry)var2.nextElement();
         String var4 = ZipUtils.getPlatformIndependentName(var3);
         File var5 = new File(var4);
         if (var5.getName().equals(this.headerFileName)) {
            return var3;
         }
      }

      throw new IllegalDataFormatException(getErrorMessageHeader() + "Missing header information.");
   }

   private static String getErrorMessageHeader() {
      return "Not a valid Jitterpak: ";
   }
}
