package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.google.common.base.Strings;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorAdapterRuntimeException;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class EbsRequestIdParser {
   public static int obtainEbsRequestId(String var0) throws SAXException {
      String var1 = parseEbsRequestIdResponse(var0);
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorAdapterRuntimeException("No EBS request Id specified");
      } else {
         return Integer.parseInt(var1);
      }
   }

   public static String parseEbsRequestIdResponse(String var0) throws SAXException {
      EbsRequestIdParser.EbsRequestIdHandler var1 = new EbsRequestIdParser.EbsRequestIdHandler();
      SaxParsers.parseString(var0, var1, true);
      return var1.requestId;
   }

   public static void main(String[] var0) throws SAXException {
      String var1 = "Z:/etlforge/branches/v5/5.2.x/dev/ETLForgeConnectorProject/EBS/Sample/BOMCOIN_res.xml";
      String var2 = ConnectorExecutionSystem.readFileUTF8(var1);
      String var3 = parseEbsRequestIdResponse(var2);
      System.out.println("id=" + var3);
   }

   private static class EbsRequestIdHandler extends DefaultHandler {
      int level = 0;
      String requestId = null;
      boolean isRequestId = false;
      static final String RequestIdElementName = "REQUEST_ID";

      private EbsRequestIdHandler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.level++;
         this.isRequestId = this.level == 3 && "REQUEST_ID".equals(var2);
         if (this.isRequestId) {
            this.requestId = null;
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         this.isRequestId = false;
         this.level--;
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.isRequestId) {
            if (this.requestId == null) {
               this.requestId = new String(var1, var2, var3);
            } else {
               this.requestId = this.requestId + new String(var1, var2, var3);
            }
         }
      }
   }
}
