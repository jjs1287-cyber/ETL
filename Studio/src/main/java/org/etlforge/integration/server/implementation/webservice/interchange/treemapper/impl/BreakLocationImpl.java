package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.impl;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.BreakLocation;
import org.etlforge.integration.debug.client.GeneratorInstance;

final class BreakLocationImpl implements BreakLocation {
   private int lineNumber = -1;
   private IntegrationEntityId integrationEntityId;
   private String targetNodePath;
   private GeneratorInstance generatorInstance;

   public BreakLocationImpl(
      org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client.BreakLocation var1, String var2, GeneratorInstance var3
   ) {
      if (var1 != null) {
         this.lineNumber = var1.getLineNumber();
         String var4 = var1.getOperationGuid();
         if (StringUtils.isNotEmpty(var4)) {
            this.integrationEntityId = new OperationId(var4);
         }

         String var5 = var1.getTransformationGuid();
         if (StringUtils.isNotEmpty(var5)) {
            this.integrationEntityId = new TransformationId(var5);
         }

         String var6 = var1.getScriptGuid();
         if (StringUtils.isNotEmpty(var6)) {
            this.integrationEntityId = new ScriptId(var6);
         }

         if (StringUtils.isNotEmpty(var1.getNodePath())) {
            this.targetNodePath = var1.getNodePath();
         } else {
            this.targetNodePath = var2;
         }

         this.generatorInstance = var3;
      }
   }

   @Override
   public int getLineNumber() {
      return this.lineNumber;
   }

   @Override
   public String getTargetNodePath() {
      return this.targetNodePath;
   }

   @Override
   public GeneratorInstance getGeneratorInstance() {
      return this.generatorInstance;
   }

   @Override
   public IntegrationEntityId getIntegrationEntityId() {
      return this.integrationEntityId;
   }

   public void fillInIntegrationEntityId(IntegrationEntityId var1) {
      this.integrationEntityId = var1;
   }

   @Override
   public String toString() {
      String var1 = "line: " + this.lineNumber;
      if (this.integrationEntityId != null) {
         var1 = var1 + ", item: " + this.integrationEntityId.getEntityType() + " " + this.integrationEntityId;
      }

      if (StringUtils.isNotEmpty(this.targetNodePath)) {
         var1 = var1 + ", node path: " + this.targetNodePath;
      }

      return var1;
   }
}
