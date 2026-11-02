package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import java.util.Collections;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.graph.GraphSelectionModel;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.util.KeyUtils;

public final class DeleteNodeAction extends AbstractSingleObjectSelectionAction<AbstractGraphNode> {
   private final OperationGraphController controller;

   public DeleteNodeAction(OperationGraphController var1) {
      this(var1, null);
   }

   public DeleteNodeAction(OperationGraphController var1, AbstractGraphNode var2) {
      super(AbstractGraphNode.class, Strings.get("DeleteNode.Name"), CommonIcons.DELETE_16);
      this.controller = var1;
      this.putValue("AcceleratorKey", KeyUtils.DELETE);
      this.putValue("ShortDescription", Strings.get("DeleteNode.ToolTip"));
      if (var2 != null) {
         this.setSelection(SelectionFactory.newOrderedSelection(var2));
      }
   }

   protected void run(AbstractGraphNode var1, ActionEvent var2) {
      AbstractGraphNode var3 = var1.getParent();
      boolean var4 = this.controller.deleteNode(var1);
      if (var4) {
         GraphSelectionModel var5 = this.controller.getGraph().getSelectionModel();
         var5.setSelection(Collections.singletonList(var3));
      }
   }

   @Override
   public void setEnabled(boolean var1) {
      super.setEnabled(var1);
      String var2 = var1 ? Strings.get("DeleteNode.ToolTip") : Strings.get("DeleteNode.ToolTip.Disabled");
      this.putValue("ShortDescription", var2);
   }

   protected boolean isEnabledFor(AbstractGraphNode var1) {
      return this.controller.isDeletable(var1);
   }
}
