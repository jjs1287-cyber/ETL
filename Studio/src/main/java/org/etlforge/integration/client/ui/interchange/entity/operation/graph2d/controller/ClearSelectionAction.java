package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.util.KeyUtils;

public class ClearSelectionAction extends AbstractAction {
   private final Graph2D graph;

   public ClearSelectionAction(Graph2D var1) {
      super(Strings.get("ClearSelection.Name"));
      this.graph = var1;
      this.putValue("AcceleratorKey", KeyUtils.ESCAPE);
      this.putValue("ShortDescription", Strings.get("ClearSelection.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.graph.getSelectionModel().clearSelection();
   }
}
