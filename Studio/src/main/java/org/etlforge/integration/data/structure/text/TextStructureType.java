package org.etlforge.integration.data.structure.text;

public enum TextStructureType {
   SIMPLE,
   COMPLEX;

   @Override
   public String toString() {
      switch (this) {
         case SIMPLE:
            return "Simple";
         case COMPLEX:
            return "Complex";
         default:
            throw new AssertionError();
      }
   }
}
