package org.etlforge.application.ui.menu;

import org.etlforge.system.EnumWithId;

public enum MenuExtensionPoint implements EnumWithId<Integer> {
   EXTENSION_POINT_1(1),
   EXTENSION_POINT_2(2),
   EXTENSION_POINT_3(3),
   EXTENSION_POINT_4(4);

   private final int id;

   MenuExtensionPoint(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }
}
