package org.etlforge.integration.client.entity.valuecache;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.Folder;

public final class FileStorage implements CacheStorage {
   private final File file;

   public FileStorage(File var1) {
      this.file = Preconditions.checkNotNull(var1, "file");
   }

   @Override
   public void write(Iterable<String> var1) {
      Folder var2 = Folder.parentOf(this.file);
      var2.createOnDisk();

      try {
         Files.write(Joiner.on(SystemUtils.LINE_SEPARATOR).join(var1), this.file, Charsets.UTF_8);
      } catch (IOException var4) {
         ErrorLog.internal(this.getClass(), "Failed to write cached property values to disk.", var4);
      }
   }

   @Override
   public Iterable<String> read() {
      if (this.file.exists()) {
         try {
            return Files.readLines(this.file, Charsets.UTF_8);
         } catch (IOException var2) {
            ErrorLog.internal(this.getClass(), "Failed to write cached property values to disk.", var2);
         }
      }

      return Collections.emptyList();
   }
}
