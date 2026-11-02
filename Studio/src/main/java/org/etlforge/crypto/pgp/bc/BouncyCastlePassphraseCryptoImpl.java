package org.etlforge.crypto.pgp.bc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Date;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPBEEncryptedData;
import org.bouncycastle.openpgp.PGPUtil;
import org.etlforge.crypto.CryptoException;
import org.etlforge.crypto.pgp.PassphraseCrypto;
import org.etlforge.crypto.pgp.SymmetricKeyAlgorithm;

final class BouncyCastlePassphraseCryptoImpl implements PassphraseCrypto {
   @Override
   public byte[] decrypt(byte[] var1, char[] var2) throws IOException, CryptoException {
      return new BouncyCastlePassphraseCryptoImpl.DecryptWorker().goToWork(var1, var2);
   }

   @Override
   public byte[] encrypt(byte[] var1, char[] var2, SymmetricKeyAlgorithm var3) throws IOException, CryptoException {
      try {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         ByteArrayOutputStream var5 = new ByteArrayOutputStream();
         PGPCompressedDataGenerator var6 = new PGPCompressedDataGenerator(1);
         OutputStream var7 = var6.open(var5);
         PGPLiteralDataGenerator var8 = new PGPLiteralDataGenerator();
         OutputStream var9 = var8.open(var7, 'b', "_CONSOLE", var1.length, new Date());
         var9.write(var1);
         var8.close();
         var6.close();
         PGPEncryptedDataGenerator var10 = new PGPEncryptedDataGenerator(SymmetricKeyAlgorithmMap.convert(var3), new SecureRandom(), "BC");
         var10.addMethod(var2);
         byte[] var11 = var5.toByteArray();
         OutputStream var12 = var10.open(var4, var11.length);
         var12.write(var11);
         var12.close();
         return var4.toByteArray();
      } catch (NoSuchProviderException var13) {
         throw new CryptoException(var13.getMessage(), var13);
      } catch (PGPException var14) {
         throw new CryptoException(var14.getMessage(), var14);
      }
   }

   private static class DecryptWorker {
      private DecryptWorker() {
      }

      public byte[] goToWork(byte[] var1, char[] var2) throws IOException, CryptoException {
         try {
            PGPPBEEncryptedData var3 = this.extractEncryptedData(var1);
            PGPLiteralData var4 = this.extractLiteralData(var3, var2);
            return this.decryptLiteralData(var4);
         } catch (NoSuchProviderException var5) {
            throw new CryptoException(var5.getMessage(), var5);
         } catch (PGPException var6) {
            throw new CryptoException(var6.getMessage(), var6);
         }
      }

      private PGPPBEEncryptedData extractEncryptedData(byte[] var1) throws IOException {
         PGPObjectFactory var2 = this.createObjectFactory(var1);
         PGPEncryptedDataList var3 = null;
         Object var4 = var2.nextObject();
         if (var4 instanceof PGPEncryptedDataList) {
            var3 = (PGPEncryptedDataList)var4;
         } else {
            var3 = (PGPEncryptedDataList)var2.nextObject();
         }

         return (PGPPBEEncryptedData)var3.get(0);
      }

      private PGPObjectFactory createObjectFactory(byte[] var1) throws IOException {
         InputStream var2 = PGPUtil.getDecoderStream(new ByteArrayInputStream(var1));
         return new PGPObjectFactory(var2);
      }

      private PGPLiteralData extractLiteralData(PGPPBEEncryptedData var1, char[] var2) throws IOException, PGPException, NoSuchProviderException {
         InputStream var3 = var1.getDataStream(var2, "BC");
         PGPObjectFactory var4 = new PGPObjectFactory(var3);
         PGPCompressedData var5 = (PGPCompressedData)var4.nextObject();
         var4 = new PGPObjectFactory(var5.getDataStream());
         return (PGPLiteralData)var4.nextObject();
      }

      private byte[] decryptLiteralData(PGPLiteralData var1) throws IOException {
         InputStream var2 = var1.getInputStream();
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();

         int var4;
         while ((var4 = var2.read()) >= 0) {
            var3.write(var4);
         }

         byte[] var5 = var3.toByteArray();
         var3.close();
         return var5;
      }
   }
}
