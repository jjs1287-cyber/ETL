package org.etlforge.integration.server.engine.connector.netsuite.ws;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.server.engine.connector.exception.ConnectorQuickStopExeception;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSoapFault {
   private static String WS_CONCUR_SESSION_DISALLWD = "WS_CONCUR_SESSION_DISALLWD";
   private static String SESSION_TIMED_OUT = "SESSION_TIMED_OUT";
   String faultCodeType = "";
   String faultCode = "";
   String faultString = "";
   private final HttpCallResponse response;

   NetSuiteSoapFault(HttpCallResponse var1) {
      this.response = var1;
   }

   private boolean hasFault(boolean var1, boolean var2) {
      if (Strings.isNullOrEmpty(this.faultCode)) {
         return false;
      } else {
         return var2 && this.isSessionTimeOut() ? false : !var1 || !this.isWS_CONCUR_SESSION_DISALLWD();
      }
   }

   public String getFaultMessage() {
      return this.faultString;
   }

   public boolean isWS_CONCUR_SESSION_DISALLWD() {
      return WS_CONCUR_SESSION_DISALLWD.equals(this.faultCodeType);
   }

   public boolean isSessionTimeOut() {
      return SESSION_TIMED_OUT.equals(this.faultCodeType);
   }

   public boolean checkSoapFault(boolean var1, String var2, boolean var3, boolean var4) {
      try {
         return this.checkSoapFaultImpl(var1, var2, var3, var4);
      } catch (SAXException var6) {
         var6.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var6);
      } catch (IOException var7) {
         var7.printStackTrace();
         throw new ConnectorWebServiceRuntimeException(var7);
      }
   }

   public boolean checkSoapFaultImpl(boolean var1, String var2, boolean var3, boolean var4) throws SAXException, IOException {
      NetSuiteSoapFault.NetSuiteSoapFaultHandler var5 = new NetSuiteSoapFault.NetSuiteSoapFaultHandler();

      try {
         if (var1) {
            SaxParsers.parseFile(new File(var2), var5, true);
         } else {
            SaxParsers.parseString(var2, var5, true);
         }
      } catch (ConnectorQuickStopExeception var7) {
      }

      boolean var6 = !this.hasFault(var3, var4);
      if (var6) {
         return false;
      } else {
         throw new ConnectorWebServiceRuntimeException(this.toString(var2));
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("FaultString:").append(this.faultString);
      var1.append("\nFaultCode:").append(this.faultCode);
      var1.append("\ndetail.code:").append(this.faultCodeType);
      if (this.response != null) {
         var1.append('\n').append(this.response.getStatusLine());
      }

      return var1.toString();
   }

   private String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(this.toString());
      var2.append("\n detail:");
      var2.append(var1);
      return var2.toString();
   }

   public static void main(String[] var0) throws SAXException, IOException {
      String var1 = "Z:/etlforge/branches/v5/5.4.x/dev/ETLForgeConnectorProject/NETSUITE/Sample/Operations/";
      String var2 = var1 + "deleteListResSoapFault.xml";
      NetSuiteSoapFault var3 = new NetSuiteSoapFault(null);
      var3.checkSoapFault(true, var2, false, true);
      System.out.println("fault=" + var3.toString());
   }

   private class NetSuiteSoapFaultHandler extends XmlSaxHandler {
      boolean isFaultCode = false;
      boolean isFaultString = false;
      boolean isFaultDetail = false;
      boolean isInSoapBody = false;
      StringBuilder sb = null;
      int soapFaultLevel = 0;

      private NetSuiteSoapFaultHandler() {
      }

      private boolean isSoapBody(String var1, String var2) {
         return var2.equals("Body") && var1.equals("http://schemas.xmlsoap.org/soap/envelope/");
      }

      private boolean isSoapFault(String var1, String var2) {
         return this.soapFaultLevel == this.getLevel() && var2.equals("Fault") && var1.equals("http://schemas.xmlsoap.org/soap/envelope/");
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.sb != null) {
            this.sb = null;
         }

         if (this.isSoapBody(var1, var2)) {
            this.isInSoapBody = true;
            this.soapFaultLevel = this.getLevel() + 1;
         } else if (this.isInSoapBody) {
            if (this.soapFaultLevel == this.getLevel()) {
               if (!this.isSoapFault(var1, var2)) {
                  throw new ConnectorQuickStopExeception();
               }
            } else if (this.soapFaultLevel + 1 == this.getLevel()) {
               this.isFaultCode = var2.equals("faultcode");
               this.isFaultString = var2.equals("faultstring");
               this.isFaultDetail = var2.equals("detail");
               this.sb = new StringBuilder();
            } else if (this.isFaultDetail) {
               this.sb = new StringBuilder();
            }
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (this.soapFaultLevel < this.getLevel()) {
            if (this.isFaultCode) {
               NetSuiteSoapFault.this.faultCode = this.sb.toString();
               this.isFaultCode = false;
            } else if (this.isFaultString) {
               NetSuiteSoapFault.this.faultString = this.sb.toString();
               this.isFaultString = false;
            } else if (this.isFaultDetail) {
               if (var2.equals("code")) {
                  NetSuiteSoapFault.this.faultCodeType = this.sb.toString();
               }

               if (this.soapFaultLevel + 1 == this.getLevel()) {
                  this.isFaultDetail = false;
               }
            }

            if (this.sb != null) {
               this.sb = null;
            }
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.sb != null) {
            this.sb.append(var1, var2, var3);
         }
      }
   }
}
