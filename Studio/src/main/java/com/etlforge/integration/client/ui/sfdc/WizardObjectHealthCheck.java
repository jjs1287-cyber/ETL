package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;

public final class WizardObjectHealthCheck {
   public static boolean isHealthy(SalesforceWizardEntity var0) {
      WizardItemBrowser var1 = new WizardItemBrowser(var0);
      Operation var2 = var1.getImplementingOperation();
      if (var2 == null) {
         return false;
      }

      if (!var0.getMethod().usesSoap()) {
         return true;
      }

      WebServiceCall var3 = var1.getWebServiceCall();
      if (var3 == null) {
         return false;
      }

      Transformation var4 = var1.getRequestTransformation();
      if (var4 == null) {
         return false;
      }

      Transformation var5 = var1.getResponseTransformation();
      return var5 != null;
   }

   private WizardObjectHealthCheck() {
   }
}
