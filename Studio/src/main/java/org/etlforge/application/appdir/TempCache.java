package org.etlforge.application.appdir;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.util.KongaSerializationUtils;

public abstract class TempCache<K, V extends Serializable> {
   private final Class<V> valueClass;
   private final Set<K> invalid = Sets.newHashSet();

   public TempCache(Class<V> var1) {
      this.valueClass = var1;
   }

   public final void store(K var1, V var2) {
      IOException var3 = null;
      synchronized (this) {
         try {
            File var5 = this.getCacheFile((K)var1);
            var5.deleteOnExit();
            KongaSerializationUtils.serializeToFile(var2, var5);
            this.invalid.remove(var1);
         } catch (IOException var7) {
            var3 = var7;
            this.invalid.add((K)var1);
         }
      }

      if (var3 != null) {
         ErrorLog.internal(this.getClass(), null, var3);
      }
   }

   private File getCacheFile(K var1) {
      String var2 = this.getFileName((K)var1);
      return ApplicationTempFolder.newFile(var2);
   }

   protected abstract String getFileName(K var1);

   public final V restore(K var1) {
      Serializable var2 = null;
      Exception var3 = null;
      synchronized (this) {
         if (!this.invalid.contains(var1)) {
            File var5 = this.getCacheFile((K)var1);
            if (var5.canRead()) {
               try {
                  var2 = KongaSerializationUtils.deserializeFromFile(var5, this.valueClass);
               } catch (Exception var8) {
                  var3 = var8;
               }
            }
         }
      }

      if (var3 != null) {
         ErrorLog.internal(this.getClass(), null, var3);
      }

      return (V)var2;
   }

   public final void erase(K var1) {
      synchronized (this) {
         File var3 = this.getCacheFile((K)var1);
         if (var3.exists()) {
            boolean var4 = var3.delete();
            if (!var4) {
               this.invalid.add((K)var1);
               return;
            }
         }

         this.invalid.remove(var1);
      }
   }
}
