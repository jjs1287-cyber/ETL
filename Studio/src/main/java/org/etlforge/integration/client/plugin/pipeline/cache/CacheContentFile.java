package org.etlforge.integration.client.plugin.pipeline.cache;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import org.etlforge.io.KongaIoUtils;

final class CacheContentFile {
   private static final String FILE_NAME = "contents";
   private final File file;
   private final Set<String> ids;

   CacheContentFile(File var1) {
      this.file = new File(var1, "contents");
      this.ids = Sets.newHashSet();
      this.load();
   }

   private void load() {
      if (this.file.exists()) {
         try {
            String[] var1 = KongaIoUtils.readLines(this.file);

            for (String var5 : var1) {
               this.ids.add(var5);
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }
      }
   }

   private void store() {
      try {
         KongaIoUtils.createAndWrite(this.file, this.getAll(), false);
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   void clear() {
      this.ids.clear();
      if (this.file.exists()) {
         this.store();
      }
   }

   void add(Object var1) {
      this.ids.add(var1.toString());
      this.store();
   }

   String[] getAll() {
      return this.ids.toArray(new String[this.ids.size()]);
   }
}
