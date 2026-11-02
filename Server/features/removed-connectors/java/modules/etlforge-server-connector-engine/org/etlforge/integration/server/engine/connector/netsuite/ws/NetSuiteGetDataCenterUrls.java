package org.etlforge.integration.server.engine.connector.netsuite.ws;

import com.google.common.base.Strings;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteDefinitionExecution;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;

public class NetSuiteGetDataCenterUrls {
   static final String getDataCenterUrlsOperation = "getDataCenterUrls";
   String restDomain;
   String webservicesDomain;
   String systemDomain;

   private static String getRequestBody(ConnectorNetSuiteNamespaces var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      XmlElementUtils.startElementTag(var2, "getDataCenterUrls", null, var0.getMessageNamespace());
      XmlElementUtils.elementValue(var2, "account", var1);
      XmlElementUtils.endElementTag(var2, "getDataCenterUrls");
      return var2.toString();
   }

   private static HttpCallResponse getResponse(NetSuiteDefinitionExecution var0, NetSuitePassport var1) {
      String var3 = getRequestBody(var0.getConnectorNetSuiteNamespaces(), var1.getAccount());
      String var4 = NetSuiteSoapEnvelope.getSoapEnvelope(null, var3);
      NetSuiteWsdlUrl var5 = var0.getNetSuiteServerWsdlUrl().getNetSuiteWsdlUrl(var1.isSandBox());
      return NetSuiteSoapServiceHandler.execute(var1.getProxySettings(), var5.getWsdlUrl(), "getDataCenterUrls", var4, false, false);
   }

   private static NetSuiteGetDataCenterUrls getWebservicesDomain(NetSuiteDefinitionExecution var0, NetSuitePassport var1) {
      HttpCallResponse var2 = getResponse(var0, var1);
      NetSuiteGetDataCenterUrls var3 = NetSuiteGetDataCenterUrlsResponse.parseResponse(var2.getResponse());
      if (Strings.isNullOrEmpty(var3.webservicesDomain)) {
         throw new ConnectorWebServiceRuntimeException(" can not get netsuite data center url for " + var1.getAccount());
      } else {
         return var3;
      }
   }

   public static String getWebservicesUrl(NetSuitePassport var0) {
      NetSuiteDefinitionExecution var1 = NetSuiteExecutionContext.INSTANCE.getExecution(var0);
      NetSuiteWsdlUrl var2 = var1.getNetSuiteServerWsdlUrl().getNetSuiteWsdlUrl(var0.isSandBox());
      NetSuiteGetDataCenterUrls var3 = getWebservicesDomain(var1, var0);
      return var2.newDataCenterUrlString(var3.webservicesDomain);
   }
}
