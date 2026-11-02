package com.etlforge.integration.client.ui.sfdc.soql.test;

import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.query.QueryResult;

public interface QueryResultHandler {
   void reportResult(QueryResult var1);

   void handleError(SalesforceException var1);

   void reportUnsupportedQuery(String var1);
}
