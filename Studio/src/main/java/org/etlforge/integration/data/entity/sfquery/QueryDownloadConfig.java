package org.etlforge.integration.data.entity.sfquery;

import com.etlforge.integration.client.sfdc.data.OperationPipelineProtector;
import com.etlforge.integration.client.sfdc.data.SfdcItemDownloadConfig;
import com.etlforge.integration.client.sfdc.data.SoapPipelineProtector;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import java.util.EnumSet;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.project.IntegrationProject;

public final class QueryDownloadConfig extends SfdcItemDownloadConfig<SalesforceQuery> {
   public static ItemDownloadConfig newConfig() {
      QueryDownloadConfig var0 = new QueryDownloadConfig();
      ExtensionObjectService var1 = ExtensionObjectService.getService();
      var1.registerInstance(EntityType.SalesforceQuery, ItemDownloadConfig.class, var0);
      return var0;
   }

   private QueryDownloadConfig() {
      super(EntityType.SalesforceQuery, SalesforceQuery.class);
   }

   @Override
   protected OperationPipelineProtector createPipelineProtector() {
      return new SoapPipelineProtector(EnumSet.noneOf(SoapPipelineProtector.Transformation.class));
   }

   protected void doAdditionalProcessing(SalesforceQuery var1, IntegrationProject var2) {
      this.setResponseTranformationFilter(var1);
   }

   private void setResponseTranformationFilter(SalesforceQuery var1) {
      ResponseFilterCoordinator var2 = new ResponseFilterCoordinator();
      var2.updateResponseFilter(var1);
   }
}
