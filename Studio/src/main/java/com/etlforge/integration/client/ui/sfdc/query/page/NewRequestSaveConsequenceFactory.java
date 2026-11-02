package com.etlforge.integration.client.ui.sfdc.query.page;

import com.google.common.base.Optional;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequenceFactory;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.sfdc.WizardLookup;
import org.etlforge.integration.data.functors.EntityFilters;

final class NewRequestSaveConsequenceFactory implements SaveConsequenceFactory {
   @Override
   public Optional<? extends SaveConsequence> getSaveConsequence(IntegrationEntityPage var1) {
      OperationPage var2 = (OperationPage)var1;
      Operation var3 = var2.getObject();
      if (var3.getOperationType() == WebServiceOperationType.TYPE) {
         WizardLookup var4 = WizardLookup.fromOperation(var3);
         var4.setWizardFilter(EntityFilters.allOfType(EntityType.SalesforceQuery));
         SalesforceQuery var5 = (SalesforceQuery)var4.findWizard().orNull();
         if (var5 != null && var5.isBasedOnPartnerWsdl()) {
            return Optional.of(new NewRequestSaveConsequence(var2, var5));
         }
      }

      return Optional.absent();
   }
}
