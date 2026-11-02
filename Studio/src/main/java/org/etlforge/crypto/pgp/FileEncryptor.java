package org.etlforge.crypto.pgp;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.etlforge.crypto.CryptoException;

public final class FileEncryptor {
   private final Encryptor worker;
   private boolean armor;

   public FileEncryptor(Encryptor var1) {
      Preconditions.checkNotNull(var1, "worker");
      this.worker = var1;
      this.armor = true;
   }

   public void encrypt(File var1, File var2, Key var3) throws IOException, CryptoException {
      try {
         this.prepareWorker(var1);
         byte[] var4 = this.worker.encrypt(Files.toByteArray(var1), var3, this.armor);
         Files.write(var4, var2);
      } finally {
         Closeables.closeQuietly(var3);
      }
   }

   private void prepareWorker(File var1) {
      this.worker.setMessageInfo(var1.getName(), new Date(var1.lastModified()));
   }

   public void signAndEncrypt(File var1, File var2, Key var3, Key var4, char[] var5) throws IOException, CryptoException {
      try {
         this.prepareWorker(var1);
         byte[] var6 = this.worker.signAndEncrypt(Files.toByteArray(var1), var3, this.armor, var4, var5);
         Files.write(var6, var2);
      } finally {
         Closeables.closeQuietly(var3);
         Closeables.closeQuietly(var4);
      }
   }

   public void setArmor(boolean var1) {
      this.armor = var1;
   }
}
