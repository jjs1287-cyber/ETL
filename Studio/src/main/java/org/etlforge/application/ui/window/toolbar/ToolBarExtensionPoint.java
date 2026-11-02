package org.etlforge.application.ui.window.toolbar;

public enum ToolBarExtensionPoint {
   EXTENSION_POINT_1(1),
   EXTENSION_POINT_2(2),
   EXTENSION_POINT_3(3),
   EXTENSION_POINT_4(4);

   private int id;

   ToolBarExtensionPoint(int var3) {
      this.id = var3;
   }

   public static ToolBarExtensionPoint fromId(int var0) {
      for (ToolBarExtensionPoint var4 : values()) {
         if (var4.id == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Invalid id: " + var0);
   }
}
