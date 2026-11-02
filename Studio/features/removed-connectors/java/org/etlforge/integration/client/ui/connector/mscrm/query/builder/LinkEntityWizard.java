package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.application.ui.wizard.WizardPageException;

final class LinkEntityWizard extends Wizard<LinkEntityModel> {
   private final LinkEntitiesModel linkEntitiesModel;
   private final QueryBuilderController controller;
   private final LinkEntityWizardPageChain pages;

   public LinkEntityWizard(LinkEntitiesModel var1, QueryBuilderController var2) {
      this.linkEntitiesModel = var1;
      this.controller = var2;
      WizardDialogDisplayer var3 = this.createDisplayer();
      this.setDisplayer(var3);
      this.pages = new LinkEntityWizardPageChain(this);
      this.setPageChain(this.pages);
   }

   private WizardDialogDisplayer createDisplayer() {
      WizardDialogDisplayer var1 = new WizardDialogDisplayer();
      var1.setSaveText("Insert Link-Entity");
      var1.setManageLocation(this.getClass());
      var1.setModal(true);
      var1.setWizard(this);
      return var1;
   }

   public LinkEntitiesModel getLinkEntitiesModel() {
      return this.linkEntitiesModel;
   }

   public QueryBuilderController getController() {
      return this.controller;
   }

   protected LinkEntityModel finishWizard() throws WizardPageException {
      return this.pages.getResultingModel();
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }
}
