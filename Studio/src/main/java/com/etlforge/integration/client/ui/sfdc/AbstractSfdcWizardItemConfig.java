package com.etlforge.integration.client.ui.sfdc;

import com.google.common.collect.ImmutableList;
import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPageConfig;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorListener;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.EntityType;

public abstract class AbstractSfdcWizardItemConfig extends AbstractEntityConfig {
   private final FacadeFactory facadeFactory;
   private final EntityWizardFactory wizardFactory;
   private final ImmutableList<LinkType> pageLinkTypes;
   private boolean includeTestActionInPage = true;
   private OperationRunnerFactory opRunnerFactory;
   private OperationTesterFactory opTesterFactory;
   private OperationMonitorApis opMonitorApis;
   private OperationMonitorListener opMonitorListener;
   private RetryPolicyEditorFactory retryPolicyEditorFactory;
   private ApplicationAction deleteAction;

   protected AbstractSfdcWizardItemConfig(EntityType var1, FacadeFactory var2, EntityWizardFactory var3, List<LinkType> var4) {
      super(var1);
      this.facadeFactory = var2;
      this.wizardFactory = var3;
      this.pageLinkTypes = ImmutableList.copyOf(var4);
   }

   @Override
   protected boolean openEditorForNewItem() {
      return false;
   }

   protected final OperationRunnerFactory getOperationRunnerFactory() {
      return this.opRunnerFactory;
   }

   public final void setOperationRunnerFactory(OperationRunnerFactory var1) {
      this.opRunnerFactory = var1;
   }

   protected final OperationTesterFactory getOperationTesterFactory() {
      return this.opTesterFactory;
   }

   public void setOperationTesterFactory(OperationTesterFactory var1) {
      this.opTesterFactory = var1;
   }

   public final void setOperationMonitorApis(OperationMonitorApis var1) {
      this.opMonitorApis = var1;
   }

   public final void setOperationMonitorListener(OperationMonitorListener var1) {
      this.opMonitorListener = var1;
   }

   public final void setDeleteAction(ApplicationAction var1) {
      this.deleteAction = var1;
   }

   public void setRetryPolicyEditorFactory(RetryPolicyEditorFactory var1) {
      this.retryPolicyEditorFactory = var1;
   }

   public final void setIncludeTestActionInPage(boolean var1) {
      this.includeTestActionInPage = var1;
   }

   protected final List<LinkType> getPageLinkTypes() {
      return this.pageLinkTypes;
   }

   protected final FacadeFactory getFacadeFactory() {
      return this.facadeFactory;
   }

   protected final SalesforceFacade newSalesforceFacade() {
      return this.facadeFactory.newFacade();
   }

   @Override
   public final EntityWizardFactory getEntityWizardFactory() {
      return this.wizardFactory;
   }

   protected final void installDefaultCapabilitiesInPageConfig(AbstractSalesforceItemPageConfig<?> var1) {
      var1.setLinkTypes(this.pageLinkTypes);
      var1.setRunnerFactory(this.opRunnerFactory);
      var1.setTesterFactory(this.opTesterFactory);
      var1.setMonitorApis(this.opMonitorApis);
      var1.setMonitorListener(this.opMonitorListener);
      var1.setIncludeTestAction(this.includeTestActionInPage);
      var1.setRetryPolicyEditorFactory(this.retryPolicyEditorFactory);
      var1.setDeleteAction(this.deleteAction);
   }
}
