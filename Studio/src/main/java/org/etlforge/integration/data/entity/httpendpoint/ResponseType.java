package org.etlforge.integration.data.entity.httpendpoint;

import org.etlforge.system.EnumWithId;

public enum ResponseType implements EnumWithId<Integer> {
   NONE(0),
   OPERATION(1),
   VARIABLE(2);

   private final int id;

   ResponseType(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }
}
