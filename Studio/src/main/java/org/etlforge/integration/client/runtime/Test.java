package org.etlforge.integration.client.runtime;

import java.awt.AWTEvent;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

final class Test {
   public static void printFocusOwner(boolean var0) {
      KeyboardFocusManager var1 = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      var1.addPropertyChangeListener("permanentFocusOwner", new Test.FocusOwnerListener());
      if (var0) {
         var1.addPropertyChangeListener("focusOwner", new Test.PermanentFocusOwnerListener());
      }
   }

   public static void globalMouseListener() {
      AWTEventListener var0 = new AWTEventListener() {
         @Override
         public void eventDispatched(AWTEvent var1) {
            MouseEvent var2 = (MouseEvent)var1;
            if (var2.getID() == 502) {
               System.out.println("Mouse relased at: " + var2.getPoint());
               System.out.println("Source: " + var2.getSource());
            }
         }
      };
      Toolkit.getDefaultToolkit().addAWTEventListener(var0, 16L);
   }

   private static class FocusOwnerListener implements PropertyChangeListener {
      private FocusOwnerListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         System.out.println("Focus Owner --");
         System.out.println("  old: " + var1.getOldValue());
         System.out.println("  new: " + var1.getNewValue());
      }
   }

   private static class PermanentFocusOwnerListener implements PropertyChangeListener {
      private PermanentFocusOwnerListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         System.out.println("Permanent Focus Owner --");
         System.out.println("  old: " + var1.getOldValue());
         System.out.println("  new: " + var1.getNewValue());
      }
   }
}
