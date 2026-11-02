package org.etlforge.integration.database;

import org.etlforge.system.EnumWithId;

public enum DbInsertMode implements EnumWithId<Integer> {
   INSERT_UPDATE(1),
   INSERT_ONLY(2),
   UPDATE_ONLY(3),
   UPDATE_INSERT(4),
   DELETE(5),
   SOURCE_UPDATE(6);

   private final int id;

   DbInsertMode(int var3) {
      this.id = var3;
   }

   public Integer getId() {
      return this.id;
   }
}
