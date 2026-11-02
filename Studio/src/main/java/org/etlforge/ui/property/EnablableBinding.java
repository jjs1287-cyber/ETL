package org.etlforge.ui.property;

import org.etlforge.ui.Enablable;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class EnablableBinding extends AbstractBinding<Boolean> {
   private boolean reversed;
   private final Enablable component;

   public static EnablableBinding bind(AbstractPropertyBasedModel var0, String var1, Enablable var2) {
      Property var3 = var0.getProperty(var1);
      return new EnablableBinding(var3, var2);
   }

   public static EnablableBinding bind(Property<Boolean> var0, Enablable var1) {
      return new EnablableBinding(var0, var1);
   }

   public EnablableBinding(Property<Boolean> var1, Enablable var2) {
      super(var1);
      this.component = var2;
   }

   public void reverse() {
      if (!this.reversed) {
         this.reversed = true;
         this.syncUi();
      }
   }

   protected void updateUiWithValue(Boolean var1) {
      boolean var2 = this.reversed ? !var1 : var1;
      this.component.setEnabled(var2);
   }
}
