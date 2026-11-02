package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Color;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.LocationDecoratedIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.GraphNodeUi2D;
import org.etlforge.ui.style.TextStyle;

public class SourceNodeUi2D extends AbstractPipelineActivityNodeUi2D implements GraphNodeUi2D {
   private static final TextStyle SYSTEM_STYLE = TextStyle.newColor(POPULATED, new Color(0, 0, 225));

   public SourceNodeUi2D(Graph2D var1, SourceActivityNode var2) {
      super(var1, var2);
      this.setContentString(this.getContentString());
      this.setToolTipText(this.getContentToolTip());
   }

   public SourceActivity getActivity() {
      return this.getNodeObject().getDataObject();
   }

   private Source getSource() {
      return this.getActivity().getContent();
   }

   @Override
   protected String getContentString() {
      Source var1 = this.getSource();
      return var1 != null ? var1.getName() : this.getActivity().getRole();
   }

   @Override
   protected String getContentToolTip() {
      Source var1 = this.getSource();
      return var1 != null ? ToolTipStringFactory.getToolTip(var1) : "Right-click or use drag & drop to populate this place holder";
   }

   @Override
   protected Icon getIcon() {
      if (this.getSource() != null) {
         Icon var1 = LocationDecoratedIcons.forSource().getIcon(this.getSource(), EntityIcons.Size.LARGE);
         if (var1 != null) {
            return var1;
         }
      }

      return EntityIcons.forType(EntityType.Source).getDefaultIcon(EntityIcons.Size.LARGE);
   }

   @Override
   protected TextStyle getLabelStyle() {
      Source var1 = this.getSource();
      return var1 instanceof SystemSource ? SYSTEM_STYLE : super.getLabelStyle();
   }

   public SourceActivityNode getNodeObject() {
      return (SourceActivityNode)super.getNodeObject();
   }
}
