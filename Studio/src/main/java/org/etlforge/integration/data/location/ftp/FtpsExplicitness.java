package org.etlforge.integration.data.location.ftp;

import org.etlforge.system.EnumWithId;

public enum FtpsExplicitness implements EnumWithId<Integer> {
   EXPLICIT(1),
   IMPLICIT(0);

   private final int id;

   FtpsExplicitness(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }

   public static FtpsExplicitness defaultValue() {
      return EXPLICIT;
   }
}
