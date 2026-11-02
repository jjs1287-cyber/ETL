package org.etlforge.integration.server.authentication;

import java.sql.SQLException;
import org.etlforge.util.kongaprotocol.KongaProtocolCommandAuthenticator;

public class AuthenticationHandler implements KongaProtocolCommandAuthenticator {
   @Override
   public boolean isValidUser(String var1, String var2) {
      try {
         return AuthenticateUser.isValidAdminUser(var1, var2);
      } catch (SQLException var4) {
         var4.printStackTrace();
         return false;
      }
   }
}
