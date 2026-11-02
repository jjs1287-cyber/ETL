package org.etlforge.crypto.pgp.bc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.etlforge.crypto.CryptoException;
import org.etlforge.crypto.pgp.Encryptor;
import org.etlforge.crypto.pgp.Key;
import org.etlforge.io.KongaIoUtils;

final class BouncyCastleEncryptor implements Encryptor {
   private String messageName = "";
   private Date messageDate;
   private Encryptor.Compression compression = Encryptor.Compression.ZIP;

   @Override
   public byte[] encrypt(byte[] var1, Key var2, boolean var3) throws CryptoException, IOException {
      BouncyCastleEncryptor.EncryptWorker var4 = new BouncyCastleEncryptor.EncryptWorker(var2, var3, this.compression);
      var4.setMessageInfo(this.messageName, this.messageDate);
      return var4.run(var1);
   }

   @Override
   public byte[] signAndEncrypt(byte[] var1, Key var2, boolean var3, Key var4, char[] var5) throws CryptoException, IOException {
      BouncyCastleEncryptor.SignAndEncryptWorker var6 = new BouncyCastleEncryptor.SignAndEncryptWorker(var2, var3, this.compression, var4, var5);
      var6.setMessageInfo(this.messageName, this.messageDate);
      return var6.run(var1);
   }

   @Override
   public void setMessageInfo(String var1, Date var2) {
      this.messageName = var1 != null ? var1 : "";
      this.messageDate = var2 != null ? var2 : new Date();
   }

   @Override
   public void setCompression(Encryptor.Compression var1) {
      this.compression = var1;
   }

   private static class EncryptWorker extends BouncyCastleEncryptor.Worker {
      public EncryptWorker(Key var1, boolean var2, Encryptor.Compression var3) {
         super(var1, var2, var3);
      }

      @Override
      protected byte[] processBytes(ClearTextProcessor var1, byte[] var2) throws Exception {
         return var1.encrypt(var2);
      }
   }

   private static class SignAndEncryptWorker extends BouncyCastleEncryptor.Worker {
      private final Key signatureKey;
      private final char[] signatureKeyPassphrase;

      public SignAndEncryptWorker(Key var1, boolean var2, Encryptor.Compression var3, Key var4, char[] var5) {
         super(var1, var2, var3);
         this.signatureKey = var4;
         this.signatureKeyPassphrase = var5;
      }

      @Override
      protected void prepareProcessor(ClearTextProcessor var1) throws Exception {
         super.prepareProcessor(var1);
         PGPSecretKey var2 = BcUtils.readSecretKey(this.signatureKey);
         var1.createSignatureGenerator(var2, this.signatureKeyPassphrase);
      }

      @Override
      protected byte[] processBytes(ClearTextProcessor var1, byte[] var2) throws Exception {
         return var1.signAndEncrypt(var2);
      }
   }

   private abstract static class Worker {
      private final Key encryptionKey;
      private final boolean armor;
      private final ClearTextProcessor processor;
      private final Encryptor.Compression compression;

      public Worker(Key var1, boolean var2, Encryptor.Compression var3) {
         this.encryptionKey = var1;
         this.armor = var2;
         this.processor = new ClearTextProcessor();
         this.compression = var3;
      }

      public final void setMessageInfo(String var1, Date var2) {
         this.processor.setMessageInfo(var1, var2);
      }

      public final byte[] run(byte[] var1) throws CryptoException {
         try {
            this.prepareProcessor(this.processor);
            return this.processBytes(this.processor, var1);
         } catch (Exception var6) {
            throw new CryptoException(var6);
         } finally {
            this.processor.close();
         }
      }

      protected void prepareProcessor(ClearTextProcessor var1) throws Exception {
         var1.setArmor(this.armor);
         var1.createCompressor(this.compression);
         var1.createEncryptedDataGenerator(readKey(this.encryptionKey), true);
      }

      protected abstract byte[] processBytes(ClearTextProcessor var1, byte[] var2) throws Exception;

      private static PGPPublicKey readKey(Key var0) throws IOException, PGPException, CryptoException {
         if (var0.getKeyId() != null) {
            InputStream var1 = var0.getKeyData();

            try {
               PGPPublicKey var2 = PublicKeyReader.readKey(var1, var0.getKeyId());
               if (var2 != null) {
                  return var2;
               }
            } finally {
               KongaIoUtils.close(var0);
            }
         }

         if (var0.getUserId() != null) {
            InputStream var12 = var0.getKeyData();

            try {
               PGPPublicKey[] var13 = PublicKeyReader.readKeys(var12, var0.getUserId());
               if (var13.length > 0) {
                  return var13[0];
               }
            } finally {
               KongaIoUtils.close(var0);
            }

            throw new CryptoException("No matching key found in the provided key data");
         } else {
            throw new IllegalStateException("Must supply either a key ID or a user ID first");
         }
      }
   }
}
