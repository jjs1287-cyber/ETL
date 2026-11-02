package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.sfdc.data.SoapHeaderTranslator;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.NameChangeConsequence;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditorFactory;
import com.etlforge.integration.client.ui.sfdc.RunOperationAction;
import com.etlforge.integration.client.ui.sfdc.TestOperationAction;
import com.etlforge.integration.client.ui.sfdc.WizardItemOperationTester;
import com.etlforge.integration.client.ui.sfdc.page.cli.ReviewOperationExecutor;
import com.etlforge.integration.client.ui.sfdc.page.cli.ReviewRequestExecutor;
import com.etlforge.integration.client.ui.sfdc.page.cli.ReviewResponseExecutor;
import java.util.Collections;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.cli.CommandFactory;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorListener;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.SaveConsequence;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.soap.ui.SoapHeaderUiFactory;
import org.etlforge.xml.ExpandedName;

public abstract class AbstractSalesforceItemPageConfig<P extends AbstractSalesforceItemPage<?>> extends DefaultPageConfig {
   private ImmutableList<LinkType> linkTypes;
   private OperationRunnerFactory runnerFactory;
   private OperationTesterFactory testerFactory;
   private OperationMonitorApis monitorApis;
   private OperationMonitorListener monitorListener;
   private RetryPolicyEditorFactory retryPolicyEditorFactory;
   private ApplicationAction deleteAction;
   private P wizPage;
   private boolean isSoapCall;
   private boolean includeTestAction = true;

   public AbstractSalesforceItemPageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   public final void setIncludeTestAction(boolean var1) {
      this.includeTestAction = var1;
   }

   public final void setRunnerFactory(OperationRunnerFactory var1) {
      this.runnerFactory = var1;
   }

   public final void setTesterFactory(OperationTesterFactory var1) {
      this.testerFactory = var1;
   }

   public final void setMonitorApis(OperationMonitorApis var1) {
      this.monitorApis = var1;
   }

   public final void setMonitorListener(OperationMonitorListener var1) {
      this.monitorListener = var1;
   }

   public final void setDeleteAction(ApplicationAction var1) {
      this.deleteAction = var1;
   }

   public final void setLinkTypes(ImmutableList<LinkType> var1) {
      this.linkTypes = var1;
   }

   public final void setRetryPolicyEditorFactory(RetryPolicyEditorFactory var1) {
      this.retryPolicyEditorFactory = var1;
   }

   @Override
   protected final void preConfiguration(IntegrationEntityPage var1) {
      this.wizPage = (P)var1;
      this.installActions();
   }

   private void installActions() {
      this.wizPage.setDeleteAction(this.deleteAction);
      Operation var1 = this.lookupOperation();
      String var2 = this.getNameForRunAndTestActions();
      if (this.runnerFactory != null) {
         RunOperationAction var3 = new RunOperationAction(var2, var1, this.runnerFactory.newRunner(), this.wizPage, this.wizPage);
         this.wizPage.setRunAction(var3);
         this.addToolbarButton(this.createToolbarButton(var3));
      }

      if (this.includeTestAction && this.testerFactory != null) {
         WizardItemOperationTester var5 = new WizardItemOperationTester(this.wizPage, this.testerFactory);
         TestOperationAction var4 = new TestOperationAction(var2, var1, var5, this.wizPage);
         this.wizPage.setTestAction(var4);
      }
   }

   private String getNameForRunAndTestActions() {
      SfdcMethod var1 = this.wizPage.getSfdcMethod();
      if (var1 == SfdcMethod.BULK_LOAD) {
         SalesforceBulkLoad var2 = (SalesforceBulkLoad)this.wizPage.getObject();
         BulkAction var3 = var2.getAction();
         return "Bulk " + var3.toSfdcMethod().getDisplayName();
      } else {
         return var1.getDisplayName();
      }
   }

   private Operation lookupOperation() {
      SalesforceWizardEntity var1 = (SalesforceWizardEntity)this.wizPage.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      return var2.getImplementingOperation();
   }

