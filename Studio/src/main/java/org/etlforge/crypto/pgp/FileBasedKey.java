package org.etlforge.crypto.pgp;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.etlforge.io.KongaIoUtils;

public final class FileBasedKey implements Key {
   private final File file;
   private final Long keyId;
   private final String userId;
   private InputStream stream;
   private final Object lock = new Object();

   public FileBasedKey(File var1) {
      this(var1, -1L);
   }

   public FileBasedKey(File var1, Long var2) {
      this.file = Preconditions.checkNotNull(var1, "file");
      this.keyId = Preconditions.checkNotNull(var2, "keyId");
      this.userId = null;
   }

   public FileBasedKey(File var1, String var2) {
      this.file = Preconditions.checkNotNull(var1, "file");
      this.userId = Preconditions.checkNotNull(var2, "userId");
      this.keyId = null;
   }

   public FileBasedKey(File var1, Long var2, String var3) {
      this.file = Preconditions.checkNotNull(var1, "file");
      Preconditions.checkArgument(var2 != null || var3 != null, "Must provide either a key ID or a user ID");
      this.userId = var3;
      this.keyId = var2;
   }

   @Override
   public Long getKeyId() {
      return this.keyId;
   }

   @Override
   public String getUserId() {
      return this.userId;
   }

   @Override
   public InputStream getKeyData() throws IOException {
      synchronized (this.lock) {
         if (this.stream == null) {
            this.stream = new FileInputStream(this.file);
         }

         return this.stream;
      }
   }

   @Override
   public void close() throws IOException {
      synchronized (this.lock) {
         if (this.stream != null) {
            KongaIoUtils.close(this.stream);
            this.stream = null;
         }
      }
   }
}
