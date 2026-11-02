package com.etlforge.integration.client.ui.sfdc.action;

import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.ui.operation.runner.AbstractRunOperationAction;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;

public final class RunSfdcWizardAction<T extends IntegrationEntity> extends AbstractRunOperationAction {
   private final Class<T> wizardClass;
   private SalesforceWizardEntity wizard;

   public RunSfdcWizardAction(String var1, String var2, ApplicationWindow var3, Class<T> var4, OperationRunnerFactory var5) {
      super(var1, var2, var3, var5);
      this.wizardClass = var4;
   }

   @Override
   public boolean accepts(Object var1) {
      return this.wizardClass != null && this.wizardClass.isInstance(var1);
   }

   @Override
   protected Operation getOperationToRun() {
      List var1 = this.getActiveObjects(this.wizardClass);
      if (var1.size() == 1) {
         SalesforceWizardEntity var2 = (SalesforceWizardEntity)var1.get(0);
         WizardItemBrowser var3 = new WizardItemBrowser(var2);
         Operation var4 = var3.getImplementingOperation();
         if (var4 != null) {
            this.wizard = var2;
            return var4;
         }
      }

      this.wizard = null;
      return null;
   }

   @Override
   protected Editor getPageForObjectToRun() {
      return this.getEditorFor((IntegrationEntity)this.wizard);
   }
}
