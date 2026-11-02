package org.etlforge.integration.client.project.persistor.v20;

import java.io.File;
import java.io.FileFilter;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;

final class FileSupport {
   public static final FileFilter DATA_FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         if (!var1.isFile()) {
            return false;
         }

         String var2 = var1.getName().toLowerCase();
         return var2.endsWith(".xml") && this.areAllCharactersValid(var2.substring(0, var2.length() - 4));
      }

      private boolean areAllCharactersValid(String var1) {
         for (char var5 : var1.toCharArray()) {
            if (!this.isValid(var5)) {
               return false;
            }
         }

         return true;
      }

      private boolean isValid(char var1) {
         return Character.isDigit(var1) || var1 >= 'a' && var1 <= 'f' || var1 == '-';
      }
   };

   public static File getFile(File var0, IntegrationEntity var1) {
      return new File(getDirectory(var0, var1), getFilename(var1.getID()));
   }

   public static File getFile(File var0, EntityDescriptor var1) {
      return new File(getDirectory(var0, var1.getEntityType()), getFilename(var1.getID()));
   }

   private static File getDirectory(File var0, IntegrationEntity var1) {
      return getDirectory(var0, var1.getEntityType());
   }

   public static File getDirectory(File var0, EntityType var1) {
      String var2 = var1.toString();
      File var3 = new File(var0, var2);
      if (!var3.exists() && !var3.mkdirs()) {
      }

      return var3;
   }

   private static String getFilename(IntegrationEntityId var0) {
      return var0 + ".xml";
   }

   private FileSupport() {
   }
}
