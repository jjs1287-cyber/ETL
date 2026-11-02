package com.etlforge.integration.client.ui.sfdc.ucu;

import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.AbstractSfdcWizardItemConfig;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.ucu.page.SalesforceUcuPage;
import com.etlforge.integration.client.ui.sfdc.ucu.page.SalesforceUcuPageConfig;
import com.etlforge.integration.client.ui.sfdc.ucu.page.v4.SalesforceUcuPageConfigV4;
import com.etlforge.integration.client.ui.sfdc.ucu.page.v4.SalesforceUcuPageV4;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceUpdate;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SalesforceUpdateConfig extends AbstractSfdcWizardItemConfig {
   private ApplicationAction showHistoryAction;

   public SalesforceUpdateConfig(FacadeFactory var1, EntityWizardFactory var2, List<LinkType> var3) {
      super(EntityType.SalesforceUpdate, var1, var2, var3);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      SalesforceUpdate var2 = (SalesforceUpdate)var1;
      return var2.isBasedOnPartnerWsdl() ? new SalesforceUcuPage(var2) : new SalesforceUcuPageV4(var2);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SalesforceUpdate var4 = (SalesforceUpdate)var2;
      if (var4.isBasedOnPartnerWsdl()) {
         SalesforceUcuPageConfig var5 = new SalesforceUcuPageConfig(var1, var3);
         this.installDefaultCapabilitiesInPageConfig(var5);
         var5.setSalesforceFacade(this.newSalesforceFacade());
         if (this.showHistoryAction != null) {
            var5.setShowHistoryAction(this.showHistoryAction);
         }

         return var5;
      } else {
         return new SalesforceUcuPageConfigV4(var1, var3, this.getOperationRunnerFactory(), this.getOperationTesterFactory());
      }
   }

   public void setShowHistoryAction(ApplicationAction var1) {
      this.showHistoryAction = var1;
   }
}
