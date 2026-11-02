package org.etlforge.ui.widget.window;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import org.etlforge.system.Disposable;

public class WindowResizer implements Disposable {
   private final JWindow window;
   private final MouseResizeStrategyFactory factory;
   private MouseResizeStrategy strategy;
   private boolean mousePressed;
   private WindowResizer.MouseHandler mouseHandler;
   private ComponentListener resizeListener;

   public WindowResizer(JWindow var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("w cannot be null.");
      }

      this.window = var1;
      this.factory = new MouseResizeStrategyFactory(var1);
   }

   public void install() {
      if (this.mouseHandler == null) {
         this.mouseHandler = new WindowResizer.MouseHandler();
         this.window.getRootPane().addMouseListener(this.mouseHandler);
         this.window.getRootPane().addMouseMotionListener(this.mouseHandler);
         this.resizeListener = new ComponentAdapter() {};
         this.window.addComponentListener(this.resizeListener);
      }
   }

   @Override
   public void dispose() {
      this.window.removeComponentListener(this.resizeListener);
      this.window.getRootPane().removeMouseListener(this.mouseHandler);
      this.window.getRootPane().removeMouseMotionListener(this.mouseHandler);
      this.window.setCursor(Cursor.getDefaultCursor());
   }

   private void updateCursor() {
      if (this.strategy == null) {
         this.window.setCursor(Cursor.getDefaultCursor());
      } else {
         this.strategy.updateCursor();
      }
   }

   private class MouseHandler extends MouseAdapter implements MouseMotionListener {
      private MouseHandler() {
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         if (!WindowResizer.this.mousePressed) {
            WindowResizer.this.strategy = WindowResizer.this.factory.getStrategy(var1.getPoint());
            WindowResizer.this.updateCursor();
         }
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
         if (WindowResizer.this.strategy != null) {
            Point var2 = var1.getPoint();
            SwingUtilities.convertPointToScreen(var2, WindowResizer.this.window.getContentPane());
            WindowResizer.this.strategy.updateSize(var2);
         }
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         if (!WindowResizer.this.mousePressed) {
            WindowResizer.this.strategy = null;
            WindowResizer.this.updateCursor();
         }
      }

      @Override
      public void mousePressed(MouseEvent var1) {
         WindowResizer.this.mousePressed = true;
      }

      @Override
      public void mouseReleased(MouseEvent var1) {
         WindowResizer.this.mousePressed = false;
      }
   }
}
