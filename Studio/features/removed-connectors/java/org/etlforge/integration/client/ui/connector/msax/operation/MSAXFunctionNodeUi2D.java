package org.etlforge.integration.client.ui.connector.msax.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.AbstractPipelineActivityNodeUi2D;
import org.etlforge.integration.client.ui.operation.OperationTypeIcons;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.connector.operation.MSAXFunctionActivity;
import org.etlforge.integration.data.connector.operation.MSAXOperationType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

final class MSAXFunctionNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   private static final Icon ICON_WHEN_EMPTY = OperationTypeIcons.largeIcon(MSAXOperationType.TYPE);

   public MSAXFunctionNodeUi2D(Graph2D var1, MSAXFunctionActivityNode var2) {
      super(var1, var2);
   }

   public MSAXFunctionActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      MSAXFunction var1 = this.getFunction();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   private MSAXFunction getFunction() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      MSAXFunction var1 = this.getFunction();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      MSAXFunction var1 = this.getFunction();
      return var1 == null ? ICON_WHEN_EMPTY : EntityIcons.forType(var1.getEntityType()).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public MSAXFunctionActivityNode getNodeObject() {
      return (MSAXFunctionActivityNode)super.getNodeObject();
   }
}
