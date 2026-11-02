package org.etlforge.ui.layout;

public enum HorizontalAlignment {
   LEFT(2),
   RIGHT(4),
   CENTER(0),
   LEADING(10),
   TRAILING(11);

   private int swingConstant;

   HorizontalAlignment(int var3) {
      this.swingConstant = var3;
   }

   public int asSwingConstant() {
      return this.swingConstant;
   }
}
