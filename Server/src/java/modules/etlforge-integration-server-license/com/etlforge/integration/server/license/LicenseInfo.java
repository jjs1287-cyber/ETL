package com.etlforge.integration.server.license;

import java.util.Date;

public final class LicenseInfo {
   private final Date expirationDate;
   private final String options;

   LicenseInfo(Date var1, String var2) {
      this.expirationDate = var1;
      this.options = var2;
   }

   public Date getExpirationDate() {
      return this.expirationDate;
   }

   public String getOptions() {
      return this.options;
   }
}
