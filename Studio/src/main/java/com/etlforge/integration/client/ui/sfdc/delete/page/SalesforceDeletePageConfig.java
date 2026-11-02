package com.etlforge.integration.client.ui.sfdc.delete.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPageConfig;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SalesforceDeletePageConfig extends AbstractSalesforceItemPageConfig<SalesforceDeletePage> {
   private Optional<ApplicationAction> showHistoryAction = Optional.absent();

   public SalesforceDeletePageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   public void setShowHistoryAction(ApplicationAction var1) {
      this.showHistoryAction = Optional.of(var1);
   }

   protected Optional<? extends SaveConsequence> createRequestMappingsConsequence(SalesforceDeletePage var1, SoapHeaderEditor var2, EntityPersistor var3) {
      return Optional.of(new RequestMappingsConsequence(var1, var2, var3));
   }

   protected void doAdditionalConfig(SalesforceDeletePage var1) {
      if (this.showHistoryAction.isPresent()) {
         ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.showHistoryAction.get());
         var1.setViewHistoryAction(var2);
      }
   }
}
