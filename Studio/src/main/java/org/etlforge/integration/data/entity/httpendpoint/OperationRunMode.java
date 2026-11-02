package org.etlforge.integration.data.entity.httpendpoint;

import org.etlforge.system.EnumWithId;

public enum OperationRunMode implements EnumWithId<Integer> {
   ASYNCHRONOUS(0, "Asynchronous"),
   SYNCHRONOUS(1, "Synchronous");

   private final int id;
   private final String display;

   OperationRunMode(int var3, String var4) {
      this.id = var3;
      this.display = var4;
   }

   @Override
   public String toString() {
      return this.display;
   }

   public Integer getId() {
      return this.id;
   }
}
