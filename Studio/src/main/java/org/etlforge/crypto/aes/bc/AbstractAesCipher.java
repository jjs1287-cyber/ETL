package org.etlforge.crypto.aes.bc;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.etlforge.crypto.CryptoException;
import org.etlforge.crypto.aes.AesCipher;
import org.etlforge.crypto.aes.AesKeyLength;

abstract class AbstractAesCipher implements AesCipher {
   private AesKeyLength keyLength = AesKeyLength.KEY128;
   private char[] passphrase = new char[0];
   private byte[] salt = null;
   private int iterationCount = 1;

   protected AbstractAesCipher() {
   }

   @Override
   public void decrypt(File var1, File var2) throws IOException, CryptoException {
      try {
         BufferedBlockCipher var3 = this.createCipher(false);
         this.decryptFile(var1, var2, var3);
      } catch (InvalidCipherTextException var4) {
         throw new CryptoException(var4.getMessage(), var4);
      }
   }

   @Override
   public byte[] decrypt(byte[] var1) throws IOException, CryptoException {
      try {
         BufferedBlockCipher var2 = this.createCipher(false);
         return this.decryptBytes(var1, var2);
      } catch (InvalidCipherTextException var3) {
         throw new CryptoException(var3.getMessage(), var3);
      }
   }

   @Override
   public void encrypt(File var1, File var2) throws IOException, CryptoException {
      try {
         BufferedBlockCipher var3 = this.createCipher(true);
         this.encryptFile(var1, var2, var3);
      } catch (InvalidCipherTextException var4) {
         throw new CryptoException(var4.getMessage(), var4);
      }
   }

   @Override
   public byte[] encrypt(byte[] var1) throws IOException, CryptoException {
      try {
         BufferedBlockCipher var2 = this.createCipher(true);
         return this.encryptBytes(var1, var2);
      } catch (InvalidCipherTextException var3) {
         throw new CryptoException(var3.getMessage(), var3);
      }
   }

   @Override
   public void setIterationCount(int var1) {
      Preconditions.checkArgument(var1 >= 1, "count must be >= 1 (was " + var1 + ")");
      this.iterationCount = var1;
   }

   @Override
   public void setKeyLength(AesKeyLength var1) {
      Preconditions.checkNotNull(var1, "length");
      this.keyLength = var1;
   }

   @Override
   public void setPassphrase(char[] var1) {
      this.passphrase = new char[var1.length];
      System.arraycopy(var1, 0, this.passphrase, 0, var1.length);
   }

   @Override
   public void setSalt(byte[] var1) {
      if (var1 == null) {
         this.salt = null;
      } else {
         this.salt = new byte[var1.length];
         System.arraycopy(var1, 0, this.salt, 0, var1.length);
      }
   }

   protected abstract PBEParametersGenerator createPBEParametersGenerator();

   private BufferedBlockCipher createCipher(boolean var1) {
      AESEngine var2 = new AESEngine();
      CBCBlockCipher var3 = new CBCBlockCipher(var2);
      PaddedBufferedBlockCipher var4 = new PaddedBufferedBlockCipher(var3);
      CipherParameters var5 = this.createCipherParams();
      var4.init(var1, var5);
      return var4;
   }

   private CipherParameters createCipherParams() {
      PBEParametersGenerator var1 = this.createPBEParametersGenerator();
      this.initPBEParamsGenerator(var1);
      return var1.generateDerivedParameters(this.keyLength.getBits(), 128);
   }

   private void initPBEParamsGenerator(PBEParametersGenerator var1) {
      var1.init(PBEParametersGenerator.PKCS5PasswordToBytes(this.passphrase), this.salt, this.iterationCount);
   }

   private byte[] decryptBytes(byte[] var1, BufferedBlockCipher var2) throws IOException, InvalidCipherTextException {
      byte[] var3 = new byte[var2.getOutputSize(var1.length)];
      ByteArrayOutputStream var4 = new ByteArrayOutputStream();
      int var5 = var2.processBytes(var1, 0, var1.length, var3, 0);
      if (var5 > 0) {
         var4.write(var3, 0, var5);
      }

      var5 = var2.doFinal(var3, 0);
      if (var5 > 0) {
         var4.write(var3, 0, var5);
      }

      var4.close();
      return var4.toByteArray();
   }

   private void decryptFile(File var1, File var2, BufferedBlockCipher var3) throws IOException, InvalidCipherTextException {
      byte[] var4 = Files.toByteArray(var1);
      byte[] var5 = this.decryptBytes(var4, var3);
      Files.write(var5, var2);
   }

   private byte[] encryptBytes(byte[] var1, BufferedBlockCipher var2) throws IOException, InvalidCipherTextException {
      int var3 = 0;
      byte[] var4 = new byte[var2.getOutputSize(var1.length)];
      ByteArrayOutputStream var5 = new ByteArrayOutputStream();
      var3 = var2.processBytes(var1, 0, var1.length, var4, 0);
      if (var3 > 0) {
         var5.write(var4, 0, var3);
      }

      var3 = var2.doFinal(var4, 0);
      if (var3 > 0) {
         var5.write(var4, 0, var3);
      }

      var5.close();
      return var5.toByteArray();
   }

   private void encryptFile(File var1, File var2, BufferedBlockCipher var3) throws IOException, InvalidCipherTextException {
      byte[] var4 = Files.toByteArray(var1);
      byte[] var5 = this.encryptBytes(var4, var3);
      Files.write(var5, var2);
   }
}
