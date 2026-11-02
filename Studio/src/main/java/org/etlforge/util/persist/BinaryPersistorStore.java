package org.etlforge.util.persist;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;

public class BinaryPersistorStore<T extends Persistable> extends AbstractFilePersistorStore<T> implements PersistorStore<T> {
   private static final FileFilter FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile() && var1.getName().endsWith(".dat");
      }
   };

   public BinaryPersistorStore(File var1) {
      super(var1);
   }

   @Override
   protected FilePersistor getEmptyPersistor(String var1) {
      return BinaryPersistor.newRoot(var1);
   }

   @Override
   protected File getFileForPersistable(File var1, T var2) {
      return new File(var1, var2.getId() + ".dat");
   }

   @Override
   protected Iterable<File> listFiles(File var1) {
      File[] var2 = var1.listFiles(FILE_FILTER);
      return var2 != null ? Arrays.asList(var2) : Collections.emptyList();
   }

   @Override
   protected FilePersistor loadPersistorFromFile(File var1) throws Exception {
      return BinaryPersistor.load(var1);
   }
}
