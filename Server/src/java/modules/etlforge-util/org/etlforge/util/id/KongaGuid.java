package org.etlforge.util.id;

import java.math.BigInteger;
import java.util.UUID;

public final class KongaGuid extends KongaID {
   private final UUID uuid;

   public static KongaGuid createGuid() {
      return new KongaGuid();
   }

   public static KongaGuid createGuid(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("stringRep cannot be null.");
      } else {
         return new KongaGuid(var0);
      }
   }

   private KongaGuid() {
      this.uuid = UUID.randomUUID();
   }

   private KongaGuid(String var1) {
      this.uuid = UUID.fromString(var1);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof KongaGuid ? this.uuid.equals(((KongaGuid)var1).uuid) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.uuid.hashCode();
   }

   @Override
   public String toString() {
      return this.uuid.toString();
   }

   public byte[] getBytes() {
      byte[] var1 = new BigInteger(String.valueOf(this.uuid.getMostSignificantBits())).toByteArray();
      byte[] var2 = new BigInteger(String.valueOf(this.uuid.getLeastSignificantBits())).toByteArray();
      byte[] var3 = new byte[var1.length + var2.length];
      System.arraycopy(var1, 0, var3, 0, var1.length);
      System.arraycopy(var2, 0, var3, var1.length, var2.length);
      return var3;
   }
}
