package org.etlforge.integration.server.engine.connector.netsuite.ws;

import java.io.File;
import java.net.URI;
import org.apache.http.client.methods.HttpRequestBase;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.integration.server.engine.connector.util.HttpRequestFactory;
import org.etlforge.util.net.ProxySettings;

public class NetSuiteSoapServiceHandler {
   private static final String POST = "POST";
   private final String originalUrl;
   private final HttpRequestBase httpRequest;
   private final NetSuiteSoapServiceCall serviceCall;

   private NetSuiteSoapServiceHandler(ProxySettings var1, String var2, String var3, String var4) {
      this.httpRequest = HttpRequestFactory.newHttpEntityEnclosingRequestBase("POST", var2, var4);
      this.serviceCall = new NetSuiteSoapServiceCall(var1, this.httpRequest);
      this.originalUrl = var2;
      this.setHeaders(var3);
   }

   private NetSuiteSoapServiceHandler(ProxySettings var1, String var2, String var3, File var4) {
      this.httpRequest = HttpRequestFactory.newHttpEntityEnclosingRequestBase("POST", var2, var4);
      this.serviceCall = new NetSuiteSoapServiceCall(var1, this.httpRequest);
      this.originalUrl = var2;
      this.setHeaders(var3);
   }

   private void setHeaders(String var1) {
      this.httpRequest.addHeader("Content-Type", "application/xml;charset=utf-8");
      this.httpRequest.addHeader("Accept", "application/xml;charset=utf-8");
      this.httpRequest.addHeader("SoapAction", var1);
   }

   private HttpCallResponse execute() {
      HttpCallResponse var2 = this.serviceCall.execute(true);
      if (this.shouldReExecuteByNewUrl(this.serviceCall.getProxySettings(), var2)) {
         var2 = this.serviceCall.execute(true);
      }

      return var2;
   }

   private HttpCallResponse executeFile(String var1) {
      HttpCallResponse var2 = this.serviceCall.executeFile(var1);
      if (this.shouldReExecuteByNewUrl(this.serviceCall.getProxySettings(), var2)) {
         var2 = this.serviceCall.executeFile(var1);
      }

      return var2;
   }

   private boolean shouldReExecuteByNewUrl(ProxySettings var1, HttpCallResponse var2) {
      if (!var2.isRedirectUrl()) {
         return false;
      } else {
         URI var3 = this.httpRequest.getURI();
         URI var4 = NetSuiteSoapServerRedirect.redirectUrl(this.originalUrl, var1, var2);
         if (var4 != null && !var3.equals(var4)) {
            this.httpRequest.setURI(var4);
            return true;
         } else {
            return false;
         }
      }
   }

   static HttpCallResponse execute(ProxySettings var0, String var1, String var2, String var3, boolean var4, boolean var5) {
      NetSuiteSoapServiceHandler var6 = new NetSuiteSoapServiceHandler(var0, var1, var2, var3);
      HttpCallResponse var7 = var6.execute();
      NetSuiteSoapFault var8 = new NetSuiteSoapFault(var7);
      var8.checkSoapFault(false, var7.getResponse(), var4, var5);
      return var7;
   }

   static HttpCallResponse executeFile(ProxySettings var0, String var1, String var2, String var3, String var4) {
      NetSuiteSoapServiceHandler var5 = new NetSuiteSoapServiceHandler(var0, var1, var2, new File(var3));
      HttpCallResponse var6 = var5.executeFile(var4);
      NetSuiteSoapFault var7 = new NetSuiteSoapFault(var6);
      var7.checkSoapFault(true, var4, false, false);
      return var6;
   }
}
