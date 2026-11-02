package org.etlforge.integration.client.server.login;

import java.util.Map;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.UserId;

public interface AuthenticateResult {
   ServerGuid serverGuid();

   UserId userGuid();

   boolean isUserAdmin();

   String versionString();

   Map<String, String> keyValues();
}
