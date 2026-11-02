package org.etlforge.integration.client.ui.connector.autodesk.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.AbstractPipelineActivityNodeUi2D;
import org.etlforge.integration.client.ui.operation.OperationTypeIcons;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.connector.operation.AutoDeskFunctionActivity;
import org.etlforge.integration.data.connector.operation.AutoDeskOperationType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

final class AutoDeskFunctionNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   private static final Icon ICON_WHEN_EMPTY = OperationTypeIcons.largeIcon(AutoDeskOperationType.TYPE);

   public AutoDeskFunctionNodeUi2D(Graph2D var1, AutoDeskFunctionActivityNode var2) {
      super(var1, var2);
   }

   public AutoDeskFunctionActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      AutoDeskFunction var1 = this.getFunction();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   private AutoDeskFunction getFunction() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      AutoDeskFunction var1 = this.getFunction();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      AutoDeskFunction var1 = this.getFunction();
      return var1 == null ? ICON_WHEN_EMPTY : EntityIcons.forType(var1.getEntityType()).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public AutoDeskFunctionActivityNode getNodeObject() {
      return (AutoDeskFunctionActivityNode)super.getNodeObject();
   }
}
