package com.etlforge.integration.client.ui.sfdc.delete.page;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.RetryPolicyMapper;
import com.etlforge.integration.client.sfdc.data.SoapHeaderMappingsReplacer;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.List;
import java.util.Set;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class RequestMappingsConsequence implements SaveConsequence {
   private final SalesforceDeletePage page;
   private final SoapHeaderEditor soapHeaderEditor;
   private Set<SoapHeader> originalSoapHeaders;
   private RetryPolicy originalRetryPolicy;
   private final EntityPersistor persistor;

   public RequestMappingsConsequence(SalesforceDeletePage var1, SoapHeaderEditor var2, EntityPersistor var3) {
      this.page = var1;
      this.soapHeaderEditor = var2;
      this.persistor = var3;
      this.originalSoapHeaders = var2.getOriginalHeaders();
      this.originalRetryPolicy = this.getOriginalRetryPolicy();
   }

   private RetryPolicy getOriginalRetryPolicy() {
      SalesforceDelete var1 = this.page.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getRequestTransformation();
      return var3 == null ? RetryPolicy.NONE : RetryPolicyMapper.fromTransformation(var3);
   }

   @Override
   public void apply() {
      Transformation var1 = this.getRequestTransformation();
      if (var1 != null) {
         Set var2 = this.soapHeaderEditor.getEditedHeaders();
         boolean var3 = !var2.equals(this.originalSoapHeaders);
         RetryPolicy var4 = this.page.getRetryPolicy();
         boolean var5 = !var4.equals(this.originalRetryPolicy);
         if (var3 || var5) {
            this.updateRequestMappings(var1, var2, var4);
            this.saveRequest(var1);
         }
      }
   }

   private Transformation getRequestTransformation() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.page.getObject());
      return var1.getRequestTransformation();
   }

   private void updateRequestMappings(Transformation var1, Set<SoapHeader> var2, RetryPolicy var3) {
      synchronized (var1.getDataLock()) {
         List var5 = var1.getMapList();
         SoapHeaderMappingsReplacer var6 = new SoapHeaderMappingsReplacer(var2);
         var6.replaceSoapHeaders(var5);
         RetryPolicyMapper var7 = new RetryPolicyMapper(var3);
         var7.applyRetryPolicy(var5);
         var1.setMapList(var5);
      }

      this.originalSoapHeaders = var2;
      this.originalRetryPolicy = var3;
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
