package org.etlforge.integration.data.entity.sfdc;

import com.etlforge.integration.client.sfdc.data.OperationPipelineProtector;
import com.etlforge.integration.client.sfdc.data.SfdcItemDownloadConfig;
import com.etlforge.integration.client.sfdc.data.SoapPipelineProtector;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import java.util.EnumSet;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.SalesforceCreate;

public final class SalesforceCreateDownloadConfig extends SfdcItemDownloadConfig<SalesforceCreate> {
   public static ItemDownloadConfig newConfig() {
      SalesforceCreateDownloadConfig var0 = new SalesforceCreateDownloadConfig();
      ExtensionObjectService var1 = ExtensionObjectService.getService();
      var1.registerInstance(EntityType.SalesforceCreate, ItemDownloadConfig.class, var0);
      return var0;
   }

   private SalesforceCreateDownloadConfig() {
      super(EntityType.SalesforceCreate, SalesforceCreate.class);
   }

   @Override
   protected OperationPipelineProtector createPipelineProtector() {
      return new SoapPipelineProtector(EnumSet.of(SoapPipelineProtector.Transformation.REQUEST));
   }
}
