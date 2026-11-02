package org.etlforge.ui.widget.window;

import com.google.common.collect.Lists;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.SwingUtilities;

public class WindowMouseCloser {
   private final Window window;
   private final WindowMouseCloser.ClickListener clickListener;
   private final WindowListener closeListener;
   private final int dx;
   private final int dy;
   private MouseEvent closingEvent;
   private final List<Component> blindspots;

   public WindowMouseCloser(Window var1) {
      this(var1, 5, 5);
   }

   public WindowMouseCloser(Window var1, int var2, int var3) {
      this.window = var1;
      this.dx = var2;
      this.dy = var3;
      this.clickListener = new WindowMouseCloser.ClickListener();
      this.clickListener.install();
      this.blindspots = Lists.newArrayList();
      this.closeListener = new WindowMouseCloser.CloseListener();
      var1.addWindowListener(this.closeListener);
   }

   public void dispose() {
      this.clickListener.uninstall();
      this.window.removeWindowListener(this.closeListener);
   }

   public MouseEvent getClosingEvent() {
      return this.closingEvent;
   }

   public void addBlindSpot(Component var1) {
      this.blindspots.add(var1);
   }

   public void removeBlindSpot(Component var1) {
      this.blindspots.remove(var1);
   }

   private class ClickListener implements AWTEventListener {
      private ClickListener() {
      }

      @Override
      public void eventDispatched(AWTEvent var1) {
         if (var1 instanceof MouseEvent) {
            this.checkIfWindowShouldBeDisposed((MouseEvent)var1);
         }
      }

      public void install() {
         Toolkit.getDefaultToolkit().addAWTEventListener(this, 16L);
      }

      public void uninstall() {
         Toolkit.getDefaultToolkit().removeAWTEventListener(this);
      }

      private void checkIfWindowShouldBeDisposed(MouseEvent var1) {
         if (var1.getID() == 502) {
            Component var2 = var1.getComponent();
            if (!this.isBlindspot(var2)) {
               if (SwingUtilities.getWindowAncestor(var2) != WindowMouseCloser.this.window) {
                  Point var3 = var1.getPoint();
                  SwingUtilities.convertPointToScreen(var3, var2);
                  if (!WindowUtils.contains(WindowMouseCloser.this.window, var3, WindowMouseCloser.this.dx, WindowMouseCloser.this.dy)) {
                     WindowMouseCloser.this.window.setVisible(false);
                     WindowMouseCloser.this.window.dispose();
                     WindowMouseCloser.this.closingEvent = var1;
                  }
               }
            }
         }
      }

      private boolean isBlindspot(Component var1) {
         for (Component var3 : WindowMouseCloser.this.blindspots) {
            if (var1 == var3 || SwingUtilities.isDescendingFrom(var1, var3)) {
               return true;
            }
         }

         return false;
      }
   }

   private class CloseListener extends WindowAdapter {
      private CloseListener() {
      }

      @Override
      public void windowClosed(WindowEvent var1) {
         WindowMouseCloser.this.dispose();
      }
   }
}
