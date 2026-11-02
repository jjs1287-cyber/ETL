package org.etlforge.ui.graph2d;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.util.KeyUtils;

public final class ResetZoomAction extends AbstractAction {
   private final Graph2D graph;

   public ResetZoomAction(Graph2D var1) {
      super("Reset Zoom");
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.getCtrlDown(48));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.graph.setZoomLevel(1.0);
   }
}
