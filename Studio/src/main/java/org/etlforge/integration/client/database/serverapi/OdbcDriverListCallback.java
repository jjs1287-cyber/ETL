package org.etlforge.integration.client.database.serverapi;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;

public interface OdbcDriverListCallback extends ServerCallback {
   void driversRetrieved(List<OdbcDriverDescriptor> var1, ServerInfo var2);
}
