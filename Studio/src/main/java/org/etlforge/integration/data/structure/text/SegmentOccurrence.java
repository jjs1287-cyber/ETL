package org.etlforge.integration.data.structure.text;

public enum SegmentOccurrence {
   OnlyOnce("Only Once"),
   OneOrMore("One or More"),
   ZeroOrOne("Zero or One"),
   ZeroOrMore("Zero or More");

   private final String name;

   SegmentOccurrence(String var3) {
      this.name = var3;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public static SegmentOccurrence fromString(String var0) {
      for (SegmentOccurrence var4 : values()) {
         if (var4.name.equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid value: " + var0);
   }
}
