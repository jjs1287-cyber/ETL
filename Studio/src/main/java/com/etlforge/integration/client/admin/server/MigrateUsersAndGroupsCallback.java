package com.etlforge.integration.client.admin.server;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;

public interface MigrateUsersAndGroupsCallback extends ServerCallback {
   void done(ServerInfo var1);
}
