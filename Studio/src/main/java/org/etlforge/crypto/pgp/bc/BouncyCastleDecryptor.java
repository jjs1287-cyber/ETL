package org.etlforge.crypto.pgp.bc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.etlforge.crypto.CryptoException;
import org.etlforge.crypto.pgp.DecryptionResult;
import org.etlforge.crypto.pgp.Decryptor;
import org.etlforge.crypto.pgp.Key;
import org.etlforge.crypto.pgp.MessageAuthenticationStatus;
import org.etlforge.crypto.pgp.MessageIntegrity;
import org.etlforge.io.KongaIoUtils;

final class BouncyCastleDecryptor implements Decryptor {
   private Key verificationKey;

   @Override
   public DecryptionResult decrypt(byte[] var1, Key var2, char[] var3) throws IOException, CryptoException {
      try {
         BouncyCastleDecryptor.Worker var4 = new BouncyCastleDecryptor.Worker();
         return var4.goToWork(var1, var2, var3, this.verificationKey);
      } catch (SignatureException var5) {
         throw new CryptoException(var5);
      } catch (NoSuchProviderException var6) {
         throw new CryptoException(var6);
      } catch (PGPException var7) {
         throw new CryptoException(var7);
      }
   }

   @Override
   public void setVerificationKey(Key var1) {
      this.verificationKey = var1;
   }

   @Override
   public Long getEncryptionKeyId(byte[] var1) throws IOException {
      BouncyCastleDecryptor.Worker var2 = new BouncyCastleDecryptor.Worker();
      return var2.getDecryptionKeyId(var1);
   }

   private static final class Result implements DecryptionResult {
      private final byte[] message;
      private final String name;
      private final MessageAuthenticationStatus authenticationStatus;
      private final MessageIntegrity messageIntegrity;

      public Result(byte[] var1, String var2, MessageAuthenticationStatus var3, MessageIntegrity var4) {
         this.message = var1;
         this.name = var2;
         this.authenticationStatus = var3;
         this.messageIntegrity = var4;
      }

      @Override
      public byte[] message() {
         return this.message;
      }

      @Override
      public String messageName() {
         return this.name;
      }

      @Override
      public MessageAuthenticationStatus authenticationStatus() {
         return this.authenticationStatus;
      }

      @Override
      public MessageIntegrity messageIntegrity() {
         return this.messageIntegrity;
      }
   }

   private static class Worker {
      private static final String PROVIDER_ID = "BC";
      private PGPPrivateKey secretKey;
      private Key verificationKey;
      private PGPEncryptedDataList encryptedDataList;
      private PGPPublicKeyEncryptedData encryptedData;
      private PGPLiteralData message;
      private PGPOnePassSignature onePassSignature;
      private MessageAuthenticationStatus authenticationStatus;

      private Worker() {
      }

      public Long getDecryptionKeyId(byte[] var1) throws IOException {
         InputStream var2 = null;

         try {
            var2 = this.openFile(var1);
            this.extractEncryptedDataList(var2);
            return this.readEncryptionKeyId();
         } finally {
            KongaIoUtils.close(var2);
         }
      }

      public DecryptionResult goToWork(byte[] var1, Key var2, char[] var3, Key var4) throws IOException, PGPException, SignatureException, NoSuchProviderException {
         this.verificationKey = var4;
         this.authenticationStatus = MessageAuthenticationStatus.NOT_SIGNED;
         InputStream var5 = null;

         try {
            var5 = this.openFile(var1);
            this.extractEncryptedDataList(var5);
            this.extractPrivateKeyAndData(var2, var3);
            byte[] var6 = this.extractMessage();
            MessageIntegrity var7 = this.verifyIntegrity();
            return new BouncyCastleDecryptor.Result(var6, this.message.getFileName(), this.authenticationStatus, var7);
         } finally {
            KongaIoUtils.close(var5);
         }
      }

      private InputStream openFile(byte[] var1) throws IOException {
         return PGPUtil.getDecoderStream(new ByteArrayInputStream(var1));
      }

      private void extractEncryptedDataList(InputStream var1) throws IOException {
         PGPObjectFactory var2 = new PGPObjectFactory(var1);
         Object var3 = var2.nextObject();
         if (var3 instanceof PGPEncryptedDataList) {
            this.encryptedDataList = (PGPEncryptedDataList)var3;
         } else {
            this.encryptedDataList = (PGPEncryptedDataList)var2.nextObject();
         }
      }

      private Long readEncryptionKeyId() {
         Iterator var1 = this.encryptedDataList.getEncryptedDataObjects();
         if (var1.hasNext()) {
            PGPPublicKeyEncryptedData var2 = (PGPPublicKeyEncryptedData)var1.next();
            return var2.getKeyID();
         } else {
            return null;
         }
      }

