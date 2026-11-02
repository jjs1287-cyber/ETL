package com.etlforge.integration.client.sfdc.project.upgrade;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.EnumSet;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class QueryRequestReplacer {
   private final IntegrationProject project;
   private boolean saveRequired;

   public QueryRequestReplacer(IntegrationProject var1) {
      this.project = var1;
   }

   public void replaceRequests() {
      ArrayList var1 = Lists.newArrayList();

      for (Operation var4 : this.project.getAllEntitiesOfClass(Operation.class).filter(new QueryRequestReplacer.QueryOperations())) {
         OperationPipeline var5 = new OperationPipeline(var4.getPipeline());
         WebServicePipelineBrowser var6 = new WebServicePipelineBrowser(var5);
         Transformation var7 = var6.getRequest();
         if (var7 != SystemTransformation.SALESFORCE_QUERY_REQUEST) {
            var6.setRequest(SystemTransformation.SALESFORCE_QUERY_REQUEST);
            var4.setPipeline(var5);
            var1.add(var7);
            this.saveRequired = true;
         }
      }

      this.project.deleteItems(var1);
   }

   public boolean isSaveRequired() {
      return this.saveRequired;
   }

   private static class QueryOperations implements EntityFilter {
      private final EnumSet<SfdcMethod> methods = EnumSet.of(SfdcMethod.QUERY, SfdcMethod.QUERY_ALL);

      private QueryOperations() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         Operation var2 = (Operation)var1;
         return var2.isImplementingSalesforcePartnerWizard(this.methods);
      }
   }
}
