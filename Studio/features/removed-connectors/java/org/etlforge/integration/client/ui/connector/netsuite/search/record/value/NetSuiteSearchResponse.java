package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchResponse {
   private boolean status;
   private Integer totalRecords;
   private Integer pageSize = 5;
   private Integer totalPages = 0;
   private Integer pageIndex = 0;
   private String searchId;
   private String statusMessage;
   private String statusCode;

   public static NetSuiteSearchResponse getNetSuiteSearchResponse(String var0) {
      NetSuiteSearchResponse var1 = new NetSuiteSearchResponse();
      var1.parseNetSuiteSearchResponse(var0);
      return var1;
   }

   public boolean getStatus() {
      return this.status;
   }

   public String getSearchId() {
      return this.searchId;
   }

   public Integer getTotalRecords() {
      return this.totalRecords;
   }

   public Integer getPageSize() {
      return this.pageSize;
   }

   public Integer getTotalPages() {
      return this.totalPages;
   }

   public Integer getPageIndex() {
      return this.pageIndex;
   }

   public String getStatusMessage() {
      return this.statusMessage;
   }

   public String getStatusCode() {
      return this.statusCode;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("status=").append(this.status);
      if (this.status) {
         var1.append(";totalRecords=").append(this.totalRecords);
         var1.append(";pageSize=").append(this.pageSize);
         var1.append(";totalPages=").append(this.totalPages);
         var1.append(";pageIndex=").append(this.pageIndex);
         var1.append(";searchId=").append(this.searchId);
      } else {
         var1.append(";statusCode=").append(this.statusCode);
         var1.append(";statusMessage=").append(this.statusMessage);
      }

      return var1.toString();
   }

   private void parseNetSuiteSearchResponse(String var1) {
      KongaSaxParser var2 = new KongaSaxParser();
      NetSuiteSearchResponse.NetSuiteSearchResponseHandler var3 = new NetSuiteSearchResponse.NetSuiteSearchResponseHandler();
      var2.registerHandler(var3);

      try {
         var2.parseString(var1);
      } catch (SAXException var5) {
         throw new RuntimeException(var5);
      }
   }

   @RelativePathRoot("Envelope/Body/searchResponse/searchResult")
   private class NetSuiteSearchResponseHandler {
      private NetSuiteSearchResponseHandler() {
      }

      @StartElementHandler("status")
      public void status(Attributes var1) {
         String var2 = var1.getValue("isSuccess");
         NetSuiteSearchResponse.this.status = var2.equalsIgnoreCase("true") || var2.equals("1");
      }

      @EndElementHandler("status/statusDetail/code")
      public void statusCode(String var1) {
         NetSuiteSearchResponse.this.statusCode = var1;
      }

      @EndElementHandler("status/statusDetail/message")
      public void statusMessage(String var1) {
         NetSuiteSearchResponse.this.statusMessage = var1;
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
