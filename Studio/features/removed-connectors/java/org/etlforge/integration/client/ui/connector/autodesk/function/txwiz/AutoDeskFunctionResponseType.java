package org.etlforge.integration.client.ui.connector.autodesk.function.txwiz;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedOutputStructure;

public final class AutoDeskFunctionResponseType extends WizardType {
   public AutoDeskFunctionResponseType() {
      super(
         "AutoDesk.Function.Response",
         "PLM 360 Function Response",
         WizardType.Nature.HIERARCHICAL,
         new AutoDeskFunctionResponseType.ResponseAsSourceFactory(),
         new AutoDeskFunctionResponseType.ResponseAsTargetFactory()
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
         return AutoDeskFunction.allTypes().contains(var2);
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
         return new AutoDeskFunctionSelectionPageChain(var1, FunctionStructureGetter.outputGetter(), TransformationStructureSetter.inputSetter());
      }
   }

   private static class ResponseAsTargetFactory implements PageChainFactory {
      private ResponseAsTargetFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new AutoDeskFunctionSelectionPageChain(var1, FunctionStructureGetter.outputGetter(), TransformationStructureSetter.outputSetter());
      }
   }
}
