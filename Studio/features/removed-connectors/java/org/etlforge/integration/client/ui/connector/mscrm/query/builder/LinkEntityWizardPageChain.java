package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.mscrm.metadata.Relationship;

final class LinkEntityWizardPageChain implements WizardPageChain {
   private final LinkEntityWizard wizard;
   private final LinkRelationshipSelectionPage relationshipPage;
   private LinkEntityBuilderPage linkEntityBuilderPage;
   private LinkEntityModel resultingModel;

   public LinkEntityWizardPageChain(LinkEntityWizard var1) {
      this.wizard = var1;
      this.relationshipPage = new LinkRelationshipSelectionPage(var1, var1.getLinkEntitiesModel(), var1.getController());
   }

   @Override
   public WizardPage getFirstPage() {
      return this.relationshipPage;
   }

   @Override
   public WizardPage getLastPage() {
      return this.linkEntityBuilderPage;
   }

   @Override
   public boolean hasNextPage(WizardPage var1) {
      return var1 == this.relationshipPage;
   }

   @Override
   public WizardPage getNextPage(WizardPage var1) {
      return var1 == this.relationshipPage ? this.moveIntoBuilderPage() : null;
   }

   private WizardPage moveIntoBuilderPage() {
      if (this.isNewBuilderPageNeeded()) {
         this.resultingModel = this.relationshipPage.createLinkEntityModel();
         this.linkEntityBuilderPage = new LinkEntityBuilderPage(this.wizard, this.resultingModel, this.wizard.getController());
      }

      return this.linkEntityBuilderPage;
   }

   private boolean isNewBuilderPageNeeded() {
      if (this.linkEntityBuilderPage == null) {
         return true;
      }

      Relationship var1 = this.relationshipPage.getRelationship();
      return var1 != this.resultingModel.getRelationship();
   }

   @Override
   public boolean hasPreviousPage(WizardPage var1) {
      return var1 == this.linkEntityBuilderPage;
   }

   @Override
   public WizardPage getPreviousPage(WizardPage var1) {
      return var1 == this.linkEntityBuilderPage ? this.relationshipPage : null;
   }

   @Override
   public boolean canFinish(WizardPage var1) {
      return var1 == this.linkEntityBuilderPage && var1.canContinue();
   }

   public LinkEntityModel getResultingModel() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "getResultingModel");
      assert this.resultingModel != null;
      return this.resultingModel;
   }
}
