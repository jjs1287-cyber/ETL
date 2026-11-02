package org.etlforge.integration.client.ui.connector.mscrm.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.AbstractPipelineActivityNodeUi2D;
import org.etlforge.integration.client.ui.operation.OperationTypeIcons;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.connector.operation.MSCrmFunctionActivity;
import org.etlforge.integration.data.connector.operation.MSCrmOperationType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

final class MSCrmFunctionNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   private static final Icon ICON_WHEN_EMPTY = OperationTypeIcons.largeIcon(MSCrmOperationType.TYPE);

   public MSCrmFunctionNodeUi2D(Graph2D var1, MSCrmFunctionActivityNode var2) {
      super(var1, var2);
   }

   public MSCrmFunctionActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      MSCrmFunction var1 = this.getFunction();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   private MSCrmFunction getFunction() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      MSCrmFunction var1 = this.getFunction();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      MSCrmFunction var1 = this.getFunction();
      return var1 == null ? ICON_WHEN_EMPTY : EntityIcons.forType(var1.getEntityType()).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public MSCrmFunctionActivityNode getNodeObject() {
      return (MSCrmFunctionActivityNode)super.getNodeObject();
   }
}
