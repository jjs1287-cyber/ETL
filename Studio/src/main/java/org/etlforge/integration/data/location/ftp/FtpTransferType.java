package org.etlforge.integration.data.location.ftp;

public enum FtpTransferType {
   Ascii(1),
   Binary(2);

   private final int id;

   public int getId() {
      return this.id;
   }

   public static FtpTransferType getFromId(int var0) {
      for (FtpTransferType var4 : values()) {
         if (var4.getId() == var0) {
            return var4;
         }
      }

      return null;
   }

   FtpTransferType(int var3) {
      this.id = var3;
   }
}
