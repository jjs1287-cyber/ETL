package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.bulkload.TargetFields;
import org.etlforge.integration.salesforce.wsdl.ObjectSchemaLookup;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsFinder;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class DefaultTargetFieldsUpdater implements TargetFieldsUpdater {
   private final SalesforceFacade sfdcFacade;
   private final String sobjectName;
   private Supplier<SalesforceConnector> connectorSupplier;
   private boolean updateRelatedObjectsInfo;

   public DefaultTargetFieldsUpdater(SalesforceFacade var1, String var2) {
      this.sfdcFacade = var1;
      this.sobjectName = var2;
   }

   public void setUpdateRelatedObjectsInfo(boolean var1) {
      this.updateRelatedObjectsInfo = var1;
   }

   public void setConnectorSupplier(Supplier<SalesforceConnector> var1) {
      this.connectorSupplier = var1;
   }

   @Override
   public TargetFields getTargetFields() throws SalesforceException {
      SalesforceConnector var1 = this.getConnector();
      this.sfdcFacade.login(var1);

      try {
         SObjectSchema var2 = this.sfdcFacade.refreshFlatObject(this.sobjectName);
         List var3 = this.getRelatedObjects(var2);
         return new TargetFields(var2.getFields(), var3);
      } finally {
         this.sfdcFacade.close();
      }
   }

   private SalesforceConnector getConnector() throws SalesforceException {
      SalesforceConnector var1 = null;
      if (this.connectorSupplier != null) {
         var1 = this.connectorSupplier.get();
      }

      if (var1 == null) {
         throw new SalesforceException("Must select a Salesforce Connector.");
      } else {
         return var1;
      }
   }

   private List<RelatedObject> getRelatedObjects(SObjectSchema var1) {
      return this.updateRelatedObjectsInfo
         ? RelatedObjectsFinder.find(var1, new DefaultTargetFieldsUpdater.ObjectLookupFunction(var1))
         : Collections.emptyList();
   }

   private final class ObjectLookupFunction extends ObjectSchemaLookup {
      private ObjectLookupFunction(SObjectSchema var2) {
         super(var2);
         this.setErrorReporter(new LoggingExceptionHandler(DefaultTargetFieldsUpdater.class, ErrorLevel.LOG));
      }

      @Override
      protected SObjectSchema lookup(String var1) throws Exception {
         return DefaultTargetFieldsUpdater.this.sfdcFacade.refreshFlatObject(var1);
      }
   }
}
