package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSfdcWizardFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.MappingStructureWriter;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSourceConfig;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizardCompleter;
import com.etlforge.integration.data.entity.SalesforceDelete;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.wsdl.partner.crom.DeleteRequestCromBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.DeleteResponseCromBuilder;

public final class DeleteWizardFactory extends AbstractSfdcWizardFactory<SalesforceDelete> {
   private final RequestSourceConfig sourceConfig;

   public DeleteWizardFactory(FacadeFactory var1, RequestSourceConfig var2) {
      super(var1);
      this.sourceConfig = var2;
      this.setSaveText("Map && Finish");
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all(this.getIncludeObjectSummary());
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      SalesforceDelete var4 = (SalesforceDelete)var2;
      DeleteWizard var5 = new DeleteWizard(var1, var4, var3, this.createSfdcFacade());
      DeleteWizardPageChain var6 = new DeleteWizardPageChain(var5, this.sourceConfig, this.getIncludeObjectSummary());
      var5.setPageChain(var6);
      this.configureWizard(var5, var1, var4, var3);
      return var5;
   }

   @Override
   protected SalesforceWizardCompleter<SalesforceDelete> createCompleter(SalesforceWizard<SalesforceDelete> var1, EntityWizardContext var2, Folder var3) {
      return new DeleteWizardFactory.Completer((DeleteWizard)var1, var2.getProject(), var2.getProjectPersistor(), var3);
   }

   private static class Completer extends SalesforceWizardCompleter<SalesforceDelete> {
      public Completer(DeleteWizard var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
         super(var1, var2, var3, var4);
      }

      @Override
      protected void doPreSaveWork() {
         this.createMappingTrees();
      }

      private void createMappingTrees() {
         DeleteWizard var1 = (DeleteWizard)this.getWizard();
         MappingStructureWriter var2 = new MappingStructureWriter(
            var1.getRequestTransformation(),
            var1.getResponseTransformation(),
            new DeleteRequestCromBuilder(),
            new DeleteResponseCromBuilder(),
            this.getProject()
         );
         var2.writeJtrs();
      }

      @Override
      protected Transformation getMappingTransformation() {
         DeleteWizard var1 = (DeleteWizard)this.getWizard();
         return var1.getRequestTransformation();
      }

      protected RequestAutoMapper createAutoMappingJob(TransformationPage var1) {
         return new RequestAutoMapper(var1);
      }
   }
}
