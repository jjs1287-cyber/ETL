package org.etlforge.ui.widget.list;

import java.awt.Font;
import javax.swing.Icon;
import org.etlforge.ui.util.Fonts;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public class ItalicTextListDecorator implements ListDecorator {
   private final Object data;
   private final Icon icon;
   private final String tooltip;
   private Font font;

   public ItalicTextListDecorator(Object var1) {
      this(var1, null, null, null);
   }

   public ItalicTextListDecorator(Object var1, Icon var2, String var3, Font var4) {
      this.data = var1;
      this.icon = var2;
      this.tooltip = var3 != null ? var3 : var1.toString();
      this.font = var4 != null ? Fonts.italic(var4) : null;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      if (this.font == null) {
         this.font = Fonts.italic(var1.getListFont());
      }

      var1.setFont(this.font);
      var1.setIcon(this.icon);
      var1.setText(this.data.toString());
      var1.setToolTip(this.tooltip);
   }

   @Override
   public String toString() {
      return this.data.toString();
   }
}
