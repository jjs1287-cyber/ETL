package org.etlforge.integration.client.server.useradmin;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public interface GetUsersAndGroupsCallback extends ServerCallback {
   void gotUsersAndGroups(ServerInfo var1, List<User> var2, List<Group> var3);
}
