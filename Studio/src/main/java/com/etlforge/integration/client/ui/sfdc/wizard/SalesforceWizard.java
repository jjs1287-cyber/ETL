package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.ObjectSummary;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionController;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import com.etlforge.integration.client.ui.sfdc.wizard.login.LoginPage;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.operation.OperationPreferences;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.soap.DefaultSoapHeaderLookup;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUi;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiCollection;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiFactory;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.wait.WaitLock;

public abstract class SalesforceWizard<T extends IntegrationEntity & SalesforceWizardEntity> extends ApplicationWindowWizard<T> {
   private static final String COMPLETED_PREF_KEY = "completed";
   private final SfdcMethod sfdcMethod;
   private final EntityWizardContext context;
   private final SalesforceFacade salesforceFacade;
   private SalesforceConnector connector;
   private final T newItem;
   private volatile String wizardName;
   private final Folder parent;
   private volatile boolean hasBeenSaved;
   private final LoginPage loginPage;
   private volatile boolean autoMap = true;
   private volatile SObjectSchema sobject;
   private Optional<SampleFile> sampleFile = Optional.absent();
   private final Narwhals narwhals;
   private volatile WorkflowBuilder workflowBuilder;
   private volatile ObjectSummary<T> workflow;

   public SalesforceWizard(EntityWizardContext var1, T var2, Folder var3, SfdcMethod var4, SalesforceFacade var5) {
      super(var1.getWindow());
      this.newItem = (T)var2;
      this.parent = var3;
      this.sfdcMethod = var4;
      this.salesforceFacade = var5;
      this.context = var1;
      this.loginPage = new LoginPage(this, var1.getWindow(), var1.getProjectExplorer(), var5.getConnectionFactory());
      this.narwhals = new Narwhals(this);
      this.setWizardItemName("New " + StringUtils.capitalize(var4.name().toLowerCase()));
      this.setFinishWizardOnEventThread(false);
      this.addDisposableResource(this.getLoginPage());
   }

   protected final void setWorkflowBuilder(WorkflowBuilder var1) {
      this.workflowBuilder = var1;
   }

   protected final void setWorkflow(ObjectSummary<T> var1) {
      this.workflow = var1;
   }

   protected final SalesforceFacade getSalesforceFacade() {
      return this.salesforceFacade;
   }

   public final SfdcMethod getSfdcMethod() {
      return this.sfdcMethod;
   }

   public final String getCapitalizedMethodName() {
      String var1 = this.sfdcMethod.getDisplayName();
      return StringUtils.capitalize(var1.toLowerCase());
   }

   public final void setOperationPreferences(OperationPreferences var1) {
      this.workflowBuilder.setOperationPreferences(var1);
   }

   public final void loginToSalesforce(SalesforceConnector var1) throws SalesforceException {
      this.connector = var1;
      this.salesforceFacade.login(var1);
      this.workflowBuilder.setConnector(var1);
   }

   public final SObjectSchemaProvider getSchemaProvider() {
      return this.salesforceFacade.getSchemaProvider();
   }

   public final void setWizardItemName(String var1) {
      String var2 = EntityUtils.getAvailableChildName(this.parent, var1);
      if (!var2.equals(this.wizardName)) {
         this.wizardName = var2;
         this.applyName();
      }
   }

   public final String getWizardItemName() {
      return this.wizardName;
   }

   public Folder getParent() {
      return this.parent;
   }

   public final T getItemToConfigure() {
      return this.newItem;
   }

   public final IntegrationProject getProject() {
      return this.context.getProject();
   }

   public final FileStore getFileStore() {
      return this.context.getFileStore();
   }

   public final EntityExplorerSupport getProjectExplorer() {
      return this.context.getProjectExplorer();
   }

