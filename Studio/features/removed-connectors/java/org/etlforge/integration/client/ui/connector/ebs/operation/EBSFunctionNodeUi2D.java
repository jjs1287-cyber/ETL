package org.etlforge.integration.client.ui.connector.ebs.operation;

import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.AbstractPipelineActivityNodeUi2D;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.connector.operation.EBSFunctionActivity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

final class EBSFunctionNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public EBSFunctionNodeUi2D(Graph2D var1, EBSFunctionActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public EBSFunctionActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   @Override
   protected String getContentString() {
      EBSFunction var1 = this.getFunction();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   private EBSFunction getFunction() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentToolTip() {
      EBSFunction var1 = this.getFunction();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.EBSFunction).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public EBSFunctionActivityNode getNodeObject() {
      return (EBSFunctionActivityNode)super.getNodeObject();
   }
}
