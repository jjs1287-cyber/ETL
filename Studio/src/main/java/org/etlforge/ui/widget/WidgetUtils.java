package org.etlforge.ui.widget;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public final class WidgetUtils {
   public static OverlayContainer getOverlayContainerAncestor(Component var0) {
      if (var0 == null) {
         return null;
      }

      for (Container var1 = var0.getParent(); var1 != null; var1 = var1.getParent()) {
         if (var1 instanceof OverlayContainer) {
            return (OverlayContainer)var1;
         }
      }

      return null;
   }

   public static void configureGlassPane(Component var0, boolean var1) {
      if (var1) {
         var0.setCursor(Cursor.getPredefinedCursor(3));
      }

      var0.addMouseListener(new MouseAdapter() {});
      var0.addMouseMotionListener(new MouseMotionAdapter() {});
      var0.addKeyListener(new KeyAdapter() {});
      var0.setFocusTraversalKeysEnabled(false);
   }

   public static void removeMouseListeners(Component var0) {
      for (MouseListener var4 : var0.getMouseListeners()) {
         var0.removeMouseListener(var4);
      }
   }

   public static void makeReadOnly(JToggleButton... var0) {
      for (JToggleButton var4 : var0) {
         var4.setFocusable(false);
         removeMouseListeners(var4);
      }
   }

   public static void centerContextMenu(JPopupMenu var0, Component var1) {
      Dimension var2 = var0.getPreferredSize();
      int var3 = Math.max(0, (var1.getWidth() - var2.width) / 2);
      int var4 = Math.max(0, (var1.getHeight() - var2.height) / 2);
      var0.show(var1, var3, var4);
   }

   private WidgetUtils() {
   }
}
