package org.etlforge.application.ui.look;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.border.Border;

public class PanelStyle {
   private final Color background;
   private final Border border;

   public PanelStyle(Color var1, Border var2) {
      this.background = var1;
      this.border = var2;
   }

   public Color getBackground() {
      return this.background;
   }

   public Border getBorder() {
      return this.border;
   }

   public void makeOver(JComponent var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("component can't be null.");
      }

      var1.setOpaque(true);
      var1.setBackground(this.background);
      var1.setBorder(this.border);
   }
}
