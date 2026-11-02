package org.etlforge.integration.client.server;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.User;

@ParametersAreNonnullByDefault
public interface ServerConnectionService {
   String NOT_CONNECTED_MESSAGE = "Not connected to a server.";
   void addServerConnectionListener(ServerConnectionListener var1);

   void removeServerConnectionListener(ServerConnectionListener var1);

   ConnectionStatus getConnectionStatus();

   @Nullable
   String getCurrentServerName();

   @Nullable
   ServerGuid getCurrentServerGuid();

   @Nullable
   ServerInfo getCurrentServerInfo();

   @Nullable
   User getCurrentUser();
}
