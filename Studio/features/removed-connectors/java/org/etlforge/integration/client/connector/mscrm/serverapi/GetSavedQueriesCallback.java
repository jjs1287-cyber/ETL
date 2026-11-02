package org.etlforge.integration.client.connector.mscrm.serverapi;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.mscrm.query.FetchXml;

public interface GetSavedQueriesCallback extends ServerCallback {
   void gotQueries(ImmutableList<FetchXml> var1);
}
