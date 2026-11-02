package com.etlforge.integration.client.ui.sfdc.delete;

import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.AbstractSfdcWizardItemConfig;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.delete.page.SalesforceDeletePage;
import com.etlforge.integration.client.ui.sfdc.delete.page.SalesforceDeletePageConfig;
import com.etlforge.integration.client.ui.sfdc.delete.page.v4.SalesforceDeletePageConfigV4;
import com.etlforge.integration.client.ui.sfdc.delete.page.v4.SalesforceDeletePageV4;
import com.etlforge.integration.data.entity.SalesforceDelete;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SalesforceDeleteConfig extends AbstractSfdcWizardItemConfig {
   private ApplicationAction showHistoryAction;

   public SalesforceDeleteConfig(FacadeFactory var1, EntityWizardFactory var2, List<LinkType> var3) {
      super(EntityType.SalesforceDelete, var1, var2, var3);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      SalesforceDelete var2 = (SalesforceDelete)var1;
      return var2.isBasedOnPartnerWsdl() ? new SalesforceDeletePage(var2) : new SalesforceDeletePageV4(var2);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SalesforceDelete var4 = (SalesforceDelete)var2;
      if (var4.isBasedOnPartnerWsdl()) {
         SalesforceDeletePageConfig var5 = new SalesforceDeletePageConfig(var1, var3);
         this.installDefaultCapabilitiesInPageConfig(var5);
         if (this.showHistoryAction != null) {
            var5.setShowHistoryAction(this.showHistoryAction);
         }

         return var5;
      } else {
         return new SalesforceDeletePageConfigV4(var1, var3, this.getOperationRunnerFactory(), this.getOperationTesterFactory());
      }
   }

   public void setShowHistoryAction(ApplicationAction var1) {
      this.showHistoryAction = var1;
   }
}
