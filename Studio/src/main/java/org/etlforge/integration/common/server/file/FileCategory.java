package org.etlforge.integration.common.server.file;

import org.etlforge.system.EnumWithId;

public enum FileCategory implements EnumWithId<Integer> {
   SFDC_SUCCESS(0),
   SFDC_FAILURE(1);

   private final int id;

   FileCategory(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }
}
