package org.etlforge.integration.client.server.login;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.UserId;
import org.etlforge.util.id.KongaGuid;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public class MockAuthenticate implements Authenticate {
   private static final String SERVER_NAME = "Fantasia";
   private static final ServerGuid SERVER_ID = new ServerGuid(KongaGuid.createGuid().toString());
   private static final UserId ADMIN_ID = new UserId("13ec289d-1c2d-486e-98fb-16d5edbc5fbb");
   private static final UserId NON_ADMIN_USER_ID = new UserId();

   @Override
   public AuthenticateResult login(LoginCredentials var1) throws IntegrationServerException {
      String var2 = var1.getServer();
      if (var2.equals("Fantasia")) {
         final boolean var3 = "admin".equalsIgnoreCase(var1.getUser());
         return new AuthenticateResult() {
            @Override
            public boolean isUserAdmin() {
               return var3;
            }

            @Override
            public Map<String, String> keyValues() {
               return Maps.newHashMap();
            }

            @Override
            public ServerGuid serverGuid() {
               return MockAuthenticate.SERVER_ID;
            }

            @Override
            public UserId userGuid() {
               return var3 ? MockAuthenticate.ADMIN_ID : MockAuthenticate.NON_ADMIN_USER_ID;
            }

            @Override
            public String versionString() {
               try {
                  Version var1x = ApplicationConfiguration.getVersion("RequiredServerVersion");
                  return var1x.toString();
               } catch (IllegalVersionException var2x) {
                  var2x.printStackTrace();
                  return ClientInfo.getCurrentVersion().toString();
               }
            }
         };
      } else {
         throw new IntegrationServerException("You can only login to the Fantasia server");
      }
   }
}
