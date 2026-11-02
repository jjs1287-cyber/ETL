package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.XsltTransform;
import org.etlforge.integration.data.entity.operation.pipeline.XsltTransformActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

public class XsltTransformNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public XsltTransformNodeUi2D(Graph2D var1, XsltTransformActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public XsltTransformActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   private XsltTransform getXsltTransform() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentString() {
      XsltTransform var1 = this.getXsltTransform();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   @Override
   protected String getContentToolTip() {
      XsltTransform var1 = this.getXsltTransform();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.XsltTransform).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public XsltTransformActivityNode getNodeObject() {
      return (XsltTransformActivityNode)super.getNodeObject();
   }
}
