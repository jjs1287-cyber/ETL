package org.etlforge.integration.data.entity.sfdc;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;

public final class SalesforceWizardReferencesReplacer {
   private final ReferenceReplacementService service;

   public SalesforceWizardReferencesReplacer(ReferenceReplacementService var1) {
      this.service = var1;
   }

   public void replaceReferences(SalesforceWizardEntity var1) {
      this.replaceImplementingOperation(var1);
      this.replaceOrg(var1);
   }

   private void replaceImplementingOperation(SalesforceWizardEntity var1) {
      OperationId var2 = var1.getImplementingOperationId();
      Operation var3 = this.service.getReplacement(var2, Operation.class);
      if (var3 != null) {
         var1.setImplementingOperation(var3);
      }
   }

   private void replaceOrg(SalesforceWizardEntity var1) {
      SalesforceConnectorId var2 = var1.getConnectorId();
      SalesforceConnector var3 = this.service.getReplacement(var2, SalesforceConnector.class);
      if (var3 != null) {
         var1.setConnector(var3);
      }
   }
}
