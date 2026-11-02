package org.etlforge.crypto.aes;

public enum AesKeyLength {
   KEY128(128),
   KEY192(192),
   KEY256(256);

   private final int bits;

   AesKeyLength(int var3) {
      this.bits = var3;
   }

   public int getBits() {
      return this.bits;
   }
}
