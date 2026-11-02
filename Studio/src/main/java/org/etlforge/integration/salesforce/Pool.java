package org.etlforge.integration.salesforce;

public interface Pool {
   void remove(OrgIdentifier var1);

   void removeIfSame(OrgIdentifier var1, SalesforceConnection var2);
}
