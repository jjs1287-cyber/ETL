package org.etlforge.integration.server.engine.connector.netsuite.endpoint;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;

public class NetSuitePassportReq {
   private final NetSuitePassport passport;
   private final Map<String, String> passportPrefixNamespace;
   private final String passportQName;
   private final ConnectorNetSuiteNamespaces ns;

   public NetSuitePassportReq(NetSuitePassport var1) {
      this.passport = var1;
      this.passportPrefixNamespace = Maps.newTreeMap();
      this.ns = NetSuiteExecutionContext.INSTANCE.getExecution(var1).getConnectorNetSuiteNamespaces();
      this.passportPrefixNamespace.put(this.ns.getMessageNamespacePrefix(), this.ns.getMessageNamespace());
      this.passportPrefixNamespace.put(this.ns.getCoreNamespacePrefix(), this.ns.getCoreNamespace());
      this.passportQName = XmlElementUtils.getQName(this.ns.getMessageNamespacePrefix(), "passport");
   }

   public NetSuitePassport getNetSuitePassport() {
      return this.passport;
   }

   public void addPassport(StringBuilder var1) {
      XmlElementUtils.startElementTagPrefixDefs(var1, this.passportQName, this.passportPrefixNamespace);
      this.addChild(var1, "email", this.passport.getEmail());
      this.addChild(var1, "password", this.passport.getPassword());
      this.addChild(var1, "account", this.passport.getAccount());
      XmlElementUtils.endElementTag(var1, this.passportQName);
   }

   private void addChild(StringBuilder var1, String var2, String var3) {
      String var4 = XmlElementUtils.getQName(this.ns.getCoreNamespacePrefix(), var2);
      XmlElementUtils.elementValue(var1, var4, var3);
   }

   public String toXmlString() {
      StringBuilder var1 = new StringBuilder();
      this.addPassport(var1);
      return var1.toString();
   }
}
