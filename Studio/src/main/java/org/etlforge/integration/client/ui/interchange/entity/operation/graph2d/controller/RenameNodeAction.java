package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.util.KeyUtils;

final class RenameNodeAction extends AbstractSingleObjectSelectionAction<AbstractGraphNode> {
   private final OperationGraphController controller;

   public RenameNodeAction(OperationGraphController var1) {
      this(var1, null);
   }

   public RenameNodeAction(OperationGraphController var1, AbstractGraphNode var2) {
      super(AbstractGraphNode.class, Strings.get("RenameNode.Name"));
      this.controller = var1;
      this.putValue("AcceleratorKey", KeyUtils.F2);
      this.putValue("ShortDescription", Strings.get("RenameNode.ToolTip"));
      if (var2 != null) {
         this.setSelection(SelectionFactory.newOrderedSelection(var2));
      }
   }

   protected boolean isEnabledFor(AbstractGraphNode var1) {
      return var1.isRenamable();
   }

   protected void run(AbstractGraphNode var1, ActionEvent var2) {
      if (this.enabled && this.isChangeAllowed(var1)) {
         this.rename(var1);
      }
   }

   private void rename(AbstractGraphNode var1) {
      this.getRenamer(var1).rename();
   }

   private AbstractNodeRenamer<?> getRenamer(AbstractGraphNode var1) {
      if (var1 instanceof OperationWrapperNode) {
         return new OperationNodeRenamer(this.controller, (OperationWrapperNode)var1);
      } else if (var1 instanceof EmailWrapperNode) {
         return new EmailNodeRenamer(this.controller, (EmailWrapperNode)var1);
      } else if (var1 instanceof PipelineActivityNode) {
         return new PipelineActivityRenamer(this.controller, (PipelineActivityNode)var1);
      } else {
         throw new IllegalStateException("Unexpected node: " + var1);
      }
   }

   private boolean isChangeAllowed(AbstractGraphNode var1) {
      return var1 instanceof OperationWrapperNode ? this.controller.isChangeAllowed((OperationWrapperNode)var1) : true;
   }
}
