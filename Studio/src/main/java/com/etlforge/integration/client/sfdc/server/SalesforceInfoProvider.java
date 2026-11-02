package com.etlforge.integration.client.sfdc.server;

import java.util.List;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.salesforce.OrgIdentifier;

public interface SalesforceInfoProvider extends ServerCall {
   void openConnection(OrgIdentifier var1, OpenConnectionCallback var2);

   void listAllObjects(SessionData var1, DescribeMultipleSObjectsCallback var2);

   void describeObject(SessionData var1, String var2, DescribeSObjectCallback var3);

   void describeObjects(SessionData var1, List<String> var2, DescribeMultipleSObjectsCallback var3);

   void query(SessionData var1, String var2, QueryCallback var3);

   void queryAll(SessionData var1, String var2, QueryCallback var3);
}
