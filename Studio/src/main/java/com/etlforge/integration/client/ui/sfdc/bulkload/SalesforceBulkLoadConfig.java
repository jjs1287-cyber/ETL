package com.etlforge.integration.client.ui.sfdc.bulkload;

import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.AbstractSfdcWizardItemConfig;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.bulkload.page.BulkSoqlEditor;
import com.etlforge.integration.client.ui.sfdc.bulkload.page.MappingsEditor;
import com.etlforge.integration.client.ui.sfdc.bulkload.page.SalesforceBulkLoadPage;
import com.etlforge.integration.client.ui.sfdc.bulkload.page.SalesforceBulkLoadPageConfig;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.parser.CompoundSupportedQueryPolicy;
import org.etlforge.integration.salesforce.parser.FixedObjectPolicy;
import org.etlforge.integration.salesforce.parser.NoAggregateFunctionsPolicy;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SalesforceBulkLoadConfig extends AbstractSfdcWizardItemConfig {
   private final FacadeFactory facadeFactory;
   private SupportedQueryPolicy supportedQueryPolicy = new NoAggregateFunctionsPolicy();
   private ApplicationAction showHistoryAction;

   public SalesforceBulkLoadConfig(FacadeFactory var1, EntityWizardFactory var2, List<LinkType> var3) {
      super(EntityType.SalesforceBulkLoad, var1, var2, var3);
      this.facadeFactory = var1;
   }

   public void setSupportedQueryPolicy(SupportedQueryPolicy var1) {
      this.supportedQueryPolicy = var1;
   }

   public void setShowHistoryAction(ApplicationAction var1) {
      this.showHistoryAction = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new SalesforceBulkLoadPage((SalesforceBulkLoad)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SalesforceBulkLoad var4 = (SalesforceBulkLoad)var2;
      SalesforceBulkLoadPageConfig var5 = new SalesforceBulkLoadPageConfig(var1, var3);
      this.installMappingsEditor(var5, var4);
      this.installSoqlEditor(var5, var4);
      this.configureStatusHistoryCapabilities(var4, var5);
      this.installDefaultCapabilitiesInPageConfig(var5);
      if (var4.isQuery()) {
         var5.setMonitorListener(null);
      }

      return var5;
   }

   private void configureStatusHistoryCapabilities(SalesforceBulkLoad var1, SalesforceBulkLoadPageConfig var2) {
      if (this.showHistoryAction != null && var1.isLoad()) {
         var2.setShowHistoryAction(this.showHistoryAction);
      }
   }

   private void installMappingsEditor(SalesforceBulkLoadPageConfig var1, SalesforceBulkLoad var2) {
      BulkAction var3 = var2.getAction();
      if (var3.isLoad()) {
         MappingsEditor var4 = new MappingsEditor(var2, this.facadeFactory.newFacade());
         var1.setMappingsEditor(var4);
      }
   }

   private void installSoqlEditor(SalesforceBulkLoadPageConfig var1, SalesforceBulkLoad var2) {
      if (var2.isQuery()) {
         BulkSoqlEditor var3 = new BulkSoqlEditor(var2, this.facadeFactory);
         CompoundSupportedQueryPolicy var4 = new CompoundSupportedQueryPolicy(this.supportedQueryPolicy, new FixedObjectPolicy(var2.getObjectName()));
         var3.setSupportedQueryPolicy(var4);
         var1.setSoqlEditor(var3);
      }
   }

   @Override
   protected boolean openEditorForNewItem() {
      return true;
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new SalesforceBulkLoadToolTip((SalesforceBulkLoad)var1);
   }
}
