package org.etlforge.util.crypto;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public final class DefaultPBEStringCryptographer implements PBEStringCryptographer {
   private static final int DEFAULT_ITERARION_COUNT = 19;
   private static final String DEFAULT_ALGORITHM = "PBEWithMD5AndDES";
   private final byte[] salt;
   private final int iterationCount;
   private Cipher ecipher;
   private Cipher dcipher;
   private String algorithm;

   public DefaultPBEStringCryptographer() throws NoSuchAlgorithmException {
      this(generateRandomSalt(8), 19);
   }

   public DefaultPBEStringCryptographer(byte[] var1) {
      this(var1, 19);
   }

   public DefaultPBEStringCryptographer(byte[] var1, int var2) {
      this.salt = (byte[])var1.clone();
      this.iterationCount = var2;
      this.algorithm = "PBEWithMD5AndDES";
   }

   @Override
   public void setAlgorithm(String var1) {
      this.algorithm = var1;
   }

   @Override
   public void setPassphrase(String var1) throws StringCryptographerException {
      try {
         PBEKeySpec var2 = new PBEKeySpec(var1.toCharArray(), this.salt, this.iterationCount);
         SecretKey var3 = SecretKeyFactory.getInstance(this.algorithm).generateSecret(var2);
         this.ecipher = Cipher.getInstance(var3.getAlgorithm());
         this.dcipher = Cipher.getInstance(var3.getAlgorithm());
         PBEParameterSpec var4 = new PBEParameterSpec(this.salt, this.iterationCount);
         this.ecipher.init(1, var3, var4);
         this.dcipher.init(2, var3, var4);
      } catch (InvalidKeyException var5) {
         throw new StringCryptographerException(var5.getMessage(), var5);
      } catch (InvalidKeySpecException var6) {
         throw new StringCryptographerException(var6.getMessage(), var6);
      } catch (NoSuchAlgorithmException var7) {
         throw new StringCryptographerException(var7.getMessage(), var7);
      } catch (NoSuchPaddingException var8) {
         throw new StringCryptographerException(var8.getMessage(), var8);
      } catch (InvalidAlgorithmParameterException var9) {
         throw new StringCryptographerException(var9.getMessage(), var9);
      }
   }

   @Override
   public byte[] encrypt(String var1) throws StringEncryptionException {
      Preconditions.checkState(this.ecipher != null, "A passphrase has not been specified.");

      try {
         byte[] var2 = var1.getBytes(Charsets.UTF_8.name());
         return this.ecipher.doFinal(var2);
      } catch (UnsupportedEncodingException var3) {
         throw new StringEncryptionException(var3.getMessage(), var3);
      } catch (IllegalBlockSizeException var4) {
         throw new StringEncryptionException(var4.getMessage(), var4);
      } catch (BadPaddingException var5) {
         throw new StringEncryptionException(var5.getMessage(), var5);
      }
   }

   @Override
   public String decrypt(byte[] var1) throws StringDecrypterException {
      Preconditions.checkState(this.dcipher != null, "A passphrase has not been specified.");

      try {
         byte[] var2 = this.dcipher.doFinal(var1);
         return new String(var2, Charsets.UTF_8.name());
      } catch (IllegalBlockSizeException var3) {
         throw new StringDecrypterException(var3.getMessage(), var3);
      } catch (BadPaddingException var4) {
         throw new StringDecrypterException(var4.getMessage(), var4);
      } catch (UnsupportedEncodingException var5) {
         throw new StringDecrypterException(var5.getMessage(), var5);
      }
   }

   public static byte[] generateRandomSalt(int var0) throws NoSuchAlgorithmException {
      byte[] var1 = new byte[var0];
      SecureRandom var2 = SecureRandom.getInstance("SHA1PRNG");
      var2.nextBytes(var1);
      System.out.println(Arrays.toString(var1));
      return var1;
   }
}
