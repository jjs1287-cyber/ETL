package org.etlforge.ui.action;

import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public final class ActionKeyBinding {
   public static void install(JComponent var0, KeyStroke var1, ActionListener var2) {
      install(ActionFactory.forKey(var1, var2), var0);
   }

   public static void install(JComponent var0, int var1, KeyStroke var2, ActionListener var3) {
      install(ActionFactory.forKey(var2, var3), var0, var1);
   }

   public static void install(Action var0, JComponent var1) {
      install(var0, var1, 1);
   }

   public static void install(Action var0, JComponent var1, int var2) {
      KeyStroke var3 = (KeyStroke)var0.getValue("AcceleratorKey");
      if (var3 != null) {
         install(var0, var1, var2, var3);
      }
   }

   public static void install(Action var0, JComponent var1, int var2, KeyStroke var3) {
      String var4 = (String)var0.getValue("ActionCommandKey");
      if (var4 == null) {
         var4 = var3.toString();
      }

      var1.getInputMap(var2).put(var3, var4);
      var1.getActionMap().put(var4, var0);
   }

   public static void install(Action var0, JComponent var1, KeyStroke var2) {
      install(var0, var1, 0, var2);
   }

   public static void uninstall(Action var0, JComponent var1) {
      uninstall(var0, var1, 1);
   }

   public static void uninstall(Action var0, JComponent var1, int var2) {
      KeyStroke var3 = (KeyStroke)var0.getValue("AcceleratorKey");
      if (var3 != null) {
         var1.getInputMap(var2).remove(var3);
         String var4 = (String)var0.getValue("ActionCommandKey");
         if (var4 == null) {
            var4 = var3.toString();
         }

         var1.getActionMap().remove(var4);
      }
   }

   private ActionKeyBinding() {
   }
}
