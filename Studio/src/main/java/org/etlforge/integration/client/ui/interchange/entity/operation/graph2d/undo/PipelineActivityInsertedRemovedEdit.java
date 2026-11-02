package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import javax.swing.undo.AbstractUndoableEdit;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityPosition;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;

abstract class PipelineActivityInsertedRemovedEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId opId;
   private final PipelineActivityType activityType;
   private final PipelineActivityPosition activityPosition;
   private final PipelineActivityPosition previousPosition;
   private final PipelineActivityPosition nextPosition;
   private final IntegrationEntityId contentId;

   public PipelineActivityInsertedRemovedEdit(OperationGraphController var1, PipelineActivity var2, OperationWrapperNode var3) {
      this.controller = var1;
      this.opId = var3.getOperationId();
      OperationPipeline var4 = var3.getPipeline();
      this.activityType = var2.getType();
      this.activityPosition = var4.getPosition(var2);
      this.previousPosition = this.getPrevious(var4, var2);
      this.nextPosition = this.getNext(var4, var2);
      this.contentId = this.getContentId(var2);
   }

   protected final PipelineActivityType getActivityType() {
      return this.activityType;
   }

   protected static void alertNotUndoable() {
      Alert.error("Cannot undo this edit.", "Not Undoable");
   }

   protected static void alertNotRedoable() {
      Alert.error("Cannot redo this edit.", "Not Redoable");
   }

   protected final boolean insertActivity() {
      OperationPipeline var1 = this.locatePipeline();
      if (var1 == null) {
         return false;
      }

      PipelineActivity var2 = this.activityType.createEmptyActivity();
      if (this.contentId != null) {
         IntegrationEntityLookup var3 = this.controller.getProject().getItemLookup();
         ((EntityActivity)var2).setContent(this.contentId, var3);
      }

      if (this.previousPosition == null) {
         this.insertFirst(var1, var2);
      } else if (this.nextPosition == null) {
         this.insertLast(var1, var2);
      } else if (!this.insertBetween(var1, var2)) {
         return false;
      }

      return true;
   }

   private boolean insertBetween(OperationPipeline var1, PipelineActivity var2) {
      PipelineActivity var3 = var1.getActivityAtPosition(this.previousPosition);
      PipelineActivity var4 = var1.getActivityAtPosition(this.nextPosition);
      if (var3 != null && var4 != null && var1.getNextActivity(var3) == var4) {
         try {
            var1.insertBetween(var2, var1.getActivityAtPosition(this.previousPosition), var1.getActivityAtPosition(this.nextPosition));
         } catch (Exception var6) {
            return false;
         }
      }

      return true;
   }

   private void insertLast(OperationPipeline var1, PipelineActivity var2) {
      PipelineActivityPosition var3 = var1.getPosition(var1.getLastActivity());
      if (var3.equals(this.previousPosition)) {
         var1.insertLast(var2);
      }
   }

   private void insertFirst(OperationPipeline var1, PipelineActivity var2) {
      PipelineActivityPosition var3 = var1.getPosition(var1.getFirstActivity());
      if (var3.equals(this.nextPosition)) {
         var1.insertFirst(var2);
      }
   }

   protected final boolean removeActivity() {
      OperationPipeline var1 = this.locatePipeline();
      if (var1 == null) {
         return false;
      }

      PipelineActivity var2 = var1.getActivityAtPosition(this.activityPosition);
      if (var2 == null) {
         return false;
      }

      try {
         var1.removeActivity(var2);
         return true;
      } catch (RemoveActivityVetoException var4) {
         Alert.error(var4.getMessage(), "Error");
         return false;
      }
   }

   private OperationPipeline locatePipeline() {
      for (OperationWrapperNode var2 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (this.opId.equals(var2.getDataObject().getActivity().getID())) {
            return var2.getPipeline();
         }
      }

      return null;
   }

   private PipelineActivityPosition getPrevious(OperationPipeline var1, PipelineActivity var2) {
      PipelineActivity var3 = var1.getPreviousActivity(var2);
      return var3 != null ? var1.getPosition(var3) : null;
   }

   private PipelineActivityPosition getNext(OperationPipeline var1, PipelineActivity var2) {
      PipelineActivity var3 = var1.getNextActivity(var2);
      return var3 != null ? var1.getPosition(var3) : null;
   }

   private IntegrationEntityId getContentId(PipelineActivity var1) {
      Object var2 = var1.getContent();
      return var2 instanceof IntegrationEntity ? ((IntegrationEntity)var2).getID() : null;
   }
}
