package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import org.etlforge.application.DebugMode;
import org.etlforge.integration.client.ui.interchange.core.action.EntityDebugActions;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationActivityEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.PipelineActivityEdge;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.PipelineActivityNode;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.ui.graph.edge.GraphEdge;
import org.etlforge.ui.graph.node.AbstractGraphNode;
import org.etlforge.ui.graph2d.edge.EdgeContextMenuProvider;
import org.etlforge.ui.graph2d.edge.GraphEdgeUi2D;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

class ContextMenuRequestHandler implements EdgeContextMenuProvider {
   private final OperationGraphController controller;
   private final CanvasContextMenuFactory canvasFactory;
   private final OperationContextMenuFactory pipelineFactory;
   private final EmailContextMenuFactory emailFactory;
   private final InsertionPointContextMenuFactory connectionFactory;

   public ContextMenuRequestHandler(OperationGraphController var1) {
      this.controller = var1;
      this.canvasFactory = new CanvasContextMenuFactory(var1);
      this.pipelineFactory = new OperationContextMenuFactory(var1);
      this.emailFactory = new EmailContextMenuFactory(var1);
      this.connectionFactory = new InsertionPointContextMenuFactory(var1, "Insert", null);
   }

   public void handle(Object var1) {
      Rectangle var2 = this.controller.getGraph().getLayout().getBounds(var1);
      if (var2 != null) {
         Point var3 = new Point(var2.x + 16, var2.y + 16);
         if (var1 instanceof OperationWrapperNode) {
            this.handle((OperationWrapperNode)var1, var3);
         } else if (var1 instanceof EmailWrapperNode) {
            this.handle((EmailWrapperNode)var1, var3);
         } else if (var1 instanceof PipelineActivityNode) {
            this.handle((PipelineActivityNode)var1, var3);
         }
      }
   }

   public void handle(OperationWrapperNode var1, Point var2) {
      JPopupMenu var3 = this.pipelineFactory.createMenu(var1);
      this.addDebugItems(var1, var3);
      this.controller.getGraph().showContextMenu(var3, var2);
   }

   public void handle(PipelineActivityNode var1, Point var2) {
      ActivityContextMenuFactory var3 = new ActivityContextMenuFactory(this.controller, var1);
      JPopupMenu var4 = var3.getPopupMenu();
      this.addDebugItems(var1, var4);
      this.controller.getGraph().showContextMenu(var4, var2);
   }

   public void handle(EmailWrapperNode var1, Point var2) {
      KongaPopupMenu var3 = this.emailFactory.createMenu(var1);
      this.addDebugItems(var1, var3);
      this.controller.getGraph().showContextMenu(var3, var2);
   }

   public void handleCanvas(Point var1) {
      JPopupMenu var2 = this.canvasFactory.createMenu();
      this.controller.getGraph().showContextMenu(var2, var1);
   }

   @Override
   public JPopupMenu getPopupMenu(GraphEdgeUi2D var1, int var2, int var3) {
      GraphEdge var4 = var1.getEdge();
      JPopupMenu var5 = null;
      if (var4 instanceof PipelineActivityEdge) {
         var5 = this.createMenuForPipelineActivityEdge((PipelineActivityEdge)var4);
      } else if (var4 instanceof OperationActivityEdge) {
         var5 = this.createMenuForOperationActivityEdge((OperationActivityEdge)var4);
      }

      return var5;
   }

   private JPopupMenu createMenuForPipelineActivityEdge(PipelineActivityEdge var1) {
      OperationPipeline var2 = this.getPipeline(var1);
      return this.connectionFactory.createPopupMenu(var2, var1.asConnectionPoint());
   }

   private OperationPipeline getPipeline(PipelineActivityEdge var1) {
      PipelineActivityNode var2 = var1.from();
      return var2.getParent().getPipeline();
   }

   private JPopupMenu createMenuForOperationActivityEdge(OperationActivityEdge var1) {
      KongaPopupMenu var2 = null;
      Route var3 = var1.getRoute();
      if (var3.getType() != RouteType.SCRIPTED) {
         var2 = new KongaPopupMenu();
         var2.add(new DeleteRouteAction(this.controller, var3));
         return var2;
      } else {
         return var2;
      }
   }

   private void addDebugItems(AbstractGraphNode var1, JPopupMenu var2) {
      if (DebugMode.IS_ENABLED) {
         var2.addSeparator();
         KongaMenu var3 = new KongaMenu("Debug");
         EntityDebugActions var4 = new EntityDebugActions();
         var4.addToMenu(var3);
         var3.add(new ContextMenuRequestHandler.PrintNodeToConsoleAction(var1));
         var2.add(var3);
      }
   }

   private static class PrintNodeToConsoleAction extends AbstractAction {
      private final AbstractGraphNode node;

      public PrintNodeToConsoleAction(AbstractGraphNode var1) {
         super("Print To Console");
         this.node = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.node.printToConsole();
      }
   }
}
