package com.etlforge.integration.client.sfdc.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public interface DescribeSObjectCallback extends ServerCallback {
   void gotDescription(SObjectSchema var1);
}
