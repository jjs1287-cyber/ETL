package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSfdcWizardFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizardCompleter;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;

public final class BulkLoadWizardFactory extends AbstractSfdcWizardFactory<SalesforceBulkLoad> {
   private final SupportedQueryPolicy supportedQueryPolicy;
   private final ImmutableList<DataLocationType> fileTypes;

   public BulkLoadWizardFactory(FacadeFactory var1, List<DataLocationType> var2, SupportedQueryPolicy var3) {
      super(var1);
      this.fileTypes = ImmutableList.copyOf(var2);
      this.supportedQueryPolicy = var3;
   }

   @Override
   protected String[] getSections() {
      return new String[0];
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      SalesforceBulkLoad var4 = (SalesforceBulkLoad)var2;
      BulkLoadWizard var5 = new BulkLoadWizard(var1, var4, var3, this.createSfdcFacade(), new WizardSections(this.getIncludeObjectSummary()));
      var5.setSupportedQueryPolicy(this.supportedQueryPolicy);
      this.configureWizard(var5, var1, var4, var3);
      BulkWizardPageChain var6 = new BulkWizardPageChain(var5, this.fileTypes, this.getIncludeObjectSummary());
      var5.setPageChain(var6);
      return var5;
   }

   @Override
   protected SalesforceWizardCompleter<SalesforceBulkLoad> createCompleter(SalesforceWizard<SalesforceBulkLoad> var1, EntityWizardContext var2, Folder var3) {
      return new BulkLoadWizardFactory.Completer((BulkLoadWizard)var1, var2.getProject(), var2.getProjectPersistor(), var3);
   }

   private static class Completer extends SalesforceWizardCompleter<SalesforceBulkLoad> {
      public Completer(BulkLoadWizard var1, IntegrationProject var2, ProjectPersistor var3, Folder var4) {
         super(var1, var2, var3, var4);
      }

      @Override
      protected void doAdditionalPostSaveWork() {
      }
   }
}
