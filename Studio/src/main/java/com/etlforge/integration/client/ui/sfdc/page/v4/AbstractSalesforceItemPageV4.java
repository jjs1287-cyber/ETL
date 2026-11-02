package com.etlforge.integration.client.ui.sfdc.page.v4;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.sfdc.InterchangeKnownWsdlsStore;
import com.etlforge.integration.client.ui.sfdc.InvalidWizardObjectStateUi;
import com.etlforge.integration.client.ui.sfdc.RunOperationAction;
import com.etlforge.integration.client.ui.sfdc.TestOperationAction;
import com.etlforge.integration.client.ui.sfdc.WizardObjectHealthCheck;
import com.etlforge.integration.client.ui.sfdc.page.ImplementingObjectsLinkPanel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTester;
import org.etlforge.integration.client.ui.operation.OperationMonitorPageSite;
import org.etlforge.integration.client.ui.operation.runner.OperationRunner;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public abstract class AbstractSalesforceItemPageV4<T extends IntegrationEntity & SalesforceWizardEntity>
   extends IntegrationEntityPage
   implements OperationMonitorSite {
   private final SfdcMethod sfdcMethod;
   private OperationRunner runner;
   private OperationTester tester;
   private CredentialsEditor credentialsEditor;
   private OperationMonitorSite activityMonitor = OperationMonitorSite.INACTIVE;
   private ImplementingObjectsLinkPanel links;

   protected AbstractSalesforceItemPageV4(T var1, SfdcMethod var2) {
      super(new IntegrationEntityPageModel(var1));
      this.sfdcMethod = var2;
      this.setUseScroll(false);
   }

   @Override
   public final T getObject() {
      return (T)super.getObject();
   }

   public final void setOperationRunner(OperationRunner var1) {
      this.runner = var1;
   }

   public final void installMonitoring(OperationMonitor var1) {
      this.activityMonitor = new OperationMonitorPageSite(var1, this);
   }

   public final void setOperationTester(OperationTester var1) {
      this.tester = var1;
   }

   @Override
   public final void setEntityPersistor(EntityPersistor var1) {
      Preconditions.checkNotNull(var1, "persistor");
      super.setEntityPersistor(var1);
      Receiver var2 = new Receiver<EntityPageSaveJob>() {
         public void handle(EntityPageSaveJob var1) {
            AbstractSalesforceItemPageV4.this.addSaveJob(var1);
         }
      };
      this.credentialsEditor = new CredentialsEditor(this.getObject(), var1, var2);
      this.handleEntityPersistor(var1, var2);
   }

   protected void handleEntityPersistor(EntityPersistor var1, Receiver<EntityPageSaveJob> var2) {
   }

   protected abstract AbstractSalesforceItemPanelV4<T> createDataPanel();

   protected final void doDefaultPanelConfiguration(AbstractSalesforceItemPanelV4<T> var1) {
      this.addRunAndTestActions(var1);
      var1.setCredentialsEditor(this.credentialsEditor);
   }

   private void addRunAndTestActions(AbstractSalesforceItemPanelV4<T> var1) {
      Operation var2 = this.lookupOperation();
      String var3 = StringUtils.capitalize(this.sfdcMethod.name().toLowerCase());
      RunOperationAction var4 = new RunOperationAction(var3, var2, this.runner, this, this);
      var1.setRunAction(var4);
      TestOperationAction var5 = new TestOperationAction(var3, var2, this.tester, this);
      var1.setTestAction(var5);
   }

   public final void setLinks(ImplementingObjectsLinkPanel var1) {
      this.links = var1;
   }

   protected final ImplementingObjectsLinkPanel getLinks() {
      return this.links;
   }

   protected final Operation lookupOperation() {
      IntegrationEntity var1 = this.getObject();
      WizardItemBrowser var2 = new WizardItemBrowser((SalesforceWizardEntity)var1);
      return var2.getImplementingOperation();
   }

   @Override
   protected final void layoutPage(IntegrationEntityPanel var1) {
      IntegrationEntity var2 = this.getObject();
      if (WizardObjectHealthCheck.isHealthy((SalesforceWizardEntity)var2)) {
         var1.getUi().setOpaque(false);
         if (this.activityMonitor instanceof OperationMonitorPageSite) {
            ((OperationMonitorPageSite)this.activityMonitor).layoutPage(var1.getUi(), true, true);
         } else {
            super.layoutPage(var1);
         }
      } else {
         this.setMainPanel(InvisiblePanel.of(new InvalidWizardObjectStateUi()));
      }
   }

   @Override
   public final void openActivityMonitor(long var1) {
      this.activityMonitor.openActivityMonitor(var1);
   }

   @Override
   public final void closeActivityMonitor() {
      this.activityMonitor.closeActivityMonitor();
   }

   static {
      InterchangeKnownWsdlsStore.getStore();
   }
}
