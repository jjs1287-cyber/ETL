package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.util.KeyUtils;

public final class ZoomOutAction extends AbstractAction {
   private static final int MIN_ZOOM = 10;
   private final Graph2D graph;

   public ZoomOutAction(Graph2D var1) {
      super("Zoom Out");
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(109));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      int var2 = (int)(100.0 * this.graph.getZoomLevel());
      var2 = Math.max(10, var2 - 10);
      this.graph.setZoomLevel(var2 / 100.0);
   }
}
