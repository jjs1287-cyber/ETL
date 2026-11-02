package org.etlforge.ui.widget;

import java.awt.Component;
import java.awt.Container;
import org.etlforge.system.callback.Acceptor;

public final class ComponentTraverser {
   public static void traverse(Container var0, Acceptor<Component> var1) {
      visit(var0, var1);
   }

   private static boolean visit(Component var0, Acceptor<Component> var1) {
      if (!var1.accept(var0)) {
         return false;
      }

      if (var0 instanceof Container) {
         for (Component var5 : ((Container)var0).getComponents()) {
            if (!visit(var5, var1)) {
               return false;
            }
         }
      }

      return true;
   }

   private ComponentTraverser() {
   }
}
