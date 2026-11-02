package org.etlforge.ui.util.time;

import java.awt.Font;
import javax.swing.JComboBox;
import org.etlforge.ui.UiResources;
import org.etlforge.util.time.AM_PM;

public class AM_PM_ComboBox extends JComboBox {
   public static final String FONT_RESOURCE_KEY = AM_PM_ComboBox.class.getName() + "#font";

   public AM_PM_ComboBox() {
      super(AM_PM.values());
      Font var1 = UiResources.getDefault().getFont(FONT_RESOURCE_KEY);
      if (var1 != null) {
         this.setFont(var1);
      }
   }

   public AM_PM getSelectedItem() {
      return (AM_PM)super.getSelectedItem();
   }
}
