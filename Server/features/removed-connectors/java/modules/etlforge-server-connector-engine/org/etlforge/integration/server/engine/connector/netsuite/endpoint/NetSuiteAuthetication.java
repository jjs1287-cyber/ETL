package org.etlforge.integration.server.engine.connector.netsuite.endpoint;

import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;

public class NetSuiteAuthetication {
   private final NetSuitePassport passport;

   public NetSuiteAuthetication(NetSuitePassport var1) {
      this.passport = var1;
   }

   public String login() {
      NetSuiteAutheticationReq var1 = new NetSuiteAutheticationReq(this.passport);
      String var2 = var1.getLoginRequestBody();
      boolean var3 = true;
      NetSuiteSoapServiceExecution var4 = new NetSuiteSoapServiceExecution(this.passport, false);
      return var4.executeString("login", var2, var3, true);
   }

   public void logout() {
      boolean var1 = true;
      NetSuiteAutheticationReq var2 = new NetSuiteAutheticationReq(this.passport);
      String var3 = var2.getLogoutRequestBody();
      NetSuiteSoapServiceExecution var4 = new NetSuiteSoapServiceExecution(this.passport);
      HttpCallResponse var5 = var4.execute("logout", var3, var1, true);
      String var6 = var5.getResponse();
   }
}
