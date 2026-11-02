package org.etlforge.ui.widget;

import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.ui.util.KeyBindingInstaller;
import org.etlforge.ui.util.KeyUtils;

public final class DefaultComponentAction {
   public static void bind(JComponent var0, Action var1) {
      bind(var0, 0, var1);
   }

   public static void bind(JComponent var0, int var1, Action var2) {
      KeyBindingInstaller var3 = new KeyBindingInstaller(var0, var1);
      var3.addBinding(KeyUtils.ENTER, "default-action", var2);
      var3.install();
   }

   public static void whenFocused(JComponent var0, Action var1) {
      bind(var0, 0, var1);
   }

   public static void whenInFocusedWindow(JComponent var0, Action var1) {
      bind(var0, 2, var1);
   }

   public static void whenAncestorOfFocusOwner(JComponent var0, Action var1) {
      bind(var0, 1, var1);
   }

   private DefaultComponentAction() {
   }
}
