package org.etlforge.integration.salesforce.wsdl;

import java.util.List;
import org.etlforge.integration.salesforce.SalesforceException;

public interface SObjectSchemaProvider {
   SObjectSchema describeObject(String var1) throws SalesforceException;

   List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException;

   List<SObjectSchema> listAllObjects() throws SalesforceException;

   void close();
}