   @Override
   public final void start() {
      final WaitLock var1 = this.getWaitService().startWait();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SalesforceWizard.this.startImpl(var1);
         }
      });
   }

   private void startImpl(WaitLock var1) {
      try {
         this.applyName();
         assert this.getPageChain() != null : "The wizards page chain has not been created.";
         super.start();
         this.postStarted();
         this.narwhals.install();
      } finally {
         var1.release();
      }
   }

   private void applyName() {
      this.setTitle(this.wizardName);
      this.newItem.setName(this.wizardName);
   }

   protected void postStarted() {
   }

   protected T finishWizard() {
      IntegrationProjectLock.acquire();

      try {
         this.saveConnector();
         this.applyPages();
         this.insertCreatedItemsIntoProject();
      } finally {
         IntegrationProjectLock.release();

         try {
            this.closeAllPages();
         } finally {
            this.releaseResources();
         }
      }

      this.hasBeenSaved = true;
      this.copyWsdlToProjectFileStore();
      this.recordCompletionInPreferences();
      return this.getItemToConfigure();
   }

   private void saveConnector() {
      IntegrationEntity var1 = this.getItemToConfigure();
      ((SalesforceWizardEntity)var1).setConnector(this.connector);
   }

   protected abstract void applyPages();

   private void insertCreatedItemsIntoProject() {
      this.workflow.insertIntoProject();
   }

   private void releaseResources() {
      this.narwhals.dispose();
   }

   private void copyWsdlToProjectFileStore() {
      PartnerWsdlToFileStoreCopier var1 = new PartnerWsdlToFileStoreCopier(this.getFileStore());
      var1.copyWsdl();
   }

   private void recordCompletionInPreferences() {
      this.getPreferences().putBoolean("completed", true);
   }

   @Override
   protected final boolean onWizardCancelled() {
      if (this.isDoneOrCancelled()) {
         return true;
      } else {
         boolean var1 = this.isStarted() ? this.promptUserOnCancel() : true;
         if (!var1) {
            this.setCancelledState(false);
            return false;
         } else {
            this.releaseResources();
            this.setCancelledState(true);
            this.processCancel();
            return true;
         }
      }
   }

   private boolean isDoneOrCancelled() {
      return this.hasBeenSaved || this.isInCancelledState();
   }

   private void processCancel() {
      this.getItemToConfigure().delete();
   }

   public final Iterable<IntegrationEntity> getCreatedItems() {
      return this.workflow.getItemsCreatedByWizard();
   }

   public final LoginPage getLoginPage() {
      return this.loginPage;
   }

   public final boolean isAutoMap() {
      return this.autoMap;
   }

   public final void setAutoMap(boolean var1) {
      this.autoMap = var1;
   }

   public final boolean hasCompletedAtLeastOnce() {
      return this.getPreferences().getBoolean("completed", false);
   }

   public final ObjectSelectionController getObjectSelectionController(ObjectSelectionModel var1) {
      ObjectSelectionController var2 = new ObjectSelectionController(var1, this.salesforceFacade);
      var2.setWaitService(this.getDisplayer().getWaitService());
      return var2;
   }

   public final List<SObjectSchema> fetchAvailableSObjects() throws SalesforceException {
      return this.salesforceFacade.fetchAvailableSObjects();
   }

   public final SObjectSchema lookupObjectDefinition(String var1) {
      return this.salesforceFacade.lookupObjectDefinition(var1);
   }

   @Override
   public final boolean canFinish() {
      WizardPage var1 = this.getCurrentPage();
      if (var1 == null) {
         return false;
      }

      WizardPageChain var2 = this.getPageChain();
      return var2 != null && var2.canFinish(var1);
   }

   public final SoapHeaderUiCollection getSoapHeaderUiCollection(String var1) {
      ArrayList var2 = Lists.newArrayList();
      List var3 = DefaultSoapHeaderLookup.getDefaultHeaders(this.sfdcMethod);
      SoapHeaderUiFactory var4 = new SoapHeaderUiFactory(this.sfdcMethod, var1);
      Comparator var5 = var4.getPreferredOrder();
      Collections.sort(var3, var5);

      for (Object __etlItem298 : var3) {
         SoapHeader var7 = (SoapHeader) __etlItem298;
         SoapHeaderUi var8 = var4.createUi(var7);
         if (var8 != null) {
            var2.add(var8);
         }
      }

      return SoapHeaderUiCollection.of(var2);
   }

   public final Optional<SampleFile> getSampleFile() {
      return this.sampleFile;
   }

   public final void setSampleFile(Optional<SampleFile> var1) {
      this.sampleFile = Preconditions.checkNotNull(var1);
   }

   public final SObjectSchema getSalesforceObject() {
      return this.sobject;
   }

   public final void setSalesforceObject(SObjectSchema var1) {
      this.sobject = var1;
      this.setWizardItemName(this.getCapitalizedMethodName() + " " + var1.getLabelPlural());
      this.workflowBuilder.setSalesforceObject(var1);
   }
}
