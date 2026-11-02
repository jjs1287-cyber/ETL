package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.ui.sfdc.InvalidWizardObjectStateUi;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import com.etlforge.integration.client.ui.sfdc.WizardObjectHealthCheck;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorSite;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.EntityPageSaveJob;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.operation.OperationMonitorPageSite;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public abstract class AbstractSalesforceItemPage<T extends IntegrationEntity & SalesforceWizardEntity>
   extends IntegrationEntityPage
   implements OperationMonitorSite {
   private final SfdcMethod sfdcMethod;
   private Action runAction;
   private Action testAction;
   private ActiveObjectPageAction deleteAction;
   private SoapHeaderEditor soapHeaderEditor;
   private OperationMonitorSite activityMonitor = OperationMonitorSite.INACTIVE;
   private RetryPolicyEditor retryPolicyEditor;
   private ImplementingObjectsLinkPanel links;

   protected AbstractSalesforceItemPage(T var1, SfdcMethod var2) {
      super(new IntegrationEntityPageModel(var1));
      this.sfdcMethod = var2;
      this.setUseScroll(false);
   }

   @Override
   public final T getObject() {
      return (T)super.getObject();
   }

   public final SfdcMethod getSfdcMethod() {
      return this.sfdcMethod;
   }

   public final void setLinks(ImplementingObjectsLinkPanel var1) {
      this.links = var1;
   }

   public final void setSoapHeaderEditor(SoapHeaderEditor var1) {
      this.soapHeaderEditor = var1;
   }

   public final void setRunAction(Action var1) {
      this.runAction = var1;
   }

   public final void setTestAction(Action var1) {
      this.testAction = var1;
   }

   public final void setDeleteAction(ApplicationAction var1) {
      this.deleteAction = new ActiveObjectPageAction(this, var1);
      this.deleteAction.setToolTip("Delete this operation");
      this.addActiveObjectAction(this.deleteAction);
   }

   public final void installMonitoring(OperationMonitor var1) {
      this.activityMonitor = new OperationMonitorPageSite(var1, this);
   }

   public void setRetryPolicyEditor(RetryPolicyEditor var1) {
      this.retryPolicyEditor = var1;
      var1.addUndoableEditListener(new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent var1) {
            AbstractSalesforceItemPage.this.undoableEditHappened(var1);
            AbstractSalesforceItemPage.this.setDirty();
         }
      });
   }

   @Override
   public final void setEntityPersistor(EntityPersistor var1) {
      Preconditions.checkNotNull(var1, "persistor");
      super.setEntityPersistor(var1);
      Receiver var2 = new Receiver<EntityPageSaveJob>() {
         public void handle(EntityPageSaveJob var1) {
            AbstractSalesforceItemPage.this.addSaveJob(var1);
         }
      };
      this.handleEntityPersistor(var1, var2);
   }

   protected void handleEntityPersistor(EntityPersistor var1, Receiver<EntityPageSaveJob> var2) {
   }

   protected AbstractSalesforceItemPanel<T> getDetailsPanel() {
      return (AbstractSalesforceItemPanel<T>)super.getDetailsPanel();
   }

   protected abstract AbstractSalesforceItemPanel<T> createDataPanel();

   protected final void doDefaultPanelConfiguration(AbstractSalesforceItemPanel<T> var1) {
      var1.setLinks(this.links);
      var1.setRetryPolicyEditor(this.retryPolicyEditor);
      if (this.soapHeaderEditor != null) {
         var1.setSoapHeaderEditor(this.soapHeaderEditor);
      }

      this.addDefaultActions(var1);
   }

   private void addDefaultActions(AbstractSalesforceItemPanel<T> var1) {
      if (this.runAction != null) {
         var1.setRunAction(this.runAction);
      }

      if (this.testAction != null) {
         var1.setTestAction(this.testAction);
      }

      if (this.deleteAction != null) {
         var1.setDeleteAction(this.deleteAction);
      }
   }

   public final RetryPolicy getRetryPolicy() {
      return this.retryPolicyEditor == null ? RetryPolicy.NONE : this.retryPolicyEditor.getPolicy();
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

   @Override
   protected final void doAdditionalSaveWorkBeforePageIsMarkedAsClean() throws InterchangeSavingException {
      this.saveConnector();
      this.saveSoapHeaders();
      this.updateImplementingOperation();
   }

   private void saveConnector() {
      SalesforceConnector var1 = this.getDetailsPanel().getSelectedConnector();
      this.getObject().setConnector(var1);
   }

   private void saveSoapHeaders() {
      if (this.soapHeaderEditor != null) {
         this.soapHeaderEditor.applyToItem();
      }
   }

   private void updateImplementingOperation() throws InterchangeSavingException {
      WizardItemBrowser var1 = new WizardItemBrowser(this.getObject());
      Operation var2 = var1.getImplementingOperation();
      if (var2 != null) {
         Schedule var3 = var1.getSchedule();
         Schedule var4 = this.getDetailsPanel().getSelectedSchedule();
         if (var3 != var4) {
            var2.setSchedule(var4);
            this.getEntityPersistor().save(var2);
         }
      }
   }

   public final SalesforceConnector getSalesforceConnector() {
      AbstractSalesforceItemPanel var1 = this.getDetailsPanel();
      return var1 == null ? null : var1.getSelectedConnector();
   }

   final Supplier<SalesforceConnectorId> getConnectorIdSupplier() {
      return new Supplier<SalesforceConnectorId>() {
         public SalesforceConnectorId get() {
            SalesforceConnector var1 = AbstractSalesforceItemPage.this.getSalesforceConnector();
            return var1 == null ? null : var1.getID();
         }
      };
   }

   public final Supplier<SalesforceConnector> getConnectorSupplier() {
      return new Supplier<SalesforceConnector>() {
         public SalesforceConnector get() {
            return AbstractSalesforceItemPage.this.getSalesforceConnector();
         }
      };
   }

   public final Supplier<OrgIdentifier> getOrgIdSupplier() {
      return new Supplier<OrgIdentifier>() {
         public OrgIdentifier get() {
            SalesforceConnector var1 = AbstractSalesforceItemPage.this.getSalesforceConnector();
            return var1 == null ? null : var1.getOrgIdentifier();
         }
      };
   }

   @Override
   protected boolean includeViewDependenciesButton() {
      return false;
   }
}
