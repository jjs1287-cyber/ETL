package org.etlforge.integration.server.implementation.webservice.interchange.authenticate.impl;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.client.server.login.AuthenticateResult;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.user.UserId;

public final class AuthenticateResultImpl implements AuthenticateResult {
   private final org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.AuthenticateResult impl;

   public AuthenticateResultImpl(org.etlforge.integration.server.implementation.webservice.interchange.authenticate.client.AuthenticateResult var1) {
      this.impl = var1;
   }

   @Override
   public Map<String, String> keyValues() {
      HashMap var1 = Maps.newHashMap();

      for (WsKeyValue var5 : this.impl.getKeyValues().getKeyValues()) {
         var1.put(var5.getKey(), var5.getValue());
      }

      return var1;
   }

   @Override
   public ServerGuid serverGuid() {
      return new ServerGuid(this.impl.getServerGuid());
   }

   @Override
   public UserId userGuid() {
      return new UserId(this.impl.getUserGuid());
   }

   @Override
   public boolean isUserAdmin() {
      return "1".equals(this.keyValues().get("IsAdministrator"));
   }

   @Override
   public String versionString() {
      return this.impl.getVersionString();
   }
}
