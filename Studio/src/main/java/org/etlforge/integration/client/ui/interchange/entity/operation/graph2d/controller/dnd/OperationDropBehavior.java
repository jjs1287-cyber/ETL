package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.dnd;

import com.google.common.collect.Lists;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Collection;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraph2D;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.RouteTypeSelector;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.operation.NextOperationFilter;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowActivity;
import org.etlforge.integration.data.entity.operation.flow.RouteType;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.ui.graph.node.AbstractGraphNode;

final class OperationDropBehavior extends DropBehavior {
   private final OperationGraphController controller;
   private final OperationGraph2D graph;
   private final OperationWrapperNode node;
   private final EntityFilter dropFilter;
   private final FileDropBehavior fileDrop;
   private Collection<?> newSelection;

   public OperationDropBehavior(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.node = var2;
      this.graph = var1.getGraph();
      this.dropFilter = new OperationDropBehavior.DropFilter();
      this.fileDrop = new FileDropBehavior(var1, var2);
   }

   @Override
   public boolean handleDrop(DropTargetDropEvent var1) {
      IntegrationEntity var2 = this.getDroppedEntity(var1);
      boolean var3 = false;
      if (var2 instanceof Schedule) {
         var3 = this.handleScheduleDrop(var2);
      } else if (var2 instanceof OperationFlowActivity) {
         var3 = this.handleOperationActivityDrop((OperationFlowActivity)var2);
      } else {
         var3 = this.fileDrop.handleDrop(var1);
      }

      return var3;
   }

   @Override
   public Collection<?> getNewSelection() {
      return this.newSelection;
   }

   private boolean handleScheduleDrop(IntegrationEntity var1) {
      boolean var2 = this.controller.setSchedule(this.node, (Schedule)var1);
      if (var2) {
         this.newSelection = Lists.newArrayList(this.node);
      } else {
         this.newSelection = null;
      }

      return var2;
   }

   private boolean handleOperationActivityDrop(OperationFlowActivity var1) {
      boolean var2 = false;
      AbstractGraphNode var3 = null;
      if (this.controller.isChangeAllowed(this.node)) {
         RouteType var4 = this.selectRouteType(var1);
         if (var4 != null) {
            if (var1 instanceof Operation) {
               var2 = this.controller.setNextOperation(this.node, (Operation)var1, var4);
               var3 = this.controller.getGraph().getModel().getOperationNode((Operation)var1);
            } else {
               if (!(var1 instanceof EmailMessage)) {
                  throw new IllegalStateException("Unexpected operation flow activity: " + var1);
               }

               var2 = this.controller.setNextEmail(this.node, (EmailMessage)var1, var4);
               var3 = this.controller.getGraph().getModel().getEmailNode((EmailMessage)var1);
            }
         }
      }

      if (var2 && var3 != null) {
         this.newSelection = Lists.newArrayList(var3);
      } else {
         this.newSelection = null;
      }

      return var2;
   }

   @Override
   public boolean isDropAllowed(DropTargetDragEvent var1) {
      return this.isEntityDropAllowed(var1) || this.fileDrop.isDropAllowed(var1);
   }

   private boolean isEntityDropAllowed(DropTargetDragEvent var1) {
      IntegrationEntity var2 = this.getDraggedEntity(var1);
      return this.dropFilter.apply(var2);
   }

   private RouteType selectRouteType(OperationFlowActivity var1) {
      RouteTypeSelector var2 = new RouteTypeSelector(this.graph.getWindowOwner());
      return var2.select(this.node.getDataObject().getActivity(), (IntegrationEntity)var1);
   }

   private final class DropFilter implements EntityFilter {
      private final EntityFilter opFilter = new NextOperationFilter();

      private DropFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         if (var1 instanceof Schedule) {
            return this.acceptsSchedules();
         } else {
            return var1 instanceof OperationFlowActivity ? this.isDroppable((OperationFlowActivity)var1) : false;
         }
      }

      private boolean acceptsSchedules() {
         return OperationDropBehavior.this.node.getDataObject().isSchedulable();
      }

      private boolean isDroppable(OperationFlowActivity var1) {
         if (var1 instanceof Operation) {
            return this.opFilter.apply((Operation)var1);
         } else if (var1 instanceof EmailMessage) {
            return OperationDropBehavior.this.node.getDataObject().getPipeline().getType().isEmailNotificationsSupported();
         } else if (var1 instanceof Script) {
            return false;
         } else {
            throw new IllegalStateException("Unexpected OperationFlowActivity: " + var1);
         }
      }
   }
}
