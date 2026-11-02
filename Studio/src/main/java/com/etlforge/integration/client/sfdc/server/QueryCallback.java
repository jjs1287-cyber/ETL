package com.etlforge.integration.client.sfdc.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.salesforce.query.QueryResult;

public interface QueryCallback extends ServerCallback {
   void done(QueryResult var1);
}
