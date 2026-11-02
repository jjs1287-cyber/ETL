package com.etlforge.integration.server.engine.bulkload.rest;

import com.etlforge.integration.server.engine.bulkload.BulkLoadEndpoint;
import com.etlforge.integration.server.engine.bulkload.BulkLoadException;
import java.util.logging.Logger;
import org.apache.http.client.HttpClient;

public final class JobCloser {
   private final BulkLoadEndpoint endpoint;
   private final String jobId;
   private final HttpClient client;
   private Logger logger = Logger.getAnonymousLogger();

   public JobCloser(BulkLoadEndpoint var1, String var2, HttpClient var3) {
      this.endpoint = var1;
      this.jobId = var2;
      this.client = var3;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void closeJob() throws BulkLoadException {
      this.logger.info(String.format("Calling Close Job. URL = %s, Session ID = %s", this.endpoint.getCloseJobUrl(this.jobId), this.endpoint.getSessionId()));
      String var1 = this.createRequest();
      this.postRequest(var1);
      this.logger.info("Closed salesforce job, ID = " + this.jobId);
   }

   private String createRequest() {
      RequestBuilder var1 = new RequestBuilder();
      var1.addElement("state", "Closed");
      String var2 = var1.close();
      this.logger.fine("Close Job request:\n" + var2);
      return var2;
   }

   private void postRequest(String var1) throws BulkLoadException {
      String var2 = null;

      try {
         Poster var3 = new Poster(this.endpoint.getCloseJobUrl(this.jobId), this.endpoint.getSessionId());
         var2 = var3.post(var1, this.client);
      } catch (Exception var4) {
         this.handleError(var1, var2, var4);
      }

      this.parseResponse(var2);
   }

   private void handleError(String var1, String var2, Exception var3) throws BulkLoadException {
      Utils.logError(var3, this.logger, "The salesforce job was not closed.", var1, var2);
      throw new BulkLoadException(103, "The salesforce job was not closed: " + var3.getMessage(), var3);
   }

   private void parseResponse(String var1) throws BulkLoadException {
      this.logger.fine("Close Job response:\n" + var1);
      ResponseParser var2 = ResponseParser.forCloseJob(var1);
      if (var2.isError()) {
         var2.raiseError();
      }
   }
}
