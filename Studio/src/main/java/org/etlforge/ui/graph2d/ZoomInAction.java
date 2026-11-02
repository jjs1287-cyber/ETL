package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.util.KeyUtils;

public final class ZoomInAction extends AbstractAction {
   private static final int MAX_ZOOM = 200;
   private final Graph2D graph;

   public ZoomInAction(Graph2D var1) {
      super("Zoom In");
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(107));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      int var2 = (int)(100.0 * this.graph.getZoomLevel());
      var2 = Math.min(var2 + 10, 200);
      this.graph.setZoomLevel(var2 / 100.0);
   }
}
