package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.border.Border;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.node.NodeStyle;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.border.RoundedBorder;

public class CollapsedOperationNodeUi extends AbstractOperationNodeUi {
   private static final Border UNSELECTED_BORDER = new BorderBuilder(new RoundedBorder(16, 16, 1, Color.GRAY)).empty(4);
   private static final Border SELECTED_BORDER = new BorderBuilder(new RoundedBorder(16, 16, 2, org.etlforge.application.ui.look.Colors.TEXT_FOREGROUND)).empty(3);

   public CollapsedOperationNodeUi(Graph2D var1, OperationWrapperNode var2) {
      super(var1, var2);
      BorderLayoutBuilder.from(this).north(this.getTitlePane());
      this.setBackground(org.etlforge.application.ui.look.Colors.PANEL_BACKGROUND);
      this.setBorder(UNSELECTED_BORDER);
      this.setOpaque(true);
   }

   @Override
   public void setNodeStyle(NodeStyle var1) {
   }

   @Override
   public boolean isResizable() {
      return true;
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(50, 50);
   }

   @Override
   protected Icon getToggleButtonIcon() {
      return GraphResources.ICONS.getIcon("Expand.16");
   }

   @Override
   protected String getToggleButtonToolTip() {
      return "Expand";
   }

   @Override
   protected void handleToggleButtonClick() {
      OperationNodeController var1 = this.getNodeObject().getNodeController();
      if (var1 != null) {
         var1.expandNode(this.getNodeObject());
      } else {
         this.getGraph().getLayout().expand(this.getNodeObject());
      }
   }

   @Override
   protected Border getSelectedBorder() {
      return SELECTED_BORDER;
   }

   @Override
   protected Border getUnselectedBorder() {
      return UNSELECTED_BORDER;
   }
}

