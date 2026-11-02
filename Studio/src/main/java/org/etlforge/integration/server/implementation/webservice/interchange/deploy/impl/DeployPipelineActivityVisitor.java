package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.function.Function;

class DeployPipelineActivityVisitor {
   private String guid;
   private IntegrationId functionId;

   public String getGuid() {
      String var1 = this.guid;
      this.guid = null;
      return var1;
   }

   public IntegrationId getFunctionId() {
      IntegrationId var1 = this.functionId;
      this.functionId = null;
      return var1;
   }

   public void visit(PipelineActivity var1) {
      if (var1 instanceof EntityActivity) {
         EntityActivity var2 = (EntityActivity)var1;
         this.set(var2);
      }
   }

   private void set(EntityActivity<?> var1) {
      IntegrationEntity var2 = (IntegrationEntity)var1.getContent();
      if (var2 != null) {
         this.guid = var2.getID().toString();
      } else {
         this.guid = null;
      }

      if (var2 instanceof Function) {
         this.functionId = ((Function)var2).getFunctionID();
      } else {
         this.functionId = null;
      }
   }
}
