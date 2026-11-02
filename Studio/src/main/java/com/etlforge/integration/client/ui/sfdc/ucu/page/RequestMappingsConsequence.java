package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.RetryPolicyMapper;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.Objects;
import org.etlforge.system.callback.Receiver;
import org.etlforge.xml.ExpandedName;

final class RequestMappingsConsequence implements SaveConsequence {
   private final SalesforceUcuPage page;
   private final ExternalIdFieldSelector externalIdSelector;
   private final SoapHeaderEditor soapHeaderEditor;
   private final EntityPersistor persistor;
   private SalesforceFacade sfdcFacade;
   private RequestMappingsConsequence.Data originalData;
   private RequestMappingsConsequence.Data newData;
   private volatile Logger logger = Logger.getAnonymousLogger();

   public RequestMappingsConsequence(SalesforceUcuPage var1, SoapHeaderEditor var2, ExternalIdFieldSelector var3, EntityPersistor var4) {
      this.page = var1;
      this.externalIdSelector = var3;
      this.soapHeaderEditor = var2;
      this.persistor = var4;
      this.originalData = new RequestMappingsConsequence.Data(
         var2.getOriginalHeaders(),
         this.getOriginalRetryPolicy(),
         var3 != null ? var3.getSelectedFieldName() : null,
         var1.getObject().getDateConversionParams()
      );
   }

   private RetryPolicy getOriginalRetryPolicy() {
      AbstractSalesforceUCU var1 = this.page.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getRequestTransformation();
      return var3 == null ? RetryPolicy.NONE : RetryPolicyMapper.fromTransformation(var3);
   }

   public void setSalesforceFacade(SalesforceFacade var1) {
      this.sfdcFacade = var1;
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   @Override
   public void apply() {
      Transformation var1 = this.getRequestTransformation();
      if (var1 == null) {
         this.logger.severe("No request Transformation found for the Salesforce wizard " + this.page.getObject());
      } else {
         List var2 = this.collectChanges(var1);
         if (!var2.isEmpty()) {
            this.updateRequestMappings(var1, var2);
         }
      }
   }

   private Transformation getRequestTransformation() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.page.getObject());
      return var1.getRequestTransformation();
   }

   private List<RequestMappingsModifier> collectChanges(Transformation var1) {
      Set var2 = this.soapHeaderEditor.getEditedHeaders();
      RetryPolicy var3 = this.page.getRetryPolicy();
      String var4 = this.externalIdSelector == null ? null : this.externalIdSelector.getSelectedFieldName();
      Optional var5 = this.page.getDateConversionParams();
      this.newData = new RequestMappingsConsequence.Data(var2, var3, var4, var5);
      String var6 = this.getObjectName(var1);
      SalesforceConnector var7 = this.page.getSalesforceConnector();
      List var8 = this.newData.collectChanges(this.originalData, var1, this.sfdcFacade, var7, var6);
      this.logger.info("Request mapping changes to perform: " + var8);
      return var8;
   }

   private String getObjectName(Transformation var1) {
      ExpandedName var2 = var1.getSalesforceObject();
      if (var2 == null) {
         var2 = this.page.getObject().getSalesforceObjectName();
         var1.setSalesforceObject(var2);
      }

      return var2.getLocalName();
   }

   private void updateRequestMappings(Transformation var1, List<RequestMappingsModifier> var2) {
      RequestMappingsConsequence.MapListProducer var3 = new RequestMappingsConsequence.MapListProducer(var1, var2);
      RequestMappingsConsequence.MapListConsumer var4 = new RequestMappingsConsequence.MapListConsumer(var1);
      Computation var5 = Computation.create(var3, var4);
      var5.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).withWaitService(this.page.getWaitService()).buildAndExecute();
   }

   @Override
   public boolean isAllowed() {
      return true;
   }

   private static class Data {
      private final Set<SoapHeader> soapHeaders;
      private final RetryPolicy retryPolicy;
      private final String externalId;
      private final Optional<DateConversionParams> dateOptions;

      public Data(Set<SoapHeader> var1, RetryPolicy var2, String var3, Optional<DateConversionParams> var4) {
         this.soapHeaders = var1;
         this.retryPolicy = var2;
         this.externalId = var3;
         this.dateOptions = var4;
      }

      public List<RequestMappingsModifier> collectChanges(
         RequestMappingsConsequence.Data var1, Transformation var2, SalesforceFacade var3, SalesforceConnector var4, String var5
      ) {
         ArrayList var6 = Lists.newArrayList();
         if (!this.soapHeaders.equals(var1.soapHeaders)) {
            SoapHeaderChange var7 = new SoapHeaderChange(this.soapHeaders);
            var6.add(var7);
         }

         if (!this.retryPolicy.equals(var1.retryPolicy)) {
            var6.add(new RetryPolicyChange(this.retryPolicy));
         }

         if (!Objects.equal(this.externalId, var1.externalId)) {
            var6.add(new ExternalIdMappingChange(this.externalId));
         }

         if (!this.dateOptions.equals(var1.dateOptions)) {
            ArrayList var8 = Lists.newArrayList();
            var8.add(new MappingStructureDateFieldsRetriever(var2, var5));
            if (var3 != null && var4 != null) {
               var8.add(new SalesforceDateFieldsRetriever(var5, var3, var4));
            }

            var6.add(new DateOptionsChange(var1.dateOptions, this.dateOptions, var8));
         }

         return var6;
      }
   }

   private class MapListConsumer implements Receiver<List<String>> {
      private final Transformation tx;

      public MapListConsumer(Transformation var2) {
         this.tx = var2;
      }

      public void handle(List<String> var1) {
         synchronized (this.tx.getDataLock()) {
            this.tx.setMapList(var1);
            this.saveTransformation();
         }

         RequestMappingsConsequence.this.originalData = RequestMappingsConsequence.this.newData;
         RequestMappingsConsequence.this.newData = null;
      }

      private void saveTransformation() {
         try {
            RequestMappingsConsequence.this.persistor.save(this.tx);
         } catch (InterchangeSavingException var2) {
            ErrorLog.attention(this.getClass(), "The request Transformation could not be saved: " + var2.getMessage(), var2);
         }
      }
   }

   private class MapListProducer implements Callable<List<String>> {
      private final Transformation tx;
      private final ImmutableList<RequestMappingsModifier> changes;

      public MapListProducer(Transformation var2, List<RequestMappingsModifier> var3) {
         this.tx = var2;
         this.changes = ImmutableList.copyOf(var3);
      }

      public List<String> call() throws Exception {
         List var1;
         synchronized (this.tx.getDataLock()) {
            var1 = this.tx.getMapList();
         }

         for (RequestMappingsModifier var3 : this.changes) {
            var3.setLogger(RequestMappingsConsequence.this.logger);
            var3.modifyMapList(var1);
         }

         return var1;
      }
   }
}
