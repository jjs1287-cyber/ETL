package org.etlforge.integration.client.ui.connector.sap.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.AbstractPipelineActivityNodeUi2D;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.connector.operation.SapFunctionActivity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

final class SapFunctionNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public SapFunctionNodeUi2D(Graph2D var1, SapFunctionActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public SapFunctionActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      SapFunction var1 = this.getFunction();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   private SapFunction getFunction() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      SapFunction var1 = this.getFunction();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.SapFunction).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public SapFunctionActivityNode getNodeObject() {
      return (SapFunctionActivityNode)super.getNodeObject();
   }
}
