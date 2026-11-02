package org.etlforge.ui.widget.window;

import java.awt.Dimension;
import java.util.Comparator;
import javax.swing.JWindow;

public class WindowSizeHandler {
   public Dimension preferredSize;
   public Dimension maximumSize;
   public Dimension minimumSize;

   public void setPreferredSize(JWindow var1) {
      if (this.preferredSize != null) {
         var1.setPreferredSize(this.preferredSize);
      }
   }

   public void enforceLimits(JWindow var1) {
      this.checkMinimumSize(var1);
      this.checkMaximumSize(var1);
   }

   private void checkMaximumSize(JWindow var1) {
      if (this.maximumSize != null) {
         applySizeLimit(var1, this.maximumSize, new Comparator<Integer>() {
            public int compare(Integer var1, Integer var2) {
               return var1.compareTo(var2);
            }
         });
      }
   }

   private void checkMinimumSize(JWindow var1) {
      if (this.minimumSize != null) {
         applySizeLimit(var1, this.minimumSize, new Comparator<Integer>() {
            public int compare(Integer var1, Integer var2) {
               return -var1.compareTo(var2);
            }
         });
      }
   }

   private static void applySizeLimit(JWindow var0, Dimension var1, Comparator<Integer> var2) {
      Dimension var3 = var0.getSize();
      Dimension var4 = new Dimension(var3);
      boolean var5 = false;
      if (var2.compare(var3.width, var1.width) > 0) {
         var4.width = var1.width;
         var5 = true;
      }

      if (var2.compare(var3.height, var1.height) > 0) {
         var4.height = var1.height;
         var5 = true;
      }

      if (var5) {
         var0.setSize(var4);
      }
   }
}
