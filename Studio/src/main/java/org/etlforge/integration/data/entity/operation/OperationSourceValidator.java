package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

final class OperationSourceValidator {
   private final Operation operation;
   private final ValidationMessageCollector collector;

   public OperationSourceValidator(Operation var1, ValidationMessageCollector var2) {
      this.operation = var1;
      this.collector = var2;
   }

   public void validate() {
      SourceId var1 = this.operation.getSourceId();
      if (isSourceFromPreviousOperation(var1)) {
         SourceFromPreviousOperationValidator var2 = new SourceFromPreviousOperationValidator(this.operation, this.collector);
         var2.validate();
      } else if (isSourceFromHttpEndpoint(var1)) {
         SourceFromHttpEndpointValidator var3 = new SourceFromHttpEndpointValidator(this.operation, this.collector);
         var3.validate();
      }
   }

   private static boolean isSourceFromPreviousOperation(SourceId var0) {
      return SystemSource.SOURCE_FROM_PREVIOUS_OPERATION.getID().equals(var0);
   }

   private static boolean isSourceFromHttpEndpoint(SourceId var0) {
      return SystemSource.FROM_HTTP_ENDPOINT.getID().equals(var0);
   }
}
