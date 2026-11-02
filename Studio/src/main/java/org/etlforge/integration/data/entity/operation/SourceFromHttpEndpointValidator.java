package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

final class SourceFromHttpEndpointValidator {
   private final Operation operation;
   private final ValidationMessageCollector collector;

   public SourceFromHttpEndpointValidator(Operation var1, ValidationMessageCollector var2) {
      this.operation = var1;
      this.collector = var2;
   }

   public void validate() {
      if (!this.isTriggeredByEndpoint()) {
         this.fatal(SystemSource.FROM_HTTP_ENDPOINT.getName() + " can only be used in an operation that is triggered by an HTTP Endpoint.");
      }
   }

   private boolean isTriggeredByEndpoint() {
      ProjectDependencies var1 = this.collector.context().getDependencies();

      for (IntegrationEntity var3 : var1.getDependingObjects(this.operation)) {
         if (this.isEndpointTrigger(var3)) {
            return true;
         }
      }

      return false;
   }

   private boolean isEndpointTrigger(IntegrationEntity var1) {
      return var1 instanceof HttpEndpoint && ((HttpEndpoint)var1).isTriggering(this.operation);
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
