package org.etlforge.integration.data.entity.httpendpoint;

import com.google.common.collect.Maps;
import java.util.HashMap;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.id.OperationId;

public final class HttpEndpointReferenceReplacer {
   private final HttpEndpoint endpoint;
   private final ReferenceReplacementService service;

   public HttpEndpointReferenceReplacer(HttpEndpoint var1, ReferenceReplacementService var2) {
      this.endpoint = var1;
      this.service = var2;
   }

   public void replaceReferences() {
      this.replaceTriggeredOperations();
   }

   private void replaceTriggeredOperations() {
      TriggeredOperations var1 = this.endpoint.getTriggeredOperations();
      HashMap var2 = Maps.newHashMap();

      for (OperationId var4 : var1.getOperationIds()) {
         Operation var5 = this.service.getReplacement(var4, Operation.class);
         if (var5 != null) {
            var2.put(var4, var5.getID());
         }
      }

      TriggeredOperations var6 = var1.replaceOperations(var2);
      this.endpoint.setTriggeredOperations(var6);
   }
}
