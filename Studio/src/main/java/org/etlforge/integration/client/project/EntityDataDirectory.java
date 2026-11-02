package org.etlforge.integration.client.project;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.io.File;
import java.io.FileFilter;
import org.etlforge.integration.data.entity.EntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;

public final class EntityDataDirectory {
   private final EntityType type;
   private final Folder path;
   private static final FileFilter DATA_FILE_FILTER = new FileFilter() {
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

   public EntityDataDirectory(EntityType var1, Folder var2) {
      this.type = var1;
      this.path = var2;
   }

   public Folder getPath() {
      return this.path;
   }

   public File getEntityFile(EntityDescriptor var1) {
      assert var1.getEntityType() == this.type;
      return this.path.getFile(var1.getID() + ".xml");
   }

   public Optional<Persistor> loadEntityFile(EntityDescriptor var1) throws Exception {
      File var2 = this.getEntityFile(var1);
      if (var2.isFile()) {
         XmlPersistor var3 = XmlPersistor.load(var2);
         return Optional.of(var3);
      } else {
         return Optional.absent();
      }
   }

   public boolean isEmpty() {
      return this.listAllFiles().length == 0;
   }

   public File[] listAllFiles() {
      return this.path.listFiles(DATA_FILE_FILTER);
   }

   public void deleteFiles(Predicate<File> var1) {
      for (File var5 : this.listAllFiles()) {
         if (var1.apply(var5)) {
            var5.delete();
         }
      }
   }

   @Override
   public String toString() {
      return String.format("%s [%s]", this.type, this.path);
   }
}
