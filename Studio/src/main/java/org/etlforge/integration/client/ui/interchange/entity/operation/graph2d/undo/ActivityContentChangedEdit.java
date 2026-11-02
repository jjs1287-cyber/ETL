package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityPosition;

public class ActivityContentChangedEdit extends AbstractUndoableEdit {
   private final OperationGraphController controller;
   private final OperationId operationId;
   private final PipelineActivityPosition activityPosition;
   private final IntegrationEntityId newContent;
   private final IntegrationEntityId previousContent;

   public ActivityContentChangedEdit(
      OperationGraphController var1, OperationId var2, PipelineActivityPosition var3, IntegrationEntityId var4, IntegrationEntityId var5
   ) {
      sanityCheck(var1, var2, var3, var4, var5);
      this.controller = var1;
      this.operationId = var2;
      this.activityPosition = var3;
      this.newContent = var4;
      this.previousContent = var5;
   }

   @Override
   public void redo() throws CannotRedoException {
      super.redo();
      this.updateContent(this.newContent);
   }

   @Override
   public void undo() throws CannotUndoException {
      super.undo();
      this.updateContent(this.previousContent);
   }

   @Override
   public String getPresentationName() {
      EntityType var1;
      if (this.newContent != null) {
         var1 = this.newContent.getEntityType();
      } else {
         if (this.previousContent == null) {
            throw new RuntimeException("This is impossible. Impossible, I say!");
         }

         var1 = this.previousContent.getEntityType();
      }

      return GraphResources.format("ActivityContentChangeEdit", var1);
   }

   private void updateContent(IntegrationEntityId var1) {
      EntityActivity var2 = this.locateActivity();
      if (var2 != null) {
         IntegrationEntityLookup var3 = this.controller.getProject().getItemLookup();
         var2.setContent(var1, var3);
      }
   }

   private EntityActivity locateActivity() {
      OperationPipeline var1 = this.locatePipeline();
      if (var1 == null) {
         this.notUndoable();
         return null;
      }

      PipelineActivity var2 = var1.getActivityAtPosition(this.activityPosition);
      if (var2 instanceof EntityActivity) {
         return (EntityActivity)var2;
      }

      this.notUndoable();
      return null;
   }

   private OperationPipeline locatePipeline() {
      for (OperationWrapperNode var2 : this.controller.getGraph().getModel().getOperationNodes()) {
         if (this.operationId.equals(var2.getDataObject().getActivity().getID())) {
            return var2.getPipeline();
         }
      }

      return null;
   }

   private void notUndoable() {
      Alert.error("This change cannot be undone/redone.", "Undo/Redo Failed");
   }

   private static void sanityCheck(
      OperationGraphController var0, OperationId var1, PipelineActivityPosition var2, IntegrationEntityId var3, IntegrationEntityId var4
   ) {
      if (var0 == null) {
         throw new NullPointerException("controller");
      }

      if (var1 == null) {
         throw new NullPointerException("operationId");
      }

      if (var2 == null) {
         throw new NullPointerException("activityPosition");
      }

      Preconditions.checkNotNull(var0, "controller");
      Preconditions.checkNotNull(var1, "operationId");
      Preconditions.checkNotNull(var2, "activityPosition");
      Preconditions.checkArgument(!Objects.equal(var3, var4), "newContent and previousContent cannot be the same");
   }
}
