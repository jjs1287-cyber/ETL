package org.etlforge.integration.data.entity.message;

public enum SmtpServerType {
   IMAP(0),
   POP3(1);

   private final int id;

   SmtpServerType(int var3) {
      this.id = var3;
   }

   public int getId() {
      return this.id;
   }

   public static SmtpServerType fromId(int var0) {
      for (SmtpServerType var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid SmtpServerType id: " + var0);
   }
}
