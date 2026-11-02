package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.operation.OperationPreferences;

public abstract class AbstractSfdcWizardFactory<T extends IntegrationEntity & SalesforceWizardEntity> extends EntityWizardFactory {
   private final FacadeFactory sfdcFacadeFactory;
   private boolean includeObjectSummary = true;
   private OperationPreferences operationPreferences = OperationPreferences.NONE;

   protected AbstractSfdcWizardFactory(FacadeFactory var1) {
      this.sfdcFacadeFactory = var1;
   }

   public final void setIncludeObjectSummary(boolean var1) {
      this.includeObjectSummary = var1;
   }

   protected final boolean getIncludeObjectSummary() {
      return this.includeObjectSummary;
   }

   public final void setOperationPreferences(OperationPreferences var1) {
      this.operationPreferences = Preconditions.checkNotNull(var1);
   }

   protected final SalesforceFacade createSfdcFacade() {
      return this.sfdcFacadeFactory.newFacade();
   }

   protected final void configureWizard(SalesforceWizard<T> var1, EntityWizardContext var2, T var3, Folder var4) {
      var1.setOperationPreferences(this.operationPreferences);
      this.createWizardDisplayer(var1, var2, var3.getEntityType());
      SalesforceWizardCompleter var5 = this.createCompleter(var1, var2, var4);
      var5.setWaitService(var2.getWindow());
      var1.addWizardListener(var5);
   }

   protected abstract SalesforceWizardCompleter<T> createCompleter(SalesforceWizard<T> var1, EntityWizardContext var2, Folder var3);

   @Override
   protected abstract String[] getSections();
}
