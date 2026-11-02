package org.etlforge.ui.util;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class KeyPressChecker implements KeyEventDispatcher {
   private int modifiers;
   private int keyCode;
   private boolean pressed;
   private boolean registered;

   public KeyPressChecker(int var1) {
      this(var1, 0);
   }

   public KeyPressChecker(int var1, int var2) {
      this.modifiers = var2;
      this.keyCode = var1;
      this.register();
   }

   public KeyPressChecker(KeyStroke var1) {
      this(var1.getKeyCode(), var1.getModifiers());
   }

   public boolean isPressed() {
      return this.pressed;
   }

   public void unregister() {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
      this.registered = false;
   }

   public void register() {
      if (!this.registered) {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
         this.registered = true;
      }
   }

   @Override
   public boolean dispatchKeyEvent(KeyEvent var1) {
      if (var1.getKeyCode() == this.keyCode && (var1.getModifiersEx() & this.modifiers) != 0) {
         this.pressed = var1.getID() == 401;
      }

      return false;
   }
}
