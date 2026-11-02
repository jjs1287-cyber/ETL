package org.etlforge.integration.client.ui.interchange.transformation.wizard.text;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.AbstractTransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.resources.TransWizResources;
import org.etlforge.integration.client.ui.structure.editor.text.DocumentSelectionPageChain;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.TextStructure;

public final class TextSourcePageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new TextSourcePageChainFactory.ChainImpl(var1);
   }

   private static class ChainImpl extends AbstractTransformationStructurePageChain {
      private final DocumentSelectionPageChain delegate;

      public ChainImpl(TransformationWizard var1) {
         this.delegate = new DocumentSelectionPageChain(var1, var1.getEntityExplorerSupport());
         this.delegate.setTitle(TransWizResources.getString("Text.Source.SubTitle"));
         this.delegate.setMainCaption(TransWizResources.getString("Text.Source.MainCaption"));
         this.delegate.setSubCaption(TransWizResources.getString("Text.Source.SubCaption"));
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
      public WizardPage getNextPage(WizardPage var1) {
         return this.delegate.getNextPage(var1);
      }

      @Override
      public WizardPage getPreviousPage(WizardPage var1) {
         return this.delegate.getPreviousPage(var1);
      }

      @Override
      public boolean hasNextPage(WizardPage var1) {
         return this.delegate.hasNextPage(var1);
      }

      @Override
      public boolean hasPreviousPage(WizardPage var1) {
         return this.delegate.hasPreviousPage(var1);
      }

      @Override
      public void applyTo(Transformation var1) throws WizardPageException {
         TextDocument var2 = this.delegate.getSelectedDocument();
         if (var2 != null) {
            TextStructure var3 = var2.getDataStructure();
            var1.setInputStructure(var3);
            var1.setSourceTextDocumentId(var2.getID());
         } else {
            throw new WizardPageException(this.delegate.getFirstPage(), "Must select a text document");
         }
      }

      @Override
      public boolean canFinish(WizardPage var1) {
         return this.delegate.canFinish(var1);
      }
   }
}
