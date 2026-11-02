package org.etlforge.integration.salesforce;

import java.util.List;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public interface SalesforceConnection {
   void open() throws SalesforceException;

   boolean isOpen();

   void close();

   OrgIdentifier getOrgIdentifier();

   String getSessionId();

   String getUserId();

   String getServiceEndpoint();

   int getSessionTimeoutInSeconds();

   QueryResult query(String var1) throws SalesforceException;

   QueryResult queryAll(String var1) throws SalesforceException;

   List<SObjectSchema> listAllObjects() throws SalesforceException;

   SObjectSchema describeObject(String var1) throws SalesforceException;

   List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException;
}
