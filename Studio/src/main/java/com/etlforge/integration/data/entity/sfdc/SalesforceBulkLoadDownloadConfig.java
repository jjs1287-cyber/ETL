package com.etlforge.integration.data.entity.sfdc;

import com.etlforge.integration.client.sfdc.data.BulkPipelineProtector;
import com.etlforge.integration.client.sfdc.data.OperationPipelineProtector;
import com.etlforge.integration.client.sfdc.data.SfdcItemDownloadConfig;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;

public final class SalesforceBulkLoadDownloadConfig extends SfdcItemDownloadConfig<SalesforceBulkLoad> {
   public static ItemDownloadConfig newConfig() {
      SalesforceBulkLoadDownloadConfig var0 = new SalesforceBulkLoadDownloadConfig();
      ExtensionObjectService var1 = ExtensionObjectService.getService();
      var1.registerInstance(EntityType.SalesforceBulkLoad, ItemDownloadConfig.class, var0);
      return var0;
   }

   private SalesforceBulkLoadDownloadConfig() {
      super(EntityType.SalesforceBulkLoad, SalesforceBulkLoad.class);
   }

   @Override
   protected OperationPipelineProtector createPipelineProtector() {
      return new BulkPipelineProtector();
   }
}
