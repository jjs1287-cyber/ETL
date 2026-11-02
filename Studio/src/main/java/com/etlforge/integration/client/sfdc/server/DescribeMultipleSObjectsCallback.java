package com.etlforge.integration.client.sfdc.server;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public interface DescribeMultipleSObjectsCallback extends ServerCallback {
   void gotDescriptions(List<SObjectSchema> var1);
}
