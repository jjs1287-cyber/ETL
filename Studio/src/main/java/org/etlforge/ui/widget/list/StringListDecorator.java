package org.etlforge.ui.widget.list;

import java.awt.Font;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public final class StringListDecorator implements ListDecorator {
   private final String value;
   private final Font font;

   public StringListDecorator(String var1) {
      this(var1, null);
   }

   public StringListDecorator(String var1, Font var2) {
      this.value = var1;
      this.font = var2;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setText(this.value);
      var1.setToolTip(this.value);
      var1.setIcon(null);
      if (this.font != null) {
         var1.setFont(this.font);
      }
   }

   @Override
   public String toString() {
      return this.value;
   }
}
