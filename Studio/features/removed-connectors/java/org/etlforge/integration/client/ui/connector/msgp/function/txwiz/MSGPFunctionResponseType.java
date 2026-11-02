package org.etlforge.integration.client.ui.connector.msgp.function.txwiz;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedOutputStructure;

public final class MSGPFunctionResponseType extends WizardType {
   public MSGPFunctionResponseType() {
      super(
         "MSGP.Function.Response",
         "MSGP Function Response",
         WizardType.Nature.HIERARCHICAL,
         new MSGPFunctionResponseType.ResponseAsSourceFactory(),
         new MSGPFunctionResponseType.ResponseAsTargetFactory()
      );
   }

   @Override
   public boolean isTiedToInput(Transformation var1) {
      DataStructure var2 = var1.getInputStructure();
      return isFunctionResponse(var2);
   }

   private static boolean isFunctionResponse(DataStructure var0) {
      if (var0 instanceof XsdBasedOutputStructure) {
         IntegrationEntityId var1 = ((XsdBasedOutputStructure)var0).getFunctionCallId();
         EntityType var2 = var1.getEntityType();
         return MSGPFunction.allTypes().contains(var2);
      } else {
         return false;
      }
   }

   @Override
   public boolean isTiedToOutput(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      return isFunctionResponse(var2);
   }

   private static class ResponseAsSourceFactory implements PageChainFactory {
      private ResponseAsSourceFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new MSGPFunctionSelectionPageChain(var1, FunctionStructureGetter.outputGetter(), TransformationStructureSetter.inputSetter());
      }
   }

   private static class ResponseAsTargetFactory implements PageChainFactory {
      private ResponseAsTargetFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new MSGPFunctionSelectionPageChain(var1, FunctionStructureGetter.outputGetter(), TransformationStructureSetter.outputSetter());
      }
   }
}
