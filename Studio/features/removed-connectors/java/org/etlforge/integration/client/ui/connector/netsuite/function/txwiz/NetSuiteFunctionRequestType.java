package org.etlforge.integration.client.ui.connector.netsuite.function.txwiz;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XsdBasedInputStructure;

public final class NetSuiteFunctionRequestType extends WizardType {
   public NetSuiteFunctionRequestType() {
      super(
         "NetSuite.Function.Request",
         "NetSuite Function Request",
         WizardType.Nature.HIERARCHICAL,
         new NetSuiteFunctionRequestType.RequestAsSourceFactory(),
         new NetSuiteFunctionRequestType.RequestAsTargetFactory()
      );
   }

   @Override
   public boolean isTiedToInput(Transformation var1) {
      DataStructure var2 = var1.getInputStructure();
      return this.isFunctionRequest(var2);
   }

   private boolean isFunctionRequest(DataStructure var1) {
      if (var1 instanceof XsdBasedInputStructure) {
         IntegrationEntityId var2 = ((XsdBasedInputStructure)var1).getFunctionCallId();
         EntityType var3 = var2.getEntityType();
         return NetSuiteFunction.allTypes().contains(var3);
      } else {
         return false;
      }
   }

   @Override
   public boolean isTiedToOutput(Transformation var1) {
      DataStructure var2 = var1.getOutputStructure();
      return this.isFunctionRequest(var2);
   }

   private static class RequestAsSourceFactory implements PageChainFactory {
      private RequestAsSourceFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new NetSuiteFunctionSelectionPageChain(var1, FunctionStructureGetter.inputGetter(), TransformationStructureSetter.inputSetter(), true);
      }
   }

   private static class RequestAsTargetFactory implements PageChainFactory {
      private RequestAsTargetFactory() {
      }

      @Override
      public TransformationStructurePageChain createChain(TransformationWizard var1) {
         return new NetSuiteFunctionSelectionPageChain(var1, FunctionStructureGetter.inputGetter(), TransformationStructureSetter.outputSetter(), true);
      }
   }
}
