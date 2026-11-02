package org.etlforge.integration.data.entity.sfdc;

import com.etlforge.integration.client.sfdc.data.OperationPipelineProtector;
import com.etlforge.integration.client.sfdc.data.SfdcItemDownloadConfig;
import com.etlforge.integration.client.sfdc.data.SoapPipelineProtector;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import java.util.EnumSet;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceUpdate;

public final class SalesforceUpdateDownloadConfig extends SfdcItemDownloadConfig<SalesforceUpdate> {
   public static ItemDownloadConfig newConfig() {
      SalesforceUpdateDownloadConfig var0 = new SalesforceUpdateDownloadConfig();
      ExtensionObjectService var1 = ExtensionObjectService.getService();
      var1.registerInstance(EntityType.SalesforceUpdate, ItemDownloadConfig.class, var0);
      return var0;
   }

   private SalesforceUpdateDownloadConfig() {
      super(EntityType.SalesforceUpdate, SalesforceUpdate.class);
   }

   @Override
   protected OperationPipelineProtector createPipelineProtector() {
      return new SoapPipelineProtector(EnumSet.of(SoapPipelineProtector.Transformation.REQUEST));
   }
}
