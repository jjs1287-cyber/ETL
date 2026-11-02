package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTesterFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.CreateJobParams;
import org.etlforge.integration.salesforce.bulkload.FileSplitterParams;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.query.QueryRunner;

public final class BulkLoadWizard extends SalesforceWizard<SalesforceBulkLoad> {
   private final BulkApiWorkflowBuilder workflowBuilder;
   private volatile SupportedQueryPolicy supportedQueryPolicy = SupportedQueryPolicy.NO_RESTRICTIONS;
   private final WizardSections wizardSections;

   public BulkLoadWizard(EntityWizardContext var1, SalesforceBulkLoad var2, Folder var3, SalesforceFacade var4, WizardSections var5) {
      super(var1, var2, var3, SfdcMethod.BULK_LOAD, var4);
      this.wizardSections = var5;
      this.workflowBuilder = new BulkApiWorkflowBuilder(var1.getProject(), var2, var3);
      this.setWorkflowBuilder(this.workflowBuilder);
   }

   void updateSectionsForAction(BulkAction var1) {
      String[] var2 = this.wizardSections.forAction(var1);
      this.getDisplayer().setSections(var2);
   }

   @Override
   protected void postStarted() {
      this.setWizardItemName("New Bulk Load");
      this.getDisplayer().setSection("Action");
   }

   @Override
   protected void applyPages() {
      BulkWizardPageChain var1 = (BulkWizardPageChain)this.getPageChain();
      var1.apply();
   }

   public void setCreateJobParams(CreateJobParams var1) {
      this.workflowBuilder.setCreateJobParams(var1);
   }

   public void configureLoad(Source var1, FileSplitterParams var2, LoadOptions var3) {
      this.workflowBuilder.configureLoad(var1, var2, var3);
   }

   public void configureQuery(String var1, Target var2, QueryOptions var3) {
      this.workflowBuilder.configureQuery(var1, var2, var3);
   }

   public BulkApiWorkflow buildWorkflow() {
      BulkApiWorkflow var1 = this.workflowBuilder.buildWorkflow();
      this.setWorkflow(var1);
      return var1;
   }

   public SupportedQueryPolicy getSupportedQueryPolicy() {
      return this.supportedQueryPolicy;
   }

   public void setSupportedQueryPolicy(SupportedQueryPolicy var1) {
      this.supportedQueryPolicy = var1;
   }

   public QueryTester createQueryTester() {
      QueryTesterFactory var1 = new QueryTesterFactory(this.getWindow().getFrame());
      var1.setRecordLimit(10);
      QueryRunner var2 = this.getSalesforceFacade().createQueryRunner();
      return var1.newTester(var2, this.supportedQueryPolicy);
   }
}
