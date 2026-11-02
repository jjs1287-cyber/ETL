package com.etlforge.integration.client.ui.sfdc.page.cli;

import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;

abstract class AbstractReviewImplementingItemExecutor implements CommandExecutor {
   private final SalesforceWizardEntity wizard;

   protected AbstractReviewImplementingItemExecutor(SalesforceWizardEntity var1) {
      this.wizard = var1;
   }

   @Override
   public final void execute(String[] var1) throws CommandException {
      WizardItemBrowser var2 = new WizardItemBrowser(this.wizard);
      IntegrationEntity var3 = this.locateItem(var2);
      if (var3 == null) {
         throw new CommandException("Cannot find the requested item.");
      }

      if (var3 instanceof BuiltInIntegrationEntity) {
         String var4 = String.format("The %s \"%s\" cannot be edited.", var3.getEntityType().getDisplayName(), var3.getName());
         throw new CommandException(var4);
      }

      UiCompanion.of(var3).openPage();
   }

   protected abstract IntegrationEntity locateItem(WizardItemBrowser var1);
}
