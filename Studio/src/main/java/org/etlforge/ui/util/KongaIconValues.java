package org.etlforge.ui.util;

import javax.swing.Icon;
import org.jdesktop.swingx.renderer.IconValue;
import org.etlforge.ui.Empty;

public final class KongaIconValues {
   public static IconValue empty(int var0, int var1) {
      return fixedIcon(Empty.icon(var0, var1));
   }

   public static IconValue fixedIcon(final Icon var0) {
      return new IconValue() {
         @Override
         public Icon getIcon(Object var1) {
            return var0;
         }
      };
   }

   private KongaIconValues() {
   }
}
