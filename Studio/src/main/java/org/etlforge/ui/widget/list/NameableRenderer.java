package org.etlforge.ui.widget.list;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.util.name.Nameable;

public class NameableRenderer extends DefaultListCellRenderer {
   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      if (var2 instanceof Nameable) {
         String var6 = this.getDisplayText(var2);
         this.setText(var6);
      }

      return this;
   }

   private String getDisplayText(Object var1) {
      return var1 instanceof Nameable ? ((Nameable)var1).getName() : this.getDisplayTextForNonNameable(var1);
   }

   protected String getDisplayTextForNonNameable(Object var1) {
      return var1 == null ? "" : var1.toString();
   }
}
