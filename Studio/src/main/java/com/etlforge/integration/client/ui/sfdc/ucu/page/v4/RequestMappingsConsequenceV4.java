package com.etlforge.integration.client.ui.sfdc.ucu.page.v4;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.RetryPolicyMapper;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceUpsert;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.entity.sfdc.UCURequestHeader;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;

final class RequestMappingsConsequenceV4 implements SaveConsequence {
   private final SalesforceUcuPageV4 page;
   private Pair<UCURequestHeader, String> originalHeader;
   private RetryPolicy originalRetryPolicy;
   private final SfdcMethodConfig methodConfig;
   private final EntityPersistor persistor;

   public RequestMappingsConsequenceV4(SalesforceUcuPageV4 var1, SfdcMethodConfig var2, EntityPersistor var3) {
      this.page = var1;
      this.originalHeader = this.getHeaderFromObject();
      this.originalRetryPolicy = this.getOriginalRetryPolicy();
      this.methodConfig = var2;
      this.persistor = var3;
   }

   private Pair<UCURequestHeader, String> getHeaderFromObject() {
      AbstractSalesforceUCU var1 = this.page.getObject();
      String var2 = var1 instanceof SalesforceUpsert ? ((SalesforceUpsert)var1).getExternalIdFieldName() : null;
      return Pair.of(var1.getRequestHeader(), var2);
   }

   private RetryPolicy getOriginalRetryPolicy() {
      AbstractSalesforceUCU var1 = this.page.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getRequestTransformation();
      return var3 == null ? RetryPolicy.NONE : RetryPolicyMapper.fromTransformation(var3);
   }

   @Override
   public void apply() {
      Pair var1 = this.getHeaderFromObject();
      RetryPolicy var2 = this.page.getRetryPolicy();
      if (!var1.equals(this.originalHeader) || !var2.equals(this.originalRetryPolicy)) {
         Transformation var3 = this.getRequestTransformation();
         if (var3 != null) {
            this.updateRequestMappings(var3, var1, var2);
            this.saveRequest(var3);
         }
      }
   }

   private Transformation getRequestTransformation() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.page.getObject());
      return var1.getRequestTransformation();
   }

   private void updateRequestMappings(Transformation var1, Pair<UCURequestHeader, String> var2, RetryPolicy var3) {
      RequestMapper var4 = new RequestMapper(var1, this.methodConfig);
      var4.setExternalIdFieldName((String)var2.second);
      var4.updateHeaderMappings((UCURequestHeader)var2.first);
      this.originalHeader = var2;
      if (!this.originalRetryPolicy.equals(var3)) {
         var4.updateRetryPolicy(var3);
         this.originalRetryPolicy = var3;
      }
   }

   private void saveRequest(Transformation var1) {
      try {
         this.persistor.save(var1);
      } catch (InterchangeSavingException var3) {
         ErrorLog.attention(this.getClass(), "The request Transformation could not be saved: " + var3.getMessage(), var3);
      }
   }

   @Override
   public boolean isAllowed() {
      return true;
   }
}
