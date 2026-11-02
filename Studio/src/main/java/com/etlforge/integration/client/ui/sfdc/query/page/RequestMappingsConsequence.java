package com.etlforge.integration.client.ui.sfdc.query.page;

import com.etlforge.integration.client.sfdc.data.SoapHeaderMappingsReplacer;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import java.util.List;
import java.util.Set;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

final class RequestMappingsConsequence implements SaveConsequence {
   private final SalesforceQueryPage page;
   private final SoapHeaderEditor soapHeaderEditor;
   private Set<SoapHeader> originalSoapHeaders;
   private final EntityPersistor persistor;

   public RequestMappingsConsequence(SalesforceQueryPage var1, SoapHeaderEditor var2, EntityPersistor var3) {
      this.page = var1;
      this.soapHeaderEditor = var2;
      this.persistor = var3;
      this.originalSoapHeaders = var2.getOriginalHeaders();
   }

   @Override
   public void apply() {
      Transformation var1 = this.getRequestTransformation();
      if (var1 != null && var1 != SystemTransformation.SALESFORCE_QUERY_REQUEST) {
         Set var2 = this.soapHeaderEditor.getEditedHeaders();
         boolean var3 = !var2.equals(this.originalSoapHeaders);
         if (var3) {
            this.updateRequestMappings(var1, var2);
            this.saveRequest(var1);
         }
      }
   }

   private Transformation getRequestTransformation() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.page.getObject());
      return var1.getRequestTransformation();
   }

   private void updateRequestMappings(Transformation var1, Set<SoapHeader> var2) {
      synchronized (var1.getDataLock()) {
         List var4 = var1.getMapList();
         SoapHeaderMappingsReplacer var5 = new SoapHeaderMappingsReplacer(var2);
         var5.replaceSoapHeaders(var4);
         var1.setMapList(var4);
      }

      this.originalSoapHeaders = var2;
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
