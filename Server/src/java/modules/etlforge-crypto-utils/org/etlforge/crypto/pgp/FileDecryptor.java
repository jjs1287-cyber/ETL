package org.etlforge.crypto.pgp;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.crypto.CryptoException;
import org.etlforge.io.Folder;
import org.etlforge.system.ArgCheck;

public final class FileDecryptor {
   private final Decryptor worker;
   private Key verificationKey;
   private String defaultOutputFileName;

   public FileDecryptor(Decryptor var1) {
      this.worker = Preconditions.checkNotNull(var1);
   }

   public FileDecryptionResult decryptToFolder(File var1, Folder var2, String var3, Key var4, char[] var5) throws IOException, CryptoException {
      this.defaultOutputFileName = ArgCheck.checkNotBlank(var3, "defaultOutputFileName");
      return this.decrypt(var1, var2.asJavaFile(), var4, var5);
   }

   public FileDecryptionResult decrypt(File var1, File var2, Key var3, char[] var4) throws IOException, CryptoException {
      try {
         byte[] var5 = Files.toByteArray(var1);
         DecryptionResult var6 = this.worker.decrypt(var5, var3, var4);
         File var7 = this.writeClearTextToFile(var6, var2);
         return new FileDecryptor.Result(var7, var6);
      } finally {
         Closeables.closeQuietly(var3);
         Closeables.closeQuietly(this.verificationKey);
      }
   }

   public void setVerificationKey(Key var1) {
      this.verificationKey = var1;
      this.worker.setVerificationKey(var1);
   }

   private File writeClearTextToFile(DecryptionResult var1, File var2) throws IOException {
      File var3 = null;
      if (var2.isDirectory()) {
         String var4 = this.getOutputFileName(var1);
         if (StringUtils.isEmpty(var4)) {
            throw new IOException(
               "The given output file is a directory, but the decrypted message did not contain a file name, so I do not know what file to write to. Please use the method decryptToFolder to handle this scenario."
            );
         }

         var3 = new File(var2, var4);
      } else {
         var3 = var2;
      }

      Files.write(var1.message(), var3);
      return var3;
   }

   private String getOutputFileName(DecryptionResult var1) {
      String var2 = var1.messageName();
      if (Strings.isNullOrEmpty(var2)) {
         var2 = this.defaultOutputFileName;
      }

      return var2;
   }

   public Long getEncryptionKeyId(File var1) throws IOException, CryptoException {
      return this.worker.getEncryptionKeyId(Files.toByteArray(var1));
   }

   private static class Result implements FileDecryptionResult {
      private final File file;
      private final MessageAuthenticationStatus authStatus;
      private final MessageIntegrity integrity;

      public Result(File var1, DecryptionResult var2) {
         this.file = var1;
         this.authStatus = var2.authenticationStatus();
         this.integrity = var2.messageIntegrity();
      }

      @Override
      public MessageAuthenticationStatus authenticationStatus() {
         return this.authStatus;
      }

      @Override
      public File decryptedFile() {
         return this.file;
      }

      @Override
      public MessageIntegrity messageIntegrity() {
         return this.integrity;
      }
   }
}
