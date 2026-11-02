package org.etlforge.integration.server.engine.connector.netsuite.search;

import java.io.File;
import java.io.IOException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchResponse {
   boolean status = false;
   Integer totalRecords = 0;
   Integer pageSize = 0;
   Integer totalPages = 0;
   Integer pageIndex = 0;
   String searchId = null;

   public static NetSuiteSearchResponse getNetSuiteSearchResponseFile(String var0) {
      NetSuiteSearchResponse var1 = new NetSuiteSearchResponse();
      var1.parseNetSuiteSearchResponse(true, var0);
      return var1;
   }

   public boolean getStatus() {
      return this.status;
   }

   public String getSearchId() {
      return this.searchId;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("status=").append(this.status);
      var1.append(";records=").append(this.totalRecords);
      var1.append(";searchId=").append(this.searchId);
      return var1.toString();
   }

   private void parseNetSuiteSearchResponse(boolean var1, String var2) {
      KongaSaxParser var3 = new KongaSaxParser();
      NetSuiteSearchResponse.Handler var4 = new NetSuiteSearchResponse.Handler();
      var3.registerHandler(var4);

      try {
         if (var1) {
            var3.parseFile(new File(var2));
         } else {
            var3.parseString(var2);
         }
      } catch (SAXException var6) {
         throw new ConnectorServerRuntimeException(var6);
      } catch (IOException var7) {
         throw new ConnectorServerRuntimeException(var7);
      }
   }

   @RelativePathRoot("searchResponse/searchResult")
   private class Handler {
      private Handler() {
      }

      @StartElementHandler("status")
      public void status(Attributes var1) {
         String var2 = var1.getValue("isSuccess");
         NetSuiteSearchResponse.this.status = var2.equalsIgnoreCase("true") || var2.equals("1");
      }

      @EndElementHandler("totalRecords")
      public void totalRecords(Integer var1) {
         NetSuiteSearchResponse.this.totalRecords = var1;
      }

      @EndElementHandler("pageSize")
      public void pageSize(Integer var1) {
         NetSuiteSearchResponse.this.pageSize = var1;
      }

      @EndElementHandler("totalPages")
      public void totalPages(Integer var1) {
         NetSuiteSearchResponse.this.totalPages = var1;
      }

      @EndElementHandler("pageIndex")
      public void pageIndex(Integer var1) {
         NetSuiteSearchResponse.this.pageIndex = var1;
      }

      @EndElementHandler("searchId")
      public void workspaceid(String var1) {
         NetSuiteSearchResponse.this.searchId = var1;
      }
   }
}
