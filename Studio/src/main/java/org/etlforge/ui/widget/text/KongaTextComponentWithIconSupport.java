package org.etlforge.ui.widget.text;

import javax.swing.Icon;
import javax.swing.text.JTextComponent;

public interface KongaTextComponentWithIconSupport extends KongaTextComponent {
   void setIcon(Icon var1);

   Icon getIcon();

   final class Wrapper {
      private final JTextComponent tc;
      private TextOverlay overlay;

      public Wrapper(JTextComponent var1) {
         this.tc = var1;
      }

      public boolean setIcon(Icon var1) {
         Icon var2 = this.getIcon();
         if (var1 == var2) {
            return false;
         }

         if (var1 == null && this.overlay == null) {
            return false;
         }

         if (this.overlay == null) {
            this.overlay = new TextOverlay(this.tc);
         }

         this.overlay.setIcon(var1);
         return true;
      }

      public Icon getIcon() {
         return this.overlay != null ? this.overlay.getIcon() : null;
      }
   }
}
