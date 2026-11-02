package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

public class TargetNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public TargetNodeUi2D(Graph2D var1, TargetActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public TargetActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   private Target getTarget() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentString() {
      Target var1 = this.getTarget();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   @Override
   protected String getContentToolTip() {
      Target var1 = this.getTarget();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      if (this.getTarget() != null) {
         Icon var1 = LocationDecoratedIcons.forTarget().getIcon(this.getTarget(), EntityIcons.Size.LARGE);
         if (var1 != null) {
            return var1;
         }
      }

      return EntityIcons.forType(EntityType.Target).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public TargetActivityNode getNodeObject() {
      return (TargetActivityNode)super.getNodeObject();
   }
}
