package org.etlforge.integration.client.ui.connector.msgp.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.AbstractPipelineActivityNodeUi2D;
import org.etlforge.integration.client.ui.operation.OperationTypeIcons;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.connector.operation.MSGPFunctionActivity;
import org.etlforge.integration.data.connector.operation.MSGPOperationType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

final class MSGPFunctionNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   private static final Icon ICON_WHEN_EMPTY = OperationTypeIcons.largeIcon(MSGPOperationType.TYPE);

   public MSGPFunctionNodeUi2D(Graph2D var1, MSGPFunctionActivityNode var2) {
      super(var1, var2);
   }

   public MSGPFunctionActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      MSGPFunction var1 = this.getFunction();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   private MSGPFunction getFunction() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      MSGPFunction var1 = this.getFunction();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      MSGPFunction var1 = this.getFunction();
      return var1 == null ? ICON_WHEN_EMPTY : EntityIcons.forType(var1.getEntityType()).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public MSGPFunctionActivityNode getNodeObject() {
      return (MSGPFunctionActivityNode)super.getNodeObject();
   }
}
