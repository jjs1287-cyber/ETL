package org.etlforge.integration.salesforce.wsdl;

import org.etlforge.integration.salesforce.SalesforceException;

public interface CachingSObjectSchemaProvider extends SObjectSchemaProvider {
   void clearCache();

   SObjectSchema refreshObjectDefinition(String var1) throws SalesforceException;
}
