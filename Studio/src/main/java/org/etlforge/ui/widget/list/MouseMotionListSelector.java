package org.etlforge.ui.widget.list;

import com.google.common.base.Preconditions;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JList;

public final class MouseMotionListSelector {
   private final JList list;
   private final MouseMotionListener listener;

   private MouseMotionListSelector(JList var1) {
      this.list = var1;
      this.listener = new MouseMotionListSelector.ListenerImpl();
      var1.addMouseMotionListener(this.listener);
   }

   public static MouseMotionListSelector install(JList var0) {
      Preconditions.checkNotNull(var0, "list");
      return new MouseMotionListSelector(var0);
   }

   public void uninstall() {
      this.list.removeMouseMotionListener(this.listener);
   }

   private class ListenerImpl implements MouseMotionListener {
      private ListenerImpl() {
      }

      @Override
      public void mouseDragged(MouseEvent var1) {
      }

      @Override
      public void mouseMoved(MouseEvent var1) {
         int var2 = MouseMotionListSelector.this.list.locationToIndex(var1.getPoint());
         if (var2 != MouseMotionListSelector.this.list.getSelectedIndex()) {
            MouseMotionListSelector.this.list.setSelectedIndex(var2);
         }
      }
   }
}
