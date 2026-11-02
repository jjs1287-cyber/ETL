package org.etlforge.ui.widget.list;

import java.awt.Font;
import javax.swing.Icon;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public class BoldTextListDecorator implements ListDecorator {
   private final Object data;
   private final Icon icon;
   private final String tooltip;
   private final Font font;

   public BoldTextListDecorator(Object var1, Font var2) {
      this(var1, null, var1.toString(), var2);
   }

   public BoldTextListDecorator(Object var1, Icon var2, String var3, Font var4) {
      this.data = var1;
      this.icon = var2;
      this.tooltip = var3 != null ? var3 : var1.toString();
      this.font = Fonts.bold(var4);
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setFont(this.font);
      var1.setIcon(this.icon);
      var1.setText(this.data.toString());
      var1.setToolTip(this.tooltip);
   }
}
