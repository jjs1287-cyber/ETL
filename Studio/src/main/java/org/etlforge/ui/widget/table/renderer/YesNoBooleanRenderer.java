package org.etlforge.ui.widget.table.renderer;

import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.ui.resources.UiResourceBundle;

public class YesNoBooleanRenderer extends DefaultTableCellRenderer {
   private static final String YES = UiResourceBundle.getString("Yes");
   private static final String NO = UiResourceBundle.getString("No");

   @Override
   protected void setValue(Object var1) {
      if (var1 instanceof Boolean) {
         super.setValue(this.getString((Boolean)var1));
      } else {
         super.setValue(var1);
      }
   }

   private String getString(Boolean var1) {
      return var1 ? YES : NO;
   }
}