      private void extractPrivateKeyAndData(Key var1, char[] var2) throws PGPException, NoSuchProviderException, IOException {
         InputStream var3 = null;

         try {
            var3 = var1.getKeyData();
            Iterator var4 = this.encryptedDataList.getEncryptedDataObjects();
            PGPSecretKeyRingCollection var5 = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(var3));

            while (this.secretKey == null && var4.hasNext()) {
               this.encryptedData = (PGPPublicKeyEncryptedData)var4.next();
               this.secretKey = findSecretKey(var5, this.encryptedData.getKeyID(), var2);
            }

            if (this.secretKey == null) {
               throw new PGPException("No matching key found in the provided key file");
            }
         } finally {
            KongaIoUtils.close(var3);
         }
      }

      private byte[] extractMessage() throws IOException, PGPException, SignatureException, NoSuchProviderException {
         byte[] var1 = null;
         InputStream var2 = this.encryptedData.getDataStream(this.secretKey, "BC");
         PGPObjectFactory var3 = new PGPObjectFactory(var2);
         Object var4 = var3.nextObject();
         if (var4 instanceof PGPCompressedData) {
            PGPCompressedData var5 = (PGPCompressedData)var4;
            var3 = new PGPObjectFactory(var5.getDataStream());
            var4 = var3.nextObject();
         }

         for (; var4 != null; var4 = var3.nextObject()) {
            if (var4 instanceof PGPLiteralData) {
               this.message = (PGPLiteralData)var4;
               var1 = this.writeOutputFile();
            } else if (var4 instanceof PGPOnePassSignatureList) {
               this.processSignatureList(var4);
            } else {
               if (!(var4 instanceof PGPSignatureList)) {
                  throw new PGPException("message is not a simple encrypted file - type unknown.");
               }

               this.verifySignature(var4);
            }
         }

         return var1;
      }

      private void processSignatureList(Object var1) throws FileNotFoundException, IOException, PGPException, NoSuchProviderException {
         if (this.verificationKey != null) {
            PGPOnePassSignatureList var2 = (PGPOnePassSignatureList)var1;
            this.onePassSignature = var2.get(0);
            long var3 = this.onePassSignature.getKeyID();
            PGPPublicKey var5 = this.loadPublicKey(var3);
            if (var5 != null) {
               this.onePassSignature.initVerify(var5, "BC");
            } else {
               this.authenticationStatus = MessageAuthenticationStatus.AUTHENTICATION_FAILED;
               this.onePassSignature = null;
            }
         }
      }

      private void verifySignature(Object var1) throws PGPException, SignatureException {
         if (this.onePassSignature != null) {
            PGPSignatureList var2 = (PGPSignatureList)var1;
            if (this.onePassSignature.verify(var2.get(0))) {
               this.authenticationStatus = MessageAuthenticationStatus.AUTHENTICATION_VERIFIED;
            } else {
               this.authenticationStatus = MessageAuthenticationStatus.AUTHENTICATION_FAILED;
            }
         }
      }

      private PGPPublicKey loadPublicKey(long var1) throws FileNotFoundException, IOException, PGPException {
         InputStream var3 = this.verificationKey.getKeyData();

         PGPPublicKeyRingCollection var4;
         try {
            var4 = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(var3));
         } finally {
            KongaIoUtils.close(var3);
         }

         return var4.getPublicKey(var1);
      }

      private byte[] writeOutputFile() throws IOException, SignatureException {
         ByteArrayOutputStream var1 = null;

         try {
            var1 = new ByteArrayOutputStream();
            InputStream var2 = this.message.getInputStream();
            int var3;
            if (this.onePassSignature == null) {
               IOUtils.copy(var2, var1);
            } else {
               while ((var3 = var2.read()) >= 0) {
                  this.onePassSignature.update((byte)var3);
                  var1.write(var3);
               }
            }

            return var1.toByteArray();
         } finally {
            KongaIoUtils.close(var1);
         }
      }

      private MessageIntegrity verifyIntegrity() throws IOException, PGPException {
         MessageIntegrity var1;
         if (this.encryptedData.isIntegrityProtected()) {
            var1 = this.encryptedData.verify() ? MessageIntegrity.INTEGRITY_VERIFIED : MessageIntegrity.INTEGRITY_FAILED;
         } else {
            var1 = MessageIntegrity.NOT_VERIFIED;
         }

         return var1;
      }

      private static PGPPrivateKey findSecretKey(PGPSecretKeyRingCollection var0, long var1, char[] var3) throws PGPException, NoSuchProviderException {
         PGPSecretKey var4 = var0.getSecretKey(var1);
         return var4 == null ? null : var4.extractPrivateKey(var3, "BC");
      }
   }
}
