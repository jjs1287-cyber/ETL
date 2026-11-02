package org.etlforge.integration.client.info;

import org.etlforge.system.EnumWithId;

public enum ClientEdition implements EnumWithId<String> {
   ENTERPRISE("enterprise"),
   PROFESSIONAL("professional"),
   SFDC_DATA_LOADER("sfdc_dataloader"),
   UNKNOWN("unknown");

   private final String id;

   ClientEdition(String var3) {
      this.id = var3;
   }

   public String getId() {
      return this.id;
   }
}
