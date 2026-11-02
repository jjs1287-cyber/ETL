package org.etlforge.util.persist;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.etlforge.lang.exception.ExceptionHandler;

public abstract class AbstractFilePersistorStore<T extends Persistable> implements PersistorStore<T> {
   private final File location;
   private Logger logger;
   private ExceptionHandler<Exception> errorHandler;

   protected AbstractFilePersistorStore(File var1) {
      this.location = var1;
   }

   public final List<T> load(PersistableFactory<T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (File var4 : this.listFiles(this.location)) {
         try {
            FilePersistor var5 = this.loadPersistorFromFile(var4);
            Persistable var6 = var1.restore(var5);
            if (var6 != null) {
               var2.add(var6);
            }
         } catch (Exception var7) {
            this.handleException(var7, var4, "load");
         }
      }

      return var2;
   }

   private void handleException(Exception var1, File var2, String var3) {
      if (this.errorHandler != null) {
         this.errorHandler.caught(var1);
      }

      if (this.logger != null) {
         this.logger.throwing(this.getClass().getName(), var3, var1);
      } else {
         System.err.println("Failed to " + var3 + " the file " + var2.getAbsolutePath());
         var1.printStackTrace();
      }
   }

   @Override
   public final void store(T var1, String var2) {
      File var3 = this.getFileForPersistable(this.location, (T)var1);

      try {
         FilePersistor var4 = this.getEmptyPersistor(var2);
         var1.save(var4);
         var4.store(var3);
      } catch (Exception var5) {
         this.handleException(var5, var3, "store");
      }
   }

   @Override
   public final void remove(T var1) {
      File var2 = this.getFileForPersistable(this.location, (T)var1);
      if (var2.exists()) {
         var2.delete();
      }
   }

   protected abstract Iterable<File> listFiles(File var1);

   protected abstract FilePersistor loadPersistorFromFile(File var1) throws Exception;

   protected abstract FilePersistor getEmptyPersistor(String var1) throws Exception;

   protected abstract File getFileForPersistable(File var1, T var2);

   public final void setLogger(Logger var1) {
      this.logger = var1;
   }

   protected final Logger getLogger() {
      return this.logger;
   }

   public final void setErrorHandler(ExceptionHandler<Exception> var1) {
      this.errorHandler = var1;
   }

   protected final ExceptionHandler<Exception> getErrorHandler() {
      return this.errorHandler;
   }
}
