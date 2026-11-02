package org.etlforge.integration.salesforce;

import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProviderFactory;

public final class FakeSObjectSchemaProviderFactory implements SObjectSchemaProviderFactory {
   private final SObjectSchemaProvider provider = new FakeSObjectSchemaProvider();

   @Override
   public SObjectSchemaProvider newProvider(SalesforceConnection var1) {
      return this.provider;
   }
}
