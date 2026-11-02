package org.etlforge.integration.server.engine.connector.netsuite.operation;

import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteExecutionContext;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.ws.NetSuiteSoapServiceExecution;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteGetServerTime {
   static final String getServerTimeOperation = "getServerTime";
   private final String getServerTimeRequest;
   private final NetSuitePassport passport;
   private String netsuiteServerTime = null;
   private boolean status;
   private String statusMessage;
   private String statusCode;

   public NetSuiteGetServerTime(NetSuitePassport var1) {
      this.passport = var1;
      this.getServerTimeRequest = getRequestBody(var1);
   }

   private static String getRequestBody(NetSuitePassport var0) {
      ConnectorNetSuiteNamespaces var1 = NetSuiteExecutionContext.INSTANCE.getExecution(var0).getConnectorNetSuiteNamespaces();
      StringBuilder var2 = new StringBuilder();
      XmlElementUtils.startElementTag(var2, "getServerTime", var1.getMessageNamespacePrefix(), var1.getMessageNamespace());
      XmlElementUtils.endElementTag(var2, "getServerTime");
      return var2.toString();
   }

   private String toErrorString() {
      StringBuilder var1 = new StringBuilder();
      if (!this.status) {
         var1.append(";statusCode=").append(this.statusCode);
         var1.append(";statusMessage=").append(this.statusMessage);
      }

      return var1.toString();
   }

   public String getServerTime() {
      this.netsuiteServerTime = null;
      NetSuiteSoapServiceExecution var1 = new NetSuiteSoapServiceExecution(this.passport);
      boolean var2 = false;
      HttpCallResponse var3 = var1.execute("getServerTime", this.getServerTimeRequest, var2, false);
      String var4 = var3.getResponse();
      this.parseServerTimeResponse(var4);
      if (!this.status) {
         throw new ConnectorServerRuntimeException(this.toErrorString());
      } else {
         return this.netsuiteServerTime;
      }
   }

   private void parseServerTimeResponse(String var1) {
      KongaSaxParser var2 = new KongaSaxParser();
      NetSuiteGetServerTime.ServerTimeResponseHandler var3 = new NetSuiteGetServerTime.ServerTimeResponseHandler();
      var2.registerHandler(var3);

      try {
         var2.parseString(var1);
      } catch (SAXException var5) {
         throw new ConnectorServerRuntimeException(var5);
      }
   }

   @RelativePathRoot("Envelope/Body/getServerTimeResponse/getServerTimeResult")
   private class ServerTimeResponseHandler {
      private ServerTimeResponseHandler() {
      }

      @StartElementHandler("status")
      public void status(Attributes var1) {
         String var2 = var1.getValue("isSuccess");
         NetSuiteGetServerTime.this.status = var2.equalsIgnoreCase("true") || var2.equals("1");
      }

      @EndElementHandler("status/statusDetail/code")
      public void statusCode(String var1) {
         NetSuiteGetServerTime.this.statusCode = var1;
      }

      @EndElementHandler("status/statusDetail/message")
      public void statusMessage(String var1) {
         NetSuiteGetServerTime.this.statusMessage = var1;
      }

      @EndElementHandler("serverTime")
      public void netsuiteServerTime(String var1) {
         NetSuiteGetServerTime.this.netsuiteServerTime = var1;
      }
   }
}
