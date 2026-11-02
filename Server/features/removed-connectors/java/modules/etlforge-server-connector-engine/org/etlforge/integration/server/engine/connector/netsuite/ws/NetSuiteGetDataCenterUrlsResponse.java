package org.etlforge.integration.server.engine.connector.netsuite.ws;

import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class NetSuiteGetDataCenterUrlsResponse {
   private static final String pathRoot = "Envelope/Body/getDataCenterUrlsResponse/getDataCenterUrlsResult/";
   private static final String statusRelativePathRoot = "Envelope/Body/getDataCenterUrlsResponse/getDataCenterUrlsResult/status";
   private static final String dataCenterUrlsRelativePathRoot = "Envelope/Body/getDataCenterUrlsResponse/getDataCenterUrlsResult/dataCenterUrls";

   static NetSuiteGetDataCenterUrls parseResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      NetSuiteGetDataCenterUrlsResponse.Handler var2 = new NetSuiteGetDataCenterUrlsResponse.Handler(
         "Envelope/Body/getDataCenterUrlsResponse/getDataCenterUrlsResult/dataCenterUrls"
      );
      NetSuiteResponseStatusHandler var3 = new NetSuiteResponseStatusHandler("Envelope/Body/getDataCenterUrlsResponse/getDataCenterUrlsResult/status");
      var1.registerHandler(var2);
      var1.registerHandler(var3);

      try {
         var1.parseString(var0);
      } catch (SAXException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      }

      var3.throwExceptionOnError();
      return var2.dataUrls;
   }

   private static class Handler {
      private final NetSuiteGetDataCenterUrls dataUrls = new NetSuiteGetDataCenterUrls();
      @RelativePathRoot
      public final String relativePathRoot;

      public Handler(String var1) {
         this.relativePathRoot = var1;
      }

      @EndElementHandler("restDomain")
      public void endRestDomain(String var1) {
         this.dataUrls.restDomain = var1;
      }

      @EndElementHandler("systemDomain")
      public void endSystemDomain(String var1) {
         this.dataUrls.systemDomain = var1;
      }

      @EndElementHandler("webservicesDomain")
      public void endWebservicesDomain(String var1) {
         this.dataUrls.webservicesDomain = var1;
      }
   }
}
