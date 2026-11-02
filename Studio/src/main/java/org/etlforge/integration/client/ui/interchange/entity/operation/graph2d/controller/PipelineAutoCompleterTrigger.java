package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityEvent;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineAutoCompleter;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineAdapter;
import org.etlforge.system.Disposable;

class PipelineAutoCompleterTrigger implements Disposable {
   private final OperationWrapper opWrapper;
   private OperationPipeline pipeline;
   private final IntegrationEntityLookup entityLookup;
   private final PipelineAutoCompleterTrigger.Listener listener;

   public PipelineAutoCompleterTrigger(OperationWrapper var1, IntegrationEntityLookup var2) {
      this.opWrapper = var1;
      this.pipeline = var1.getPipeline();
      this.entityLookup = var2;
      this.listener = new PipelineAutoCompleterTrigger.Listener();
      var1.addPropertyChangeListener("pipeline", this.listener);
      this.pipeline.addPipelineListener(this.listener);
   }

   @Override
   public void dispose() {
      this.pipeline.removePipelineListener(this.listener);
      this.opWrapper.removePropertyChangeListener("pipeline", this.listener);
   }

   private class Listener extends OperationPipelineAdapter implements PropertyChangeListener {
      private Listener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "pipeline".equals(var1.getPropertyName());
         PipelineAutoCompleterTrigger.this.pipeline.removePipelineListener(this);
         PipelineAutoCompleterTrigger.this.pipeline = PipelineAutoCompleterTrigger.this.opWrapper.getPipeline();
         PipelineAutoCompleterTrigger.this.pipeline.addPipelineListener(this);
      }

      @Override
      public void contentChanged(PipelineActivityEvent var1) {
         PipelineAutoCompleter var2 = PipelineAutoCompleterTrigger.this.pipeline
            .getType()
            .getPipelineAutoCompleter(PipelineAutoCompleterTrigger.this.entityLookup);
         var2.complete(PipelineAutoCompleterTrigger.this.pipeline, var1.getSource());
      }
   }
}
