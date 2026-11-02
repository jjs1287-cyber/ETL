package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

public class ScriptNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public ScriptNodeUi2D(Graph2D var1, ScriptActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public ScriptActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   private Script getScript() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentString() {
      Script var1 = this.getScript();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   @Override
   protected String getContentToolTip() {
      Script var1 = this.getScript();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.Script).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public ScriptActivityNode getNodeObject() {
      return (ScriptActivityNode)super.getNodeObject();
   }
}
