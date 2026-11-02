package com.etlforge.integration.server.engine.bulkload.rest;

import com.etlforge.integration.server.engine.bulkload.BulkLoadEndpoint;
import com.etlforge.integration.server.engine.bulkload.BulkLoadException;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.etlforge.integration.salesforce.bulkload.CreateJobParams;

public final class JobCreator {
   private final BulkLoadEndpoint endpoint;
   private final CreateJobParams params;
   private final HttpClient client;
   private Logger logger = Logger.getAnonymousLogger();

   public JobCreator(BulkLoadEndpoint var1, CreateJobParams var2, HttpClient var3) {
      this.endpoint = var1;
      this.params = var2;
      this.client = var3;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public String createJob() throws BulkLoadException {
      this.logger.info(String.format("Calling Create Job. URL = %s, Session ID = %s", this.endpoint.getCreateJobUrl(), this.endpoint.getSessionId()));
      String var1 = this.createRequest();
      String var2 = this.postRequest(var1);
      this.logger.info("Created salesforce job, ID = " + var2);
      return var2;
   }

   private String createRequest() {
      RequestBuilder var1 = new RequestBuilder();
      var1.addElement("operation", this.params.action.getSalesforceName());
      if (StringUtils.isNotEmpty(this.params.objectName)) {
         var1.addElement("object", this.params.objectName);
      }

      if (StringUtils.isNotEmpty(this.params.externalIdField)) {
         var1.addElement("externalIdFieldName", this.params.externalIdField);
      }

      if (this.params.serialMode) {
         var1.addElement("concurrencyMode", "Serial");
      }

      var1.addElement("contentType", "CSV");
      if (StringUtils.isNotEmpty(this.params.assignmentRuleId)) {
         var1.addElement("assignmentRuleId", this.params.assignmentRuleId);
      }

      String var2 = var1.close();
      this.logger.fine("Create Job request:\n" + var2);
      return var2;
   }

   private String postRequest(String var1) throws BulkLoadException {
      String var2 = null;

      try {
         Poster var3 = new Poster(this.endpoint.getCreateJobUrl(), this.endpoint.getSessionId());
         var2 = var3.post(var1, this.client);
      } catch (Exception var4) {
         this.handleError(var1, var2, var4);
         return null;
      }

      this.logger.fine("Create job response:\n" + var2);
      return this.parseResponse(var2);
   }

   private void handleError(String var1, String var2, Exception var3) throws BulkLoadException {
      Utils.logError(var3, this.logger, "The salesforce job was not created.", var1, var2);
      throw new BulkLoadException(102, "The salesforce job could not be created: " + var3.getMessage(), var3);
   }

   private String parseResponse(String var1) throws BulkLoadException {
      ResponseParser var2 = ResponseParser.forCreateJob(var1);
      return var2.getJobId();
   }
}
