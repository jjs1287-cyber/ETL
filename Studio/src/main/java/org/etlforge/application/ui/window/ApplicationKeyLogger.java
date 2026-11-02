package org.etlforge.application.ui.window;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.ui.util.KeyUtils;

public final class ApplicationKeyLogger {
   private static final ApplicationKeyLogger INSTANCE = new ApplicationKeyLogger();
   private final Set<Integer> keysDown = new HashSet<>();
   private KeyEventDispatcher dispatcher;
   private boolean installed;

   private ApplicationKeyLogger() {
   }

   public static ApplicationKeyLogger getLogger() {
      return INSTANCE;
   }

   public boolean isKeyDown(int var1) {
      return this.keysDown.contains(var1);
   }

   public boolean isControlDown() {
      return this.isKeyDown(17);
   }

   public boolean isCtrlShiftDown() {
      return this.isKeyDown(17) && this.isKeyDown(16);
   }

   public synchronized void install() {
      if (!this.installed) {
         if (this.dispatcher == null) {
            this.dispatcher = new ApplicationKeyLogger.Dispatcher();
         }

         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.dispatcher);
         this.installed = true;
      }
   }

   public synchronized void uninstall() {
      if (this.installed) {
         KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.dispatcher);
         this.installed = false;
      }
   }

   private void keyPressed(KeyEvent var1) {
      this.keysDown.add(var1.getKeyCode());
      if (KeyUtils.isCtrlDown(var1)) {
         this.keysDown.add(17);
      }

      if (KeyUtils.isShiftDown(var1)) {
         this.keysDown.add(64);
      }
   }

   private void keyReleased(KeyEvent var1) {
      this.keysDown.remove(var1.getKeyCode());
   }

   private class Dispatcher implements KeyEventDispatcher {
      private Dispatcher() {
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent var1) {
         switch (var1.getID()) {
            case 401:
               ApplicationKeyLogger.this.keyPressed(var1);
               break;
            case 402:
               ApplicationKeyLogger.this.keyReleased(var1);
         }

         return false;
      }
   }
}
