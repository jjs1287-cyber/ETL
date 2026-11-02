package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.etlforge.integration.client.sfdc.data.SoapWizardWorkflow;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.wizard.RequestSource;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import java.util.List;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class UcuSalesforceWizard<T extends AbstractSalesforceUCU> extends SalesforceWizard<T> {
   private final UcuWorkflowBuilder<T> workflowBuilder;
   private final ObjectSelectionModel objectSelectionModel;

   public UcuSalesforceWizard(EntityWizardContext var1, T var2, Folder var3, SalesforceFacade var4, ObjectSelectionModel var5) {
      super(var1, (T)var2, var3, var2.getMethod(), var4);
      this.objectSelectionModel = var5;
      this.workflowBuilder = new UcuWorkflowBuilder<>(var1.getProject(), (T)var2, var3);
      this.setWorkflowBuilder(this.workflowBuilder);
   }

   @Override
   protected void applyPages() {
      UcuWizardPageChain var1 = (UcuWizardPageChain)this.getPageChain();
      var1.apply();
   }

   public Transformation getRequestTransformation() {
      return this.workflowBuilder.getRequest();
   }

   public Transformation getResponseTransformation() {
      return this.workflowBuilder.getResponse();
   }

   @Override
   protected void postStarted() {
      this.getDisplayer().setSection("Login");
   }

   public SoapWizardWorkflow<T> buildWorkflow(Source var1, RequestSource var2, Options var3) {
      if (this.getSfdcMethod() == SfdcMethod.UPSERT) {
         String var4 = this.objectSelectionModel.getExternalIdFieldName();
         this.workflowBuilder.setExternalIdFieldName(var4);
      }

      this.workflowBuilder.configureSource(var1, var2);
      List var6 = this.objectSelectionModel.getRelatedObjects();
      this.workflowBuilder.setRelatedObjects(var6);
      SoapWizardWorkflow var5 = this.workflowBuilder.buildWorkflow(var3);
      this.setWorkflow(var5);
      return var5;
   }
}
