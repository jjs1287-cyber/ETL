package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.awt.Color;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.prefs.EdgeTypePreference;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph2d.EdgeUiFactory2D;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.edge.GraphEdgeUi2D;
import org.etlforge.ui.graph2d.edge.LineSegmentGraphEdgeUi;
import org.etlforge.ui.graph2d.edge.PerpendicularLineSegmentFactory;

public class OperationGraphEdgeUiFactory implements EdgeUiFactory2D {
   private static final Icon SCRIPT_ICON = ClientIcons.FORMULA_24;
   private static final Color SCRIPT_COLOR = new Color(0, 0, 192, 100);
   private static final Color SUCCESS_COLOR = new Color(0, 255, 0, 100);
   private static final Color FAILURE_COLOR = new Color(255, 0, 0, 100);
   private static final Icon SIBLING_ICON = EntityIcons.forType(EntityType.Operation).getDefaultIcon(EntityIcons.Size.MEDIUM);
   private static final Color SIBLING_COLOR = new Color(1686848787, true);

   @Override
   public GraphEdgeUi2D createUi(Graph2D var1, GraphEdge var2) {
      LineSegmentGraphEdgeUi var3 = null;
      if (var2 instanceof OperationActivityEdge) {
         var3 = new LineSegmentGraphEdgeUi(var1, var2);
         this.decorate((OperationActivityEdge)var2, var3);
      } else if (var2 instanceof ScriptSourceEdge) {
         var3 = new LineSegmentGraphEdgeUi(var1, var2);
         this.decorateScriptEdgeUi(var3);
      } else {
         if (!(var2 instanceof PipelineActivityEdge)) {
            throw new RuntimeException("Unexpected edge type: " + var2);
         }

         var3 = new PipelineActivityEdgeUi((OperationGraph2D)var1, (PipelineActivityEdge)var2);
      }

      if (EdgeTypePreference.get() == EdgeType.RIGHT_ANGLES) {
         PerpendicularLineSegmentFactory var4 = new PerpendicularLineSegmentFactory(var1, var2);
         var3.setLineSegmentFactory(var4);
      }

      var3.setLineWidth(2);
      var3.setArrowSize(6);
      return var3;
   }

   private void decorate(OperationActivityEdge var1, LineSegmentGraphEdgeUi var2) {
      switch (var1.getType()) {
         case SUCCESS:
            this.decorateSuccessEdge(var2);
            break;
         case FAILURE:
            this.decorateFailureEdge(var2);
            break;
         case SCRIPTED:
            this.decorateScriptEdgeUi(var2);
            break;
         case SIBLING:
            this.decorateSiblingEdge(var2);
      }
   }

   private void decorateSuccessEdge(LineSegmentGraphEdgeUi var1) {
      var1.setIcon(GraphResources.SUCCESS_24);
      var1.setIconToolTip("This route is triggered on success");
      var1.setColor(SUCCESS_COLOR);
   }

   private void decorateFailureEdge(LineSegmentGraphEdgeUi var1) {
      var1.setIcon(GraphResources.FAILURE_24);
      var1.setIconToolTip("This route is triggered on failure");
      var1.setColor(FAILURE_COLOR);
   }

   private void decorateScriptEdgeUi(LineSegmentGraphEdgeUi var1) {
      var1.setIcon(SCRIPT_ICON);
      var1.setIconToolTip("This route is defined in a script");
      var1.setColor(SCRIPT_COLOR);
   }

   private void decorateSiblingEdge(LineSegmentGraphEdgeUi var1) {
      var1.setIcon(SIBLING_ICON);
      var1.setIconToolTip("This route is defined through a sibling operation");
      var1.setColor(SIBLING_COLOR);
   }
}
