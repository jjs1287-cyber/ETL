package org.etlforge.integration.data.entity.sfdc;

import com.etlforge.integration.client.sfdc.data.OperationPipelineProtector;
import com.etlforge.integration.client.sfdc.data.SfdcItemDownloadConfig;
import com.etlforge.integration.client.sfdc.data.SoapPipelineProtector;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import java.util.EnumSet;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceUpsert;

public final class SalesforceUpsertDownloadConfig extends SfdcItemDownloadConfig<SalesforceUpsert> {
   public static ItemDownloadConfig newConfig() {
      SalesforceUpsertDownloadConfig var0 = new SalesforceUpsertDownloadConfig();
      ExtensionObjectService var1 = ExtensionObjectService.getService();
      var1.registerInstance(EntityType.SalesforceUpsert, ItemDownloadConfig.class, var0);
      return var0;
   }

   private SalesforceUpsertDownloadConfig() {
      super(EntityType.SalesforceUpsert, SalesforceUpsert.class);
   }

   @Override
   protected OperationPipelineProtector createPipelineProtector() {
      return new SoapPipelineProtector(EnumSet.of(SoapPipelineProtector.Transformation.REQUEST));
   }
}
