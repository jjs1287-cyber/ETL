package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSfdcWizardFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.MappingStructureWriter;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSourceConfig;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizardCompleter;
import java.util.List;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.ObjectMethodFilter;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsOwner;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.partner.crom.UcuRequestCromBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.UcuResponseCromBuilder;

public final class UcuWizardFactory<T extends AbstractSalesforceUCU> extends AbstractSfdcWizardFactory<T> {
   private final Class<T> itemClass;
   private final RequestSourceConfig sourceConfig;

   public UcuWizardFactory(FacadeFactory var1, RequestSourceConfig var2, Class<T> var3) {
      super(var1);
      this.sourceConfig = var2;
      this.itemClass = var3;
      this.setSaveText("Map && Finish");
   }

   @Override
   protected String[] getSections() {
      return WizardSections.all(this.getIncludeObjectSummary());
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      AbstractSalesforceUCU var4 = this.itemClass.cast(var2);
      ObjectSelectionModel var5 = this.createObjectSelectionModel((T)var4);
      UcuSalesforceWizard var6 = new UcuSalesforceWizard<>(var1, var4, var3, this.createSfdcFacade(), var5);
      UcuWizardPageChain var7 = new UcuWizardPageChain(var6, var5, this.sourceConfig, this.getIncludeObjectSummary());
      var6.setPageChain(var7);
      this.configureWizard(var6, var1, (T)var4, var3);
      return var6;
   }

   private ObjectSelectionModel createObjectSelectionModel(T var1) {
      ObjectSelectionModel var2 = new ObjectSelectionModel();
      var2.setObjectFilter(new ObjectMethodFilter(var1.getMethod()));
      boolean var3 = var1.hasExternalIdField();
      var2.setFieldSelectionRequired(var3);
      return var2;
   }

   @Override
   protected SalesforceWizardCompleter<T> createCompleter(SalesforceWizard<T> var1, EntityWizardContext var2, Folder var3) {
      return new UcuWizardFactory.Completer<>((UcuSalesforceWizard<T>)var1, var2.getProject(), var2.getProjectPersistor(), var3);
   }

   private static class Completer<E extends AbstractSalesforceUCU> extends SalesforceWizardCompleter<E> {
      public Completer(UcuSalesforceWizard<E> var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
         super(var1, var2, var3, var4);
      }

      @Override
      protected void doPreSaveWork() {
         this.createMappingTrees();
      }

      private void createMappingTrees() {
         UcuSalesforceWizard var1 = (UcuSalesforceWizard)this.getWizard();
         SObjectSchema var2 = var1.getSalesforceObject();
         SfdcMethod var3 = var1.getSfdcMethod();
         UcuRequestCromBuilder var4 = this.createRequestBuilder(var1, var2, var3);
         UcuResponseCromBuilder var5 = new UcuResponseCromBuilder(var3, this.createRequestBuilder(var1, var2, var3));
         MappingStructureWriter var6 = new MappingStructureWriter(
            var1.getRequestTransformation(), var1.getResponseTransformation(), var4, var5, this.getProject()
         );
         var6.writeJtrs();
      }

      private UcuRequestCromBuilder createRequestBuilder(UcuSalesforceWizard var1, SObjectSchema var2, SfdcMethod var3) {
         UcuRequestCromBuilder var4 = new UcuRequestCromBuilder(var3, var2);
         if (var3.supportsForeignKeys()) {
            List var5 = ((RelatedObjectsOwner)var1.getItemToConfigure()).getRelatedObjects();
            var4.setRelatedObjects(var5);
         }

         return var4;
      }

      @Override
      protected Transformation getMappingTransformation() {
         UcuSalesforceWizard var1 = (UcuSalesforceWizard)this.getWizard();
         return var1.getRequestTransformation();
      }

      protected RequestAutoMapper createAutoMappingJob(TransformationPage var1) {
         return new RequestAutoMapper(var1);
      }
   }
}
