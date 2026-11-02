package com.etlforge.integration.server.license;

import com.google.common.base.Charsets;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;

public final class MaskingStrategy {
   private static final String MASKING_STRING = "((a screaming comes across the sky))";

   public static String mask(String var0) {
      byte[] var1 = createXor(var0.getBytes(Charsets.UTF_8));
      return new String(Base64.encodeBase64(var1), Charsets.UTF_8);
   }

   private static byte[] createXor(byte[] var0) {
      byte[] var1 = getMaskerBytes(var0.length);
      byte[] var2 = new byte[var0.length];

      for (int var3 = 0; var3 < var0.length; var3++) {
         var2[var3] = (byte)(var0[var3] ^ var1[var3]);
      }

      return var2;
   }

   private static byte[] getMaskerBytes(int var0) {
      byte[] var1 = "((a screaming comes across the sky))".getBytes(Charsets.UTF_8);
      if (var1.length == var0) {
         return var1;
      }

      byte[] var2 = new byte[var0];
      int var3 = 0;

      while (var3 < var0) {
         int var4 = var0 - var3;
         int var5 = Math.min(var4, var1.length);
         System.arraycopy(var1, 0, var2, var3, var5);
         var3 += var5;
      }

      return var2;
   }

   public static String unmask(String var0) {
      byte[] var1 = Base64.decodeBase64(var0.getBytes(Charsets.UTF_8));
      byte[] var2 = createXor(var1);
      return new String(var2, Charsets.UTF_8);
   }

   public static void main(String[] var0) {
      String var1 = UUID.randomUUID().toString();
      String var2 = mask(var1);
      String var3 = unmask(var2);
      System.out.println("original: " + var1);
      System.out.println("masked: " + var2);
      System.out.println("unmasked: " + var3 + "\n" + var3.equals(var1));
   }

   private MaskingStrategy() {
   }
}
