package com.etlforge.integration.client.ui.sfdc.delete.wizard;

import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSource;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.data.entity.SalesforceDelete;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class DeleteWizard extends SalesforceWizard<SalesforceDelete> {
   private final DeleteWorkflowBuilder workflowBuilder;

   public DeleteWizard(EntityWizardContext var1, SalesforceDelete var2, Folder var3, SalesforceFacade var4) {
      super(var1, var2, var3, SfdcMethod.DELETE, var4);
      this.workflowBuilder = new DeleteWorkflowBuilder(var1.getProject(), var2, var3);
      this.setWorkflowBuilder(this.workflowBuilder);
      this.suggestInitialName();
   }

   private void suggestInitialName() {
      this.setWizardItemName("New " + this.getCapitalizedMethodName());
      this.workflowBuilder.suggestName(this.getItemToConfigure().getName());
   }

   @Override
   protected void applyPages() {
      DeleteWizardPageChain var1 = (DeleteWizardPageChain)this.getPageChain();
      SalesforceDelete var2 = this.getItemToConfigure();
      var1.apply(var2);
   }

   public SoapWizardWorkflow<SalesforceDelete> buildWorkflow(Source var1, RequestSource var2, Options var3) {
      this.workflowBuilder.configureSource(var1, var2);
      SoapWizardWorkflow var4 = this.workflowBuilder.buildWorkflow(var3);
      this.setWorkflow(var4);
      return var4;
   }

   public Transformation getRequestTransformation() {
      return this.workflowBuilder.getRequest();
   }

   public Transformation getResponseTransformation() {
      return this.workflowBuilder.getResponse();
   }
}
