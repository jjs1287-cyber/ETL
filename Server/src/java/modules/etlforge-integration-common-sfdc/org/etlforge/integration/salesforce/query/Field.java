package org.etlforge.integration.salesforce.query;

import org.etlforge.system.ArgCheck;

public final class Field {
   private final String name;
   private final Object value;

   public Field(String var1, Object var2) {
      this.name = ArgCheck.checkNotBlank(var1);
      this.value = var2;
   }

   public String getName() {
      return this.name;
   }

   public String getValueAsString() {
      return this.value == null ? "" : this.value.toString();
   }
}
