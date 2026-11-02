package org.etlforge.integration.client.ui.interchange.transformation.wizard.json;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.WizardType;
import org.etlforge.integration.client.ui.structure.editor.json.JsonPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

final class PageChainImpl implements TransformationStructurePageChain {
   private final JsonPageChain pages;
   private final SourceTarget mode;

   public PageChainImpl(TransformationWizard var1, SourceTarget var2) {
      this.mode = var2;
      XsdFileStore var3 = new XsdFileStore(var1.getFileStore());
      this.pages = new JsonPageChain(var1, var3);
   }

   @Override
   public WizardPage getFirstPage() {
      return this.pages.getFirstPage();
   }

   @Override
   public WizardPage getLastPage() {
      return this.pages.getLastPage();
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return this.pages.hasNextPage(var1);
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      return this.pages.getNextPage(var1);
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return this.pages.hasPreviousPage(var1);
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      return this.pages.getPreviousPage(var1);
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return this.pages.canFinish(var1);
   }

   @Override
   public void applyTo(Transformation var1) throws WizardPageException {
      JsonStructure var2 = this.pages.getDefinedStructure();
      if (this.mode == SourceTarget.Source) {
         var1.setInputStructure(var2);
      } else {
         var1.setOutputStructure(var2);
      }
   }

   @Override
   public void setOpposingStructureNature(WizardType.Nature var1) {
   }
}
