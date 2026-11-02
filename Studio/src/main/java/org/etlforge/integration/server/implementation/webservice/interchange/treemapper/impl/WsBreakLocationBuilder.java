package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.BreakLocation;

final class WsBreakLocationBuilder {
   public static BreakLocation build(org.etlforge.integration.debug.client.BreakLocation var0) {
      BreakLocation var1 = new BreakLocation();
      IntegrationEntityId var2 = var0.getIntegrationEntityId();
      if (var2 instanceof ScriptId) {
         var1.setScriptGuid(var2.toString());
      } else if (var2 instanceof TransformationId) {
         var1.setTransformationGuid(var2.toString());
      } else if (var2 instanceof OperationId) {
         var1.setOperationGuid(var2.toString());
      }

      var1.setLineNumber(var0.getLineNumber());
      var1.setNodePath(var0.getTargetNodePath());
      return var1;
   }

   private WsBreakLocationBuilder() {
   }
}
