package org.etlforge.integration.data.entity.httpendpoint;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class HttpEndpointValidator {
   private final HttpEndpoint endpoint;
   private final ValidationMessageCollector collector;

   public HttpEndpointValidator(HttpEndpoint var1, ValidationMessageCollector var2) {
      Preconditions.checkNotNull(var1, "endpoint");
      Preconditions.checkNotNull(var2, "collector");
      this.endpoint = var1;
      this.collector = var2;
   }

   public void run() {
      this.validateUrl();
      this.validateOperations();
   }

   private void validateUrl() {
      String var1 = this.endpoint.getUrl();
      if (StringUtils.isBlank(var1)) {
         this.fatal("The URL is missing");
      }
   }

   private void validateOperations() {
      TriggeredOperations var1 = this.endpoint.getTriggeredOperations();
      if (var1.isEmpty()) {
         this.warning("No operations are triggered by this endpoint");
      } else {
         this.validateResponse(var1);
      }
   }

   private void validateResponse(TriggeredOperations var1) {
      switch (this.endpoint.getResponseType()) {
         case NONE:
         default:
            break;
         case OPERATION:
            this.ensureSynchronous();
            this.checkResponseGenerator();
            break;
         case VARIABLE:
            this.ensureSynchronous();
            this.checkResponseVariable();
      }
   }

   private void ensureSynchronous() {
      if (!this.endpoint.isSynchronous(null)) {
         this.fatal("This endpoint is configured to return a response, but all operations are asynchronous");
      }
   }

   private void checkResponseGenerator() {
      if (this.endpoint.getResponseGeneratingOperationId(null) == null) {
         this.fatal("Must select an operation for generating the response");
      }
   }

   private void checkResponseVariable() {
      if (StringUtils.isBlank(this.endpoint.getResponseVariable())) {
         this.fatal("The response variable name is missing");
      }
   }

   private void warning(String var1) {
      this.collector.addMessage(var1, ValidationStatus.WARNING);
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
