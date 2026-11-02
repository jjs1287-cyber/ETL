package org.etlforge.integration.client.ui.interchange.entity.script.page;

import org.etlforge.integration.client.entity.settings.EntitySettings;
import org.etlforge.integration.data.entity.Script;

final class CaretPositionMemento {
   public static final CaretPositionMemento NONE = new CaretPositionMemento(null, null);
   private static final String KEY = "CaretPosition";
   private final Script script;
   private final EntitySettings settings;

   public CaretPositionMemento(Script var1, EntitySettings var2) {
      this.script = var1;
      this.settings = var2;
   }

   public void store(int var1) {
      if (this.settings != null) {
         this.settings.getEntitySettings(this.script).putInteger("CaretPosition", var1);
         this.settings.persist();
      }
   }

   public int restore() {
      if (this.settings != null) {
         Integer var1 = this.settings.getEntitySettings(this.script).getInteger("CaretPosition");
         if (var1 != null) {
            return var1;
         }
      }

      return -1;
   }
}
