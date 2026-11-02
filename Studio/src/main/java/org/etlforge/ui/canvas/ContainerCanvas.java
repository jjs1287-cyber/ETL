package org.etlforge.ui.canvas;

import java.awt.Component;
import java.awt.Container;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class ContainerCanvas {
   public static void decorate(UiProvider var0, Container var1) {
      decorate(var0, var1, null);
   }

   public static void decorate(UiProvider var0, final Container var1, final Object var2) {
      var0.decorate(new UiCanvas() {
         @Override
         public void display(Component var1x) {
            var1.add(var1x, var2);
         }
      });
   }

   private ContainerCanvas() {
   }
}
