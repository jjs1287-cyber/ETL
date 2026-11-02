package org.etlforge.ui.widget.menu;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public final class MenuDisabler {
   private static final String IGNORE_DISABLER = "ignore_disabler";
   private final JMenu menu;
   private final MenuDisabler.Listener listener;

   public static void ignore(JMenu var0) {
      var0.putClientProperty("ignore_disabler", Boolean.TRUE);
   }

   public static void install(JMenu var0) {
      new MenuDisabler(var0);
   }

   public MenuDisabler(JMenu var1) {
      this.menu = var1;
      this.listener = new MenuDisabler.Listener();
      this.register(var1);
      this.update();
   }

   private void register(JMenu var1) {
      for (Component var5 : var1.getMenuComponents()) {
         if (var5 instanceof JMenuItem) {
            JMenuItem var6 = (JMenuItem)var5;
            if (!this.isIgnored(var6)) {
               var6.addPropertyChangeListener("enabled", this.listener);
               if (var6 instanceof JMenu) {
                  this.register((JMenu)var6);
                  install((JMenu)var6);
               }
            }
         }
      }
   }

   private boolean isIgnored(JMenuItem var1) {
      Object var2 = var1.getClientProperty("ignore_disabler");
      return Boolean.TRUE.equals(var2);
   }

   private void update() {
      this.menu.setEnabled(this.isEnabled(this.menu));
   }

   private boolean isEnabled(JMenu var1) {
      for (Component var5 : var1.getMenuComponents()) {
         if (var5 instanceof JMenuItem) {
            if (var5.isEnabled()) {
               return true;
            }

            if (var5 instanceof JMenu) {
               boolean var6 = this.isEnabled((JMenu)var5);
               if (var6) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private final class Listener implements PropertyChangeListener {
      private Listener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         MenuDisabler.this.update();
      }
   }
}
