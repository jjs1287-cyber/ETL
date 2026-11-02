package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;

final class SalesforceDateFieldsRetriever implements DateFieldsRetriever {
   private final String objectName;
   private final SalesforceFacade facade;
   private final SalesforceConnector connector;

   public SalesforceDateFieldsRetriever(String var1, SalesforceFacade var2, SalesforceConnector var3) {
      this.objectName = var1;
      this.facade = var2;
      this.connector = var3;
   }

   @Override
   public Optional<DateFields> getDateFields() throws Exception {
      this.facade.login(this.connector);

      try {
         SObjectSchemaProvider var1 = this.facade.getSchemaProvider();
         SObjectSchema var2 = var1.describeObject(this.objectName);
         DateFields var3 = DateFields.fromSObject(var2);
         return Optional.of(var3);
      } finally {
         this.facade.close();
      }
   }
}
