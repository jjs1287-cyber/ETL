package com.etlforge.integration.client.ui.sfdc.query.page;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.SfdcMethod;

final class QueryAllConsequence implements SaveConsequence {
   private final SalesforceQueryPage page;
   private boolean originalValue;
   private final EntityPersistor persistor;

   public QueryAllConsequence(SalesforceQueryPage var1, EntityPersistor var2) {
      this.page = var1;
      this.persistor = var2;
      this.originalValue = var1.getObject().isQueryAll();
   }

   @Override
   public boolean isAllowed() {
      return true;
   }

   @Override
   public void apply() {
      boolean var1 = this.page.isQueryAll();
      if (var1 != this.originalValue) {
         this.applyNewValue(var1);
         this.originalValue = var1;
      }
   }

   private void applyNewValue(boolean var1) {
      Operation var2 = this.getOperation();
      if (var2 != null) {
         SfdcMethod var3 = var1 ? SfdcMethod.QUERY_ALL : SfdcMethod.QUERY;
         var2.setSalesforceWizardMethodName(var3.getMethodName());
         this.saveOperation(var2);
      }
   }

   private Operation getOperation() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.page.getObject());
      return var1.getImplementingOperation();
   }

   private void saveOperation(Operation var1) {
      try {
         this.persistor.save(var1);
      } catch (InterchangeSavingException var3) {
         ErrorLog.attention(this.getClass(), "The implementing Operation could not be saved: " + var3.getMessage(), var3);
      }
   }
}
