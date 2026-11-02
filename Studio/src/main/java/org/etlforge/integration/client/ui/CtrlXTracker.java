package org.etlforge.integration.client.ui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.etlforge.ui.util.KeyUtils;

final class CtrlXTracker {
   public static void install() {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new CtrlXTracker.Dispatcher());
   }

   private CtrlXTracker() {
   }

   private static class Dispatcher implements KeyEventDispatcher {
      private static final KeyStroke CTRL_X = KeyUtils.getCtrlDown(88);

      private Dispatcher() {
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         if (CTRL_X.equals(KeyStroke.getKeyStrokeForEvent(var1))) {
            System.out.println("ctrl-x");
         }

         return false;
      }
   }
}
