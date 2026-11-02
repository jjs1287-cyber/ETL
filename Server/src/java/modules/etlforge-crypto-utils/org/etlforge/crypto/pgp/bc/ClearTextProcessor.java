package org.etlforge.crypto.pgp.bc;

import com.google.common.io.Closeables;
import com.google.common.io.Files;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.etlforge.crypto.pgp.Encryptor;

final class ClearTextProcessor {
   private boolean armor;
   private PGPCompressedDataGenerator compressor;
   private PGPSignatureGenerator v4signer;
   private PGPOnePassSignature onePassSignature;
   private PGPEncryptedDataGenerator encryptor;
   private String messageName = "";
   private Date messageDate = new Date();

   public byte[] sign(File var1) throws Exception {
      this.prepareForFileRead(var1);
      return this.sign(Files.toByteArray(var1));
   }

   public byte[] sign(byte[] var1) throws Exception {
      byte[] var2 = this.signImpl(var1);
      byte[] var3 = this.compress(var2);
      return this.armorIfNeeded(var3);
   }

   public byte[] encrypt(File var1) throws Exception {
      this.prepareForFileRead(var1);
      return this.encrypt(Files.toByteArray(var1));
   }

   public byte[] encrypt(byte[] var1) throws Exception {
      byte[] var2 = BcUtils.literalize(var1, this.messageName, this.messageDate);
      byte[] var3 = this.compress(var2);
      byte[] var4 = this.encryptImpl(var3);
      return this.armorIfNeeded(var4);
   }

   public byte[] signAndEncrypt(File var1) throws Exception {
      this.prepareForFileRead(var1);
      return this.signAndEncrypt(Files.toByteArray(var1));
   }

   public byte[] signAndEncrypt(byte[] var1) throws Exception {
      byte[] var2 = this.signImpl(var1);
      byte[] var3 = this.compress(var2);
      byte[] var4 = this.encryptImpl(var3);
      return this.armorIfNeeded(var4);
   }

   private byte[] armorIfNeeded(byte[] var1) throws Exception {
      return this.armor ? BcUtils.armor(var1) : var1;
   }

   public void setArmor(boolean var1) {
      this.armor = var1;
   }

   public void createSignatureGenerator(File var1, long var2, char[] var4) throws Exception {
      PGPSecretKey var5 = BcUtils.readSecretKey(var1, var2);
      this.createSignatureGenerator(var5, var4);
   }

   public void createSignatureGenerator(PGPSecretKey var1, char[] var2) throws Exception {
      PGPPrivateKey var3 = var1.extractPrivateKey(var2, "BC");
      PGPSignatureGenerator var4 = new PGPSignatureGenerator(var1.getPublicKey().getAlgorithm(), 2, "BC");
      var4.initSign(0, var3);
      Iterator var5 = var1.getPublicKey().getUserIDs();
      if (var5.hasNext()) {
         PGPSignatureSubpacketGenerator var6 = new PGPSignatureSubpacketGenerator();
         var6.setSignerUserID(false, (String)var5.next());
         var4.setHashedSubpackets(var6.generate());
      }

      this.v4signer = var4;
      this.onePassSignature = this.v4signer.generateOnePassVersion(false);
   }

   public void createEncryptedDataGenerator(PGPPublicKey var1, boolean var2) throws Exception {
      PGPEncryptedDataGenerator var3 = new PGPEncryptedDataGenerator(3, var2, new SecureRandom(), "BC");
      var3.addMethod(var1);
      this.encryptor = var3;
   }

   public void createCompressor(Encryptor.Compression var1) {
      int var2 = BcUtils.getCompressionId(var1);
      this.compressor = new PGPCompressedDataGenerator(var2);
   }

   public void setMessageInfo(String var1, Date var2) {
      this.messageName = var1 != null ? var1 : "";
      this.messageDate = var2 != null ? var2 : new Date();
   }

   public void close() {
      try {
         if (this.compressor != null) {
            this.compressor.close();
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      if (this.encryptor != null) {
         try {
            this.encryptor.close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }

   private void prepareForFileRead(File var1) {
      this.setMessageInfo(var1.getName(), new Date(var1.lastModified()));
   }

   private byte[] compress(byte[] var1) throws Exception {
      OutputStream var2 = null;

      try {
         if (this.compressor != null) {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            var2 = this.compressor.open(var3);
            var2.write(var1);
            this.compressor.close();
            return var3.toByteArray();
         } else {
            return var1;
         }
      } finally {
         Closeables.closeQuietly(var2);
      }
   }

   private byte[] signImpl(byte[] var1) throws Exception {
      if (this.v4signer != null) {
         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         if (this.onePassSignature != null) {
            this.onePassSignature.encode(var2);
            var2.write(BcUtils.literalize(var1, this.messageName, this.messageDate));
            this.v4signer.update(var1);
            this.v4signer.generate().encode(var2);
         } else {
            this.v4signer.update(var1);
            this.v4signer.generate().encode(var2);
            var2.write(BcUtils.literalize(var1, this.messageName, this.messageDate));
         }

         return var2.toByteArray();
      } else {
         return var1;
      }
   }

   private byte[] encryptImpl(byte[] var1) throws Exception {
      OutputStream var2 = null;

      try {
         if (this.encryptor != null) {
            ByteArrayOutputStream var3 = new ByteArrayOutputStream();
            var2 = this.encryptor.open(var3, var1.length);
            var2.write(var1);
            this.encryptor.close();
            return var3.toByteArray();
         } else {
            return var1;
         }
      } finally {
         Closeables.closeQuietly(var2);
      }
   }
}