   @Override
   protected final void postConfiguration(IntegrationEntityPage var1) {
      SalesforceWizardEntity var2 = (SalesforceWizardEntity)this.wizPage.getObject();
      this.installLinks();
      this.installRetryPolicyEditor();
      this.isSoapCall = var2.getMethod().usesSoap();
      SoapHeaderEditor var3 = this.isSoapCall ? this.installSoapHeaderEditor() : null;
      this.installSaveConsequences(var3);
      this.installMonitoring();
      this.installCommands();
      this.doAdditionalConfig(this.wizPage);
   }

   private void installLinks() {
      if (this.linkTypes != null && !this.linkTypes.isEmpty()) {
         LinkPanelInstaller var1 = new LinkPanelInstaller(this.getPersistor(), this.getProjectExplorer());
         var1.installLinks(this.wizPage, this.linkTypes);
      }
   }

   private void installRetryPolicyEditor() {
      if (this.retryPolicyEditorFactory != null) {
         RetryPolicyEditor var1 = this.retryPolicyEditorFactory.newEditor((SalesforceWizardEntity)this.wizPage.getObject());
         this.wizPage.setRetryPolicyEditor(var1);
      }
   }

   private SoapHeaderEditor installSoapHeaderEditor() {
      SalesforceWizardEntity var1 = (SalesforceWizardEntity)this.wizPage.getObject();
      String var2 = this.getObjectName(var1);
      SoapHeaderUiFactory var3 = new SoapHeaderUiFactory(this.wizPage.getSfdcMethod(), var2);
      this.configureSoapHeaderUiFactory(this.wizPage, var3);
      var3.installSessionHeaderUiFactory(this.wizPage.getConnectorIdSupplier());
      SoapHeaderEditor var4 = new SoapHeaderEditor(var1, new SoapHeaderTranslator(), var3);
      this.wizPage.setSoapHeaderEditor(var4);
      return var4;
   }

   protected void configureSoapHeaderUiFactory(P var1, SoapHeaderUiFactory var2) {
   }

   private String getObjectName(SalesforceWizardEntity var1) {
      ExpandedName var2 = var1.getObject();
      return var2 == null ? null : var2.getLocalName();
   }

   private void installSaveConsequences(SoapHeaderEditor var1) {
      ProjectPersistor var2 = this.getPersistor();
      this.wizPage.addSaveConsequence(new NameChangeConsequence(this.wizPage, var2));
      Optional var3 = this.createRequestMappingsConsequence(this.wizPage, var1, var2);
      if (var3.isPresent()) {
         this.wizPage.addSaveConsequence((SaveConsequence)var3.get());
      }

      for (SaveConsequence var5 : this.createAdditionalSaveConsequences(this.wizPage, var2)) {
         this.wizPage.addSaveConsequence(var5);
      }
   }

   protected Optional<? extends SaveConsequence> createRequestMappingsConsequence(P var1, SoapHeaderEditor var2, EntityPersistor var3) {
      return Optional.absent();
   }

   protected List<? extends SaveConsequence> createAdditionalSaveConsequences(P var1, EntityPersistor var2) {
      return Collections.emptyList();
   }

   private void installMonitoring() {
      if (this.monitorApis != null) {
         OperationMonitor var1 = new OperationMonitor(this.monitorApis);
         if (this.monitorListener != null) {
            this.installMonitorListener(var1);
         }

         this.wizPage.installMonitoring(var1);
      }
   }

   private void installMonitorListener(final OperationMonitor var1) {
      var1.addListener(this.monitorListener);
      this.wizPage.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1x) {
            var1.removeListener(AbstractSalesforceItemPageConfig.this.monitorListener);
         }
      });
   }

   private void installCommands() {
      SalesforceWizardEntity var1 = (SalesforceWizardEntity)this.wizPage.getObject();
      if (var1.getMethod() != SfdcMethod.BULK_LOAD) {
         this.addCommand(CommandFactory.newCommand("review request", new ReviewRequestExecutor(var1)));
         this.addCommand(CommandFactory.newCommand("review response", new ReviewResponseExecutor(var1)));
         if (!ClientInfo.isDataLoader()) {
            this.addCommand(CommandFactory.newCommand("review operation", new ReviewOperationExecutor(var1)));
         }
      }
   }

   protected void doAdditionalConfig(P var1) {
   }
}
