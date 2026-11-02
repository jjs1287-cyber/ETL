package org.etlforge.integration.client.server.useradmin;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.user.User;

public interface GetUserInfoCallback extends ServerCallback {
   void gotUserInfo(User var1);
}
