package com.etlforge.integration.client.sfdc.facade;

import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;

public final class FacadeFactory {
   private final SalesforceConnectionFactory connectionFactory;
   private final SObjectSchemaProviderFactory schemaProviderFactory;

   public FacadeFactory(SalesforceConnectionFactory var1, SObjectSchemaProviderFactory var2) {
      this.connectionFactory = var1;
      this.schemaProviderFactory = var2;
   }

   public SalesforceFacade newFacade() {
      return new SalesforceFacade(this.connectionFactory, this.schemaProviderFactory);
   }
}
