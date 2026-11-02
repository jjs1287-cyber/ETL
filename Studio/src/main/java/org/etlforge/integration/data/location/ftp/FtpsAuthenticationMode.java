package org.etlforge.integration.data.location.ftp;

import org.etlforge.system.EnumWithId;

public enum FtpsAuthenticationMode implements EnumWithId<Integer> {
   PREFER_TLS(0, "Prefer TLS Authentication"),
   PREFER_SSL(1, "Prefer SSL Authentication");

   private final int id;
   private final String display;

   FtpsAuthenticationMode(int var3, String var4) {
      this.id = var3;
      this.display = var4;
   }

   public Integer getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return this.display;
   }

   public static FtpsAuthenticationMode defaultValue() {
      return PREFER_TLS;
   }
}
