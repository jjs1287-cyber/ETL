package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.NoStructure;

public final class NoSourceStructurePageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new NoSourceStructurePageChainFactory.ChainImpl();
   }

   private static class ChainImpl extends AbstractTransformationStructurePageChain {
      private ChainImpl() {
      }

      @Override
      public WizardPage getFirstPage() {
         return null;
      }

      @Override
      public WizardPage getLastPage() {
         return null;
      }

      @Override
      public WizardPage getNextPage(WizardPage var1) {
         return null;
      }

      @Override
      public WizardPage getPreviousPage(WizardPage var1) {
         return null;
      }

      @Override
      public boolean hasNextPage(WizardPage var1) {
         return false;
      }

      @Override
      public boolean hasPreviousPage(WizardPage var1) {
         return false;
      }

      @Override
      public void applyTo(Transformation var1) {
         var1.setInputStructure(new NoStructure());
      }
   }
}
