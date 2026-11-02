package org.etlforge.crypto.pgp;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import org.etlforge.io.KongaIoUtils;

public final class StreamBasedKey implements Key {
   private final InputStream stream;
   private final Long keyId;
   private final String userId;
   private boolean closed;
   private final Object lock;

   public StreamBasedKey(String var1) {
      this(var1, -1L);
   }

   public StreamBasedKey(String var1, Long var2) {
      this(StreamBasedKey.class.getClassLoader().getResourceAsStream(var1), var2);
   }

   public StreamBasedKey(String var1, String var2) {
      this(StreamBasedKey.class.getClassLoader().getResourceAsStream(var1), var2);
   }

   public StreamBasedKey(InputStream var1) {
      this(var1, -1L);
   }

   public StreamBasedKey(InputStream var1, Long var2) {
      Preconditions.checkNotNull(var1, "stream");
      Preconditions.checkNotNull(var2, "keyId");
      this.stream = var1;
      this.keyId = var2;
      this.userId = null;
      this.lock = new Object();
   }

   public StreamBasedKey(InputStream var1, String var2) {
      Preconditions.checkNotNull(var1, "stream");
      Preconditions.checkNotNull(var2, "userId");
      this.stream = var1;
      this.userId = var2;
      this.keyId = null;
      this.lock = new Object();
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
         if (this.closed) {
            throw new IOException("This key has been closed. It cannot be reused.");
         } else {
            return this.stream;
         }
      }
   }

   @Override
   public void close() {
      synchronized (this.lock) {
         KongaIoUtils.close(this.stream);
         this.closed = true;
      }
   }
}
