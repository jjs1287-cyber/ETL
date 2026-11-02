package org.etlforge.integration.data.structure.text;

import java.util.EnumSet;

public enum FieldDataType {
   String(1),
   Integer(2),
   Date(3),
   Long(4),
   Float(5),
   Double(6),
   Boolean(7),
   Segment(8),
   Identifier(9);

   private final int intVal;

   FieldDataType(int var3) {
      this.intVal = var3;
   }

   public int intVal() {
      return this.intVal;
   }

   public static EnumSet<FieldDataType> allValueTypes() {
      return EnumSet.range(String, Boolean);
   }

   public static EnumSet<FieldDataType> getVisibleTypes(boolean var0) {
      EnumSet var1 = EnumSet.allOf(FieldDataType.class);
      if (!var0) {
         var1.remove(Identifier);
      }

      var1.remove(Segment);
      return var1;
   }

   public boolean isNumeric() {
      switch (this) {
         case Integer:
         case Long:
         case Float:
         case Double:
            return true;
         default:
            return false;
      }
   }
}
