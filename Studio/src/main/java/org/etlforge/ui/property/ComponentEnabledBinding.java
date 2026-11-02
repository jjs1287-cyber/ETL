package org.etlforge.ui.property;

import javax.swing.JComponent;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ComponentEnabledBinding extends AbstractBinding<Boolean> {
   private final JComponent component;

   public ComponentEnabledBinding(AbstractPropertyBasedModel var1, String var2, JComponent var3) {
      super(var1, var2);
      this.component = var3;
   }

   public ComponentEnabledBinding(Property<Boolean> var1, JComponent var2) {
      super(var1);
      this.component = var2;
   }

   protected void updateUiWithValue(Boolean var1) {
      this.component.setEnabled(var1);
   }
}
