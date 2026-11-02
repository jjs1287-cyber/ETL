package org.etlforge.integration.data.location.ftp;

import java.util.EnumSet;
import org.etlforge.system.EnumWithId;

public enum FtpSecurityOption implements EnumWithId<Integer> {
   NONE(0, "None"),
   TRY_SSL(1, "Try SSL"),
   REQUIRE_SSL_FOR_CONTROL(2, "Require TLS/SSL for control connection"),
   REQUIRE_SSL_FOR_ALL(3, "Require TLS/SSL for all data transfer"),
   SFTP(4, "SFTP");

   private final int id;
   private final String displayString;

   FtpSecurityOption(int var3, String var4) {
      this.id = var3;
      this.displayString = var4;
   }

   public Integer getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return this.displayString;
   }

   public static FtpSecurityOption defaultOption() {
      return NONE;
   }

   public static EnumSet<FtpSecurityOption> getSslOptions() {
      return EnumSet.of(TRY_SSL, REQUIRE_SSL_FOR_CONTROL, REQUIRE_SSL_FOR_ALL);
   }
}
