package org.etlforge.integration.activity.client.files;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.util.id.KongaGuid;

public final class RemoteFileCache {
   private final Cache<KongaGuid, File> localFiles = CacheBuilder.newBuilder().maximumSize(100L).build();

   public File wrapSupplier(OperationFile var1, Callable<File> var2) throws Exception {
      Callable var3 = this.wrapCallable(var2);

      try {
         return this.localFiles.get(var1.getGuid(), var3);
      } catch (UncheckedExecutionException var5) {
         if (var5.getCause() instanceof CancellationException) {
            return null;
         } else {
            throw var5;
         }
      }
   }

   private Callable<File> wrapCallable(final Callable<File> var1) {
      return new Callable<File>() {
         public File call() throws Exception {
            File var1x = (File)var1.call();
            if (var1x == null) {
               throw new CancellationException();
            } else {
               return var1x;
            }
         }
      };
   }
}
