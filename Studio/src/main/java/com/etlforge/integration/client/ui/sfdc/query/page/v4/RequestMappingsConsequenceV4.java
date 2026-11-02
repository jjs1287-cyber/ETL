package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.RetryPolicyMapper;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;

final class RequestMappingsConsequenceV4 implements SaveConsequence {
   private final SalesforceQueryPageV4 page;
   private final SfdcMethodConfig queryConfig;
   private RetryPolicy originalRetryPolicy;
   private final EntityPersistor persistor;
   private boolean hasConsequence;

   public RequestMappingsConsequenceV4(SalesforceQueryPageV4 var1, SfdcMethodConfig var2, EntityPersistor var3) {
      this.page = var1;
      this.queryConfig = var2;
      this.persistor = var3;
      this.originalRetryPolicy = this.getOriginalRetryPolicy();
   }

   private RetryPolicy getOriginalRetryPolicy() {
      SalesforceQuery var1 = this.page.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getRequestTransformation();
      return var3 == null ? RetryPolicy.NONE : RetryPolicyMapper.fromTransformation(var3);
   }

   @Override
   public boolean isAllowed() {
      this.hasConsequence = this.page.hasQueryStringOrBatchSizeBeenModified() || !this.originalRetryPolicy.equals(this.page.getRetryPolicy());
      if (this.hasConsequence) {
      }

      return true;
   }

   @Override
   public void apply() {
      if (this.hasConsequence) {
         this.applyChangesToRequestTransformation();
      }
   }

   private void applyChangesToRequestTransformation() {
      Transformation var1 = this.getRequestTransformation();
      if (var1 != null) {
         this.updateRequestMappings(var1);
         this.saveRequest(var1);
      }
   }

   private Transformation getRequestTransformation() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.page.getObject());
      return var1.getRequestTransformation();
   }

   private void updateRequestMappings(Transformation var1) {
      String var2 = this.page.getQueryString();
      int var3 = this.page.getBatchSize();
      RetryPolicy var4 = this.page.getRetryPolicy();
      RequestMapperV4 var5 = new RequestMapperV4(var1, this.queryConfig);
      var5.updateQueryStringAndBatchSize(var2, var3);
      if (!var4.equals(this.originalRetryPolicy)) {
         var5.updateRetryPolicy(var4);
         this.originalRetryPolicy = var4;
      }
   }

   private void saveRequest(Transformation var1) {
      try {
         this.persistor.save(var1);
      } catch (InterchangeSavingException var3) {
         ErrorLog.attention(this.getClass(), "The request Transformation could not be saved: " + var3.getMessage(), var3);
      }
   }
}
