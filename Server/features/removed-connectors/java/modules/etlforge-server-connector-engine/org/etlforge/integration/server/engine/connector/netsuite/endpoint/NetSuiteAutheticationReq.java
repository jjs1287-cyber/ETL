package org.etlforge.integration.server.engine.connector.netsuite.endpoint;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapEnvelope;

public class NetSuiteAutheticationReq {
   private final ConnectorNetSuiteNamespaces ns;
   private final String loginQName;
   private final String logoutQName;
   private final NetSuitePassportReq passport;

   public NetSuiteAutheticationReq(NetSuitePassport var1) {
      this.passport = new NetSuitePassportReq(var1);
      this.ns = NetSuiteExecutionContext.INSTANCE.getExecution(var1).getConnectorNetSuiteNamespaces();
      this.loginQName = XmlElementUtils.getQName(this.ns.getMessageNamespacePrefix(), "login");
      this.logoutQName = XmlElementUtils.getQName(this.ns.getMessageNamespacePrefix(), "logout");
   }

   String getLoginRequestEnvelope() {
      String var1 = this.getLoginRequestBody();
      return NetSuiteSoapEnvelope.getSoapEnvelope(null, var1);
   }

   String getLoginRequestBody() {
      StringBuilder var1 = new StringBuilder();
      XmlElementUtils.startElementTag(var1, this.loginQName, this.ns.getMessageNamespacePrefix(), this.ns.getMessageNamespace());
      this.passport.addPassport(var1);
      XmlElementUtils.endElementTag(var1, this.loginQName);
      return var1.toString();
   }

   String getLogoutRequestBody() {
      StringBuilder var1 = new StringBuilder();
      XmlElementUtils.startElementTag(var1, this.logoutQName, this.ns.getMessageNamespacePrefix(), this.ns.getMessageNamespace());
      XmlElementUtils.endElementTag(var1, this.logoutQName);
      return var1.toString();
   }
}
