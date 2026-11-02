package org.etlforge.integration.data.structure.database;

public enum RelationshipCardinality {
   ONE_TO_N,
   ONE_TO_ONE,
   ZERO_TO_N,
   ZERO_OR_ONE;

   public boolean isOuterJoin() {
      switch (this) {
         case ZERO_TO_N:
         case ZERO_OR_ONE:
            return true;
         case ONE_TO_N:
         case ONE_TO_ONE:
            return false;
         default:
            return false;
      }
   }
}
