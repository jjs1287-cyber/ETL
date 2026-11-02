package org.etlforge.integration.salesforce.wsdl;

import org.etlforge.integration.salesforce.SalesforceConnection;

public interface SObjectSchemaProviderFactory {
   SObjectSchemaProvider newProvider(SalesforceConnection var1);
}
