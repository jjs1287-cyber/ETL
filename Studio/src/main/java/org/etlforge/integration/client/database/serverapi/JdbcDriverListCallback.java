package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;

public interface JdbcDriverListCallback extends ServerCallback {
   void driversRetrieved(JdbcDriverDescriptor[] var1, ServerInfo var2);
}
