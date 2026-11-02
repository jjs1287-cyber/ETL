package org.etlforge.integration.server.engine.connector.netsuite.ws;

import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteResponseStatusHandler {
   private NetSuiteResponseStatusDetail statusDetail = null;
   private final NetSuiteResponseStatus status = new NetSuiteResponseStatus();
   private boolean isFirstStatus = true;
   @RelativePathRoot
   public final String relativePathRoot;

   public static void validateResponse(String var0, String var1) {
      KongaSaxParser var2 = new KongaSaxParser();
      NetSuiteResponseStatusHandler var3 = new NetSuiteResponseStatusHandler(var0);
      var2.registerHandler(var3);

      try {
         var2.parseString(var1);
      } catch (SAXException var5) {
         throw new ConnectorWebServiceRuntimeException(var5);
      }

      var3.throwExceptionOnError();
   }

   public void throwExceptionOnError() {
      this.status.throwExceptionOnError();
   }

   public NetSuiteResponseStatusHandler(String var1) {
      this.relativePathRoot = var1;
   }

   @StartElementHandler("/")
   public void startStatus(Attributes var1) {
      if (this.isFirstStatus) {
         this.isFirstStatus = false;
         this.status.isSuccess = KongaXmlUtils.isTrue(var1, "isSuccess");
      } else {
         throw new ConnectorWebServiceRuntimeException("There are at least 2 status returns. Only one is allowed by design");
      }
   }

   @StartElementHandler("statusDetail")
   public void startStatusDetail(Attributes var1) {
      this.statusDetail = new NetSuiteResponseStatusDetail();
      this.statusDetail.type = var1.getValue("type");
   }

   @EndElementHandler("statusDetail/code")
   public void code(String var1) {
      this.statusDetail.code = var1;
   }

   @EndElementHandler("statusDetail/message")
   public void message(String var1) {
      this.statusDetail.message = var1;
   }

   @EndElementHandler("statusDetail")
   public void endStatusDetail(String var1) {
      this.status.addResponseStatusDetail(this.statusDetail);
   }
}
