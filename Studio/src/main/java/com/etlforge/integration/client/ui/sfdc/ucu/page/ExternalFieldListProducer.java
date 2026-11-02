package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.wsdl.ExternalIdFieldFilter;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;

final class ExternalFieldListProducer implements Callable<List<SObjectSchema.Field>> {
   private final SalesforceFacade sfdcFacade;
   private final SalesforceConnector connector;
   private final String objectName;

   public ExternalFieldListProducer(SalesforceFacade var1, SalesforceConnector var2, String var3) {
      this.sfdcFacade = var1;
      this.connector = var2;
      this.objectName = var3;
   }

   public List<SObjectSchema.Field> call() throws Exception {
      this.sfdcFacade.login(this.connector);
      SObjectSchemaProvider var1 = this.sfdcFacade.getSchemaProvider();

      try {
         SObjectSchema var2 = var1.describeObject(this.objectName);
         return KongaListUtils.filter(var2.getFields(), new ExternalIdFieldFilter());
      } finally {
         this.sfdcFacade.close();
      }
   }
}
