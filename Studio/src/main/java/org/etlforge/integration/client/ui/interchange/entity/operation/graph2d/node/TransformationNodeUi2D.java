package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Color;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.style.TextStyle;

public class TransformationNodeUi2D extends AbstractPipelineActivityNodeUi2D {
   private static final TextStyle SYSTEM_TRANSFORMATION = TextStyle.newColor(POPULATED, new Color(0, 0, 225));

   public TransformationNodeUi2D(Graph2D var1, TransformationActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public TransformationActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   private Transformation getTransformation() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentString() {
      Transformation var1 = this.getTransformation();
      String var2 = var1 != null ? var1.getName() : this.getActivity().getRole();
      if (var1 instanceof BuiltInIntegrationEntity) {
         var2 = "(" + var2 + ")";
      }

      return var2;
   }

   @Override
   protected String getContentToolTip() {
      Transformation var1 = this.getTransformation();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      return EntityIcons.forType(EntityType.Transformation).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   public TransformationActivityNode getNodeObject() {
      return (TransformationActivityNode)super.getNodeObject();
   }

   @Override
   protected TextStyle getLabelStyle() {
      return this.getTransformation() instanceof BuiltInIntegrationEntity ? SYSTEM_TRANSFORMATION : super.getLabelStyle();
   }
}
