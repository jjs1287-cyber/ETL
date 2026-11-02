package org.etlforge.integration.client.ui.connector.sap.function.txwiz;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedInputStructure;

public final class SapFunctionRequestType extends WizardType {
   public SapFunctionRequestType() {
      super(
         "SAP.Function.Request",
         "SAP Function Request",
         WizardType.Nature.HIERARCHICAL,
         new SapFunctionRequestType.RequestAsSourceFactory(),
         new SapFunctionRequestType.RequestAsTargetFactory()
      );
   }

   @Override
   public boolean isTiedToInput(Transformation var1) {
      DataStructure var2 = var1.getInputStructure();
      return isFunctionRequest(var2);
   }

   private static boolean isFunctionRequest(DataStructure var0) {
      if (var0 instanceof XsdBasedInputStructure) {
         IntegrationEntityId var1 = ((XsdBasedInputStructure)var0).getFunctionCallId();
         return var1.getEntityType() == EntityType.SapFunction;
      } else {
         return false;
      }
   }

   @Override
   public boolean isTiedToOutput(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      return isFunctionRequest(var2);
   }

   private static class RequestAsSourceFactory implements PageChainFactory {
      private RequestAsSourceFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new SapFunctionSelectionPageChain(var1, FunctionStructureGetter.inputGetter(), TransformationStructureSetter.inputSetter());
      }
   }

   private static class RequestAsTargetFactory implements PageChainFactory {
      private RequestAsTargetFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new SapFunctionSelectionPageChain(var1, FunctionStructureGetter.inputGetter(), TransformationStructureSetter.outputSetter());
      }
   }
}
