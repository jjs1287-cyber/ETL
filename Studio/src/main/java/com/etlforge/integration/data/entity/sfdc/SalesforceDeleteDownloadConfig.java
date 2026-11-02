package com.etlforge.integration.data.entity.sfdc;

import com.etlforge.integration.client.sfdc.data.OperationPipelineProtector;
import com.etlforge.integration.client.sfdc.data.SfdcItemDownloadConfig;
import com.etlforge.integration.client.sfdc.data.SoapPipelineProtector;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.EnumSet;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.data.entity.EntityType;

public final class SalesforceDeleteDownloadConfig extends SfdcItemDownloadConfig<SalesforceDelete> {
   public static ItemDownloadConfig newConfig() {
      SalesforceDeleteDownloadConfig var0 = new SalesforceDeleteDownloadConfig();
      ExtensionObjectService var1 = ExtensionObjectService.getService();
      var1.registerInstance(EntityType.SalesforceDelete, ItemDownloadConfig.class, var0);
      return var0;
   }

   private SalesforceDeleteDownloadConfig() {
      super(EntityType.SalesforceDelete, SalesforceDelete.class);
   }

   @Override
   protected OperationPipelineProtector createPipelineProtector() {
      return new SoapPipelineProtector(EnumSet.of(SoapPipelineProtector.Transformation.REQUEST));
   }
}
