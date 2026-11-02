package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.id.WebServiceCallId;

public abstract class PipelineAutoCompleter {
   private final IntegrationEntityLookup entityLookup;

   protected PipelineAutoCompleter(IntegrationEntityLookup var1) {
      this.entityLookup = var1;
   }

   public abstract void complete(OperationPipeline var1, PipelineActivity var2);

   protected final Source lookupSource(SourceId var1) {
      return this.entityLookup.getEntity(var1, Source.class);
   }

   protected final Target lookupTarget(TargetId var1) {
      return this.entityLookup.getEntity(var1, Target.class);
   }

   protected final WebServiceCall lookupWsCall(WebServiceCallId var1) {
      return this.entityLookup.getEntity(var1, WebServiceCall.class);
   }

   protected final <E extends IntegrationEntity> E lookup(IntegrationEntityId var1, Class<E> var2) {
      return this.entityLookup.getEntity(var1, var2);
   }

   protected final void removeSourceActivityIfEmpty(OperationPipelineBrowser var1) {
      PipelineActivity var2 = var1.getPipeline().getFirstActivityOfType(SourceActivity.class);
      if (var2 != null && var2.isEmpty()) {
         try {
            var1.getPipeline().removeActivity(var2);
         } catch (RemoveActivityVetoException var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   protected final void checkSource(OperationPipelineBrowser var1, Transformation var2, String var3) {
      if (var2.getInputStructure().getStructureType().isSourceDataRequired()) {
         this.populateSourceActivity(var1, var2, var3);
      } else {
         this.removeSourceActivity(var1);
      }
   }

   private void populateSourceActivity(OperationPipelineBrowser var1, Transformation var2, String var3) {
      Source var4 = this.lookupSource(var2.getSourceId());
      if (var4 != null) {
         SourceActivity var5 = var1.getSourceActivity();
         if (var5 == null) {
            this.insertSourceActivityIfPossible(var1, var3);
         }

         var1.setSource(var4);
      }
   }

   private void insertSourceActivityIfPossible(OperationPipelineBrowser var1, String var2) {
      if (var1.getPipeline().getFirstActivity().getType() == PipelineActivityType.TRANSFORMATION) {
         var1.getPipeline().insertFirst(new SourceActivity(var2));
      }
   }

   protected final void removeSourceActivity(OperationPipelineBrowser var1) {
      PipelineActivity var2 = var1.getPipeline().getFirstActivityOfType(SourceActivity.class);
      if (var2 != null) {
         var1.getPipeline().removeActivity(var2);
      }
   }
}
