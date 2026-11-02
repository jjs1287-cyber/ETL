package org.etlforge.integration.server.engine.connector.netsuite.operation;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;

public class NetSuiteGetList {
   private static final String CustomizationRef = "CustomizationRef";
   private static final String getList = "getList";
   private final NetSuitePassport passport;

   public NetSuiteGetList(NetSuitePassport var1) {
      this.passport = var1;
   }

   private static String getRequest(NetSuitePassport var0, List<NetSuiteCustomizationRef> var1) {
      ConnectorNetSuiteNamespaces var2 = NetSuiteExecutionContext.INSTANCE.getExecution(var0).getConnectorNetSuiteNamespaces();
      StringBuilder var3 = new StringBuilder();
      var3.append("<").append("getList");
      var3.append(" xmlns=\"" + var2.getMessageNamespace() + "\"");
      var3.append(" xmlns:" + var2.getCoreNamespacePrefix() + "=\"" + var2.getCoreNamespace() + "\"");
      var3.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
      var3.append('>');

      for (NetSuiteCustomizationRef var5 : var1) {
         var3.append("<baseRef internalId=\"" + var5.getInternalId() + "\" type=\"" + var5.getRefType() + "\" xsi:type=\"");
         var3.append(var2.getCoreNamespacePrefix() + ":" + "CustomizationRef" + "\"/>");
      }

      var3.append("</").append("getList").append('>');
      return var3.toString();
   }

   public String getList(NetSuiteCustomizationRef var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(var1);
      return this.getList(var2);
   }

   public String getList(List<NetSuiteCustomizationRef> var1) {
      String var2 = getRequest(this.passport, var1);
      return getList(this.passport, var2);
   }

   private static String getList(NetSuitePassport var0, String var1) {
      NetSuiteSoapServiceExecution var2 = new NetSuiteSoapServiceExecution(var0);
      return var2.executeString("getList", var1, true);
   }
}
