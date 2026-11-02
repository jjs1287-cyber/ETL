package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;

public class WebServiceCallNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   public WebServiceCallNodeUi2D(Graph2D var1, WebServiceCallActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public WebServiceCallActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   private WebServiceCall getWebServiceCall() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentString() {
      WebServiceCall var1 = this.getWebServiceCall();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   @Override
   protected String getContentToolTip() {
      WebServiceCall var1 = this.getWebServiceCall();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.WebServiceCall).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public WebServiceCallActivityNode getNodeObject() {
      return (WebServiceCallActivityNode)super.getNodeObject();
   }
}
