package org.etlforge.integration.salesforce.wsdl;

import org.etlforge.integration.salesforce.SalesforceConnection;

public final class DefaultSObjectSchemaProviderFactory implements SObjectSchemaProviderFactory {
   @Override
   public SObjectSchemaProvider newProvider(SalesforceConnection var1) {
      return new DefaultSObjectSchemaProvider(var1);
   }
}
