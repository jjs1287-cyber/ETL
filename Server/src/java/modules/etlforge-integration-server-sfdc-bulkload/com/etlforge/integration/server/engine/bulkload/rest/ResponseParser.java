package com.etlforge.integration.server.engine.bulkload.rest;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.BulkLoadException;
import java.text.MessageFormat;
import org.apache.commons.lang.StringUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.SimpleNamespaceContext;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class ResponseParser {
   private final String operation;
   private final int etlforgeErrorCode;
   private final String response;
   private Document dom;
   private Exception parseError;
   private final String prefix = "ns";
   private final SimpleNamespaceContext ns;

   public static ResponseParser forCreateJob(String var0) {
      return new ResponseParser("Create Job", 102, var0);
   }

   public static ResponseParser forCloseJob(String var0) {
      return new ResponseParser("Close Job", 103, var0);
   }

   private ResponseParser(String var1, int var2, String var3) {
      this.operation = var1;
      this.etlforgeErrorCode = var2;
      this.response = var3;
      this.ns = new SimpleNamespaceContext();
      this.ns.add("ns", "http://www.force.com/2009/06/asyncapi/dataload");

      try {
         this.dom = DomParser.parseXmlString(var3, true);
         this.parseError = null;
      } catch (Exception var5) {
         this.dom = null;
         this.parseError = var5;
      }
   }

   public boolean isError() {
      return this.parseError != null || "error".equals(this.dom.getDocumentElement().getLocalName());
   }

   public void raiseError() throws BulkLoadException {
      String var1 = this.getErrorMessage();
      throw new BulkLoadException(this.etlforgeErrorCode, var1);
   }

   private String getErrorMessage() {
      Preconditions.checkState(this.isError(), "There was no error.");
      if (this.dom == null) {
         return this.operation + " returned unexpected response:\n" + this.response;
      }

      String var1 = this.getErrorDetail("exceptionMessage");
      if (StringUtils.isEmpty(var1)) {
         return this.operation + " returned an error. The response was:\n" + this.response;
      }

      String var2 = this.getErrorDetail("exceptionCode");
      if (StringUtils.isNotBlank(var2)) {
         var1 = var1 + " [ExceptionCode: " + var2 + "]";
      }

      return var1 + "\nThis error occurred when calling " + this.operation;
   }

   private String getErrorDetail(String var1) {
      Element var2 = Elements.firstMatch(MessageFormat.format("/{0}:error/{0}:{1}", "ns", var1), this.dom, this.ns);
      return var2 == null ? "" : KongaXmlUtils.getElementValue(var2);
   }

   public String getJobId() throws BulkLoadException {
      if (this.isError()) {
         this.raiseError();
      }

      String var1 = this.getJobInfoSubElement("id");
      if (StringUtils.isBlank(var1)) {
         throw new BulkLoadException(this.etlforgeErrorCode, "The job ID was not included in the Salesforce response:\n" + this.response);
      } else {
         return var1;
      }
   }

   private String getJobInfoSubElement(String var1) throws BulkLoadException {
      Element var2 = Elements.firstMatch(MessageFormat.format("/{0}:jobInfo/{0}:id", "ns"), this.dom, this.ns);
      return var2 == null ? null : KongaXmlUtils.getElementValue(var2);
   }
}
