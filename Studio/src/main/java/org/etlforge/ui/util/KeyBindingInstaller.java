package org.etlforge.ui.util;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyBindingInstaller {
   private JComponent component;
   private final int condition;
   private final List<KeyBindingInstaller.Entry> entries = new ArrayList<>();

   public KeyBindingInstaller() {
      this(0);
   }

   public KeyBindingInstaller(JComponent var1) {
      this(var1, 0);
   }

   public KeyBindingInstaller(int var1) {
      this.condition = var1;
   }

   public KeyBindingInstaller(JComponent var1, int var2) {
      this.component = var1;
      this.condition = var2;
   }

   public void addBinding(KeyStroke var1, Object var2, Action var3) {
      if (var1 == null) {
         var1 = (KeyStroke)var3.getValue("AcceleratorKey");
         if (var1 == null) {
            throw new IllegalArgumentException("A keystroke was not defined, and the action does not have an accelerator key");
         }
      }

      this.entries.add(new KeyBindingInstaller.Entry(var1, var2, var3));
   }

   public void install() {
      this.install(this.component);
   }

   public void install(JComponent var1) {
      InputMap var2 = var1.getInputMap(this.condition);
      ActionMap var3 = var1.getActionMap();

      for (KeyBindingInstaller.Entry var5 : this.entries) {
         var5.install(var2, var3);
      }
   }

   public void remove() {
      this.remove(this.component);
   }

   public void remove(JComponent var1) {
      InputMap var2 = var1.getInputMap(this.condition);
      ActionMap var3 = var1.getActionMap();

      for (KeyBindingInstaller.Entry var5 : this.entries) {
         var5.remove(var2, var3);
      }
   }

   private static class Entry {
      private final KeyStroke keyStroke;
      private final Object actionKey;
      private final Action action;

      public Entry(KeyStroke var1, Object var2, Action var3) {
         this.keyStroke = var1;
         this.actionKey = var2;
         this.action = var3;
      }

      public void install(InputMap var1, ActionMap var2) {
         var1.put(this.keyStroke, this.actionKey);
         var2.put(this.actionKey, this.action);
      }

      public void remove(InputMap var1, ActionMap var2) {
         var1.remove(this.keyStroke);
         var2.remove(this.actionKey);
      }
   }
}
