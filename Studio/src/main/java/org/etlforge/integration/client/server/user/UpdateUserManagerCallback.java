package org.etlforge.integration.client.server.user;

import java.util.List;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public interface UpdateUserManagerCallback {
   void updateCompleted(ServerInfo var1, List<User> var2, List<Group> var3);

   void cancelled();

   void failed(Throwable var1);
}
