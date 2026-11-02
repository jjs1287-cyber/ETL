package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPageConfig;
import com.etlforge.integration.client.ui.sfdc.page.SalesforceWizardPageLog;
import com.etlforge.integration.client.ui.sfdc.page.SoapHeaderEditor;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SalesforceUcuPageConfig extends AbstractSalesforceItemPageConfig<SalesforceUcuPage> {
   private ExternalIdFieldSelector externalIdSelector;
   private SalesforceFacade sfdcFacade;
   private Optional<ApplicationAction> showHistoryAction = Optional.absent();

   public SalesforceUcuPageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   public void setSalesforceFacade(SalesforceFacade var1) {
      this.sfdcFacade = var1;
   }

   public void setExternalIdSelector(ExternalIdFieldSelector var1) {
      this.externalIdSelector = var1;
   }

   public void setShowHistoryAction(ApplicationAction var1) {
      this.showHistoryAction = Optional.of(var1);
   }

   protected Optional<? extends SaveConsequence> createRequestMappingsConsequence(SalesforceUcuPage var1, SoapHeaderEditor var2, EntityPersistor var3) {
      RequestMappingsConsequence var4 = new RequestMappingsConsequence(var1, var2, this.externalIdSelector, var3);
      var4.setSalesforceFacade(this.sfdcFacade);
      var4.setLogger(SalesforceWizardPageLog.LOG);
      return Optional.of(var4);
   }

   protected void doAdditionalConfig(SalesforceUcuPage var1) {
      if (this.showHistoryAction.isPresent()) {
         ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.showHistoryAction.get());
         var1.setViewHistoryAction(var2);
      }

      var1.setExternalIdSelector(this.externalIdSelector);
   }
}
