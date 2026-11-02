package org.etlforge.integration.data.entity.sfquery;

import org.etlforge.system.EnumWithId;

public enum TargetHeaderMode implements EnumWithId<Integer> {
   NONE(0),
   SALESFORCE_FIELD_NAMES(1),
   TEXT_STRUCTURE_FIELD_NAMES(2);

   private final int id;

   TargetHeaderMode(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }

   @Override
   public String toString() {
      switch (this) {
         case NONE:
            return "No Header";
         case SALESFORCE_FIELD_NAMES:
            return "Salesforce Field Names";
         case TEXT_STRUCTURE_FIELD_NAMES:
            return "File Format Field Names";
         default:
            throw new RuntimeException("Unexpected mode: " + this);
      }
   }
}
