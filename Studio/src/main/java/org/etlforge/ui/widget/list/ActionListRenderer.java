package org.etlforge.ui.widget.list;

import java.awt.Component;
import java.awt.Font;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import org.etlforge.ui.resources.UiResourceBundle;

public class ActionListRenderer extends DefaultListCellRenderer {
   private final boolean italicAndBold;

   public static ActionListRenderer withItalicAndBoldFont() {
      return new ActionListRenderer(true);
   }

   public ActionListRenderer() {
      this(false);
   }

   private ActionListRenderer(boolean var1) {
      this.italicAndBold = var1;
   }

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      Font var6 = var1.getFont();
      if (var2 instanceof Action) {
         Action var7 = (Action)var2;
         String var8 = (String)var7.getValue("Name");
         String var9 = UiResourceBundle.MNEMONIC_FACTORY.asText(var8);
         this.setIcon((Icon)var7.getValue("SmallIcon"));
         this.setText(var9);
         this.setToolTipText((String)var7.getValue("ShortDescription"));
         this.setEnabled(var7.isEnabled());
         if (this.italicAndBold) {
            var6 = var6.deriveFont(3);
         }
      }

      this.setFont(var6);
      return this;
   }
}
