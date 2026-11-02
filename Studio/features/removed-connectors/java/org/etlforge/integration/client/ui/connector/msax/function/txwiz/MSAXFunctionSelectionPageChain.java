package org.etlforge.integration.client.ui.connector.msax.function.txwiz;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.TransformationStructureSetter;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;

final class MSAXFunctionSelectionPageChain implements TransformationStructurePageChain {
   private final FunctionSelectionPageChain delegate;
   private final FunctionStructureGetter<MSAXFunction> structureGetter;
   private final TransformationStructureSetter structureSetter;

   protected MSAXFunctionSelectionPageChain(TransformationWizard var1, FunctionStructureGetter<MSAXFunction> var2, TransformationStructureSetter var3) {
      this.delegate = new FunctionSelectionPageChain(var1, var1.getEntityExplorerSupport());
      this.structureGetter = var2;
      this.structureSetter = var3;
   }

   @Override
   public WizardPage getFirstPage() {
      return this.delegate.getFirstPage();
   }

   @Override
   public WizardPage getLastPage() {
      return this.delegate.getLastPage();
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return this.delegate.hasNextPage(var1);
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      return this.delegate.getNextPage(var1);
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return this.delegate.hasPreviousPage(var1);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      return this.delegate.getPreviousPage(var1);
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return this.delegate.canFinish(var1);
   }

   @Override
   public void applyTo(Transformation var1) throws WizardPageException {
      MSAXFunction var2 = this.delegate.getSelectedFunction();
      DataStructure var3 = this.structureGetter.getStructure(var2);
      this.structureSetter.setStructure(var1, var3);
   }

   @Override
   public void setOpposingStructureNature(WizardType.Nature var1) {
   }
}
