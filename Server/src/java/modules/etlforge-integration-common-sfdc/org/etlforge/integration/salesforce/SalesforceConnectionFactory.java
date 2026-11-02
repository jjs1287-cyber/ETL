package org.etlforge.integration.salesforce;

public interface SalesforceConnectionFactory {
   SalesforceConnection newConnection(OrgIdentifier var1);
}
