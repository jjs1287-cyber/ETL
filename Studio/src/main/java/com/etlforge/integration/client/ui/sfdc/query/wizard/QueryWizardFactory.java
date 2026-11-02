package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.facade.FacadeFactory;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.QueryBuilderModelFactory;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTester;
import com.etlforge.integration.client.ui.sfdc.soql.test.QueryTesterFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.AbstractSfdcWizardFactory;
import com.etlforge.integration.client.ui.sfdc.wizard.MappingStructureWriter;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizardCompleter;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.QueryBuilderModel;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.integration.client.connect.mapper.AutoMappingJob;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardContext;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.query.QueryRunner;
import org.etlforge.integration.salesforce.wsdl.partner.crom.QueryRequestCromBuilder;
import org.etlforge.integration.salesforce.wsdl.partner.crom.QueryResponseCromBuilder;

public final class QueryWizardFactory extends AbstractSfdcWizardFactory<SalesforceQuery> {
   private final QueryTargetConfig targetConfig;
   private final SupportedQueryPolicy supportedQueryPolicy;

   public QueryWizardFactory(FacadeFactory var1, QueryTargetConfig var2, SupportedQueryPolicy var3) {
      super(var1);
      this.targetConfig = var2;
      this.supportedQueryPolicy = var3;
   }

   @Override
   protected String[] getSections() {
      return QueryWizardSections.all(this.getIncludeObjectSummary());
   }

   @Override
   public Wizard<?> createWizard(EntityWizardContext var1, IntegrationEntity var2, Folder var3) {
      SalesforceQuery var4 = (SalesforceQuery)var2;
      SalesforceFacade var5 = this.createSfdcFacade();
      QueryBuilderModel var6 = QueryBuilderModelFactory.newModel();
      QueryTester var7 = this.createQueryTester(var1.getWindow().getFrame(), var5);
      QueryWizard var8 = new QueryWizard(var1, var4, var3, var5, var6);
      QueryWizardPageChain var9 = new QueryWizardPageChain(
         var8, var1.getWindow(), var6, this.supportedQueryPolicy, var7, this.targetConfig, this.getIncludeObjectSummary()
      );
      var8.setPageChain(var9);
      this.configureWizard(var8, var1, var4, var3);
      return var8;
   }

   private QueryTester createQueryTester(JFrame var1, final SalesforceFacade var2) {
      QueryTesterFactory var3 = new QueryTesterFactory(var1);
      var3.setRecordLimit(10);
      Supplier var4 = new Supplier<QueryRunner>() {
         private QueryRunner runner;

         public QueryRunner get() {
            if (this.runner == null) {
               this.runner = var2.createQueryRunner();
            }

            return this.runner;
         }
      };
      return var3.newTester(var4, this.supportedQueryPolicy);
   }

   @Override
   protected SalesforceWizardCompleter<SalesforceQuery> createCompleter(SalesforceWizard<SalesforceQuery> var1, EntityWizardContext var2, Folder var3) {
      return new QueryWizardFactory.Completer((QueryWizard)var1, var2.getProject(), var2.getProjectPersistor(), var3);
   }

   private static class Completer extends SalesforceWizardCompleter<SalesforceQuery> {
      private boolean inPageAutoMappingNeeded = true;

      public Completer(QueryWizard var1, IntegrationProject var2, ProjectPersistor var3, IntegrationEntity var4) {
         super(var1, var2, var3, var4);
      }

      @Override
      protected void doPreSaveWork() {
         this.createMappingTrees();
         this.performResponseAutoMappingIfPossible();
      }

      private void createMappingTrees() {
         QueryWizard var1 = (QueryWizard)this.getWizard();
         QueryResponseCromBuilder var2 = this.createResponseBuilder(var1);
         MappingStructureWriter var3 = new MappingStructureWriter(
            var1.getRequestTransformation(), var1.getResponseTransformation(), new QueryRequestCromBuilder(), var2, this.getProject()
         );
         var3.writeJtrs();
      }

      private QueryResponseCromBuilder createResponseBuilder(QueryWizard var1) {
         QueryResponseCromBuilder var2 = new QueryResponseCromBuilder(var1.getSalesforceObject());
         var2.includeRelationships(var1.getSchemaProvider());
         return var2;
      }

      private void performResponseAutoMappingIfPossible() {
         QueryWizard var1 = (QueryWizard)this.getWizard();
         if (var1.isAutoMap()) {
            QueryWorkflowBuilder var2 = var1.getWorkflowBuilder();
            boolean var3 = var2.autoMapResponseIfPossible();
            this.inPageAutoMappingNeeded = !var3;
         }
      }

      @Override
      protected Transformation getMappingTransformation() {
         QueryWizard var1 = (QueryWizard)this.getWizard();
         SalesforceQuery var2 = var1.getItemToConfigure();
         return var2.isAutoResponse() ? null : var1.getResponseTransformation();
      }

      @Override
      protected AutoMappingJob createAutoMappingJob(TransformationPage var1) {
         return this.inPageAutoMappingNeeded ? new ResponseAutoMapper(var1) : new AutoMappingJob() {
            @Override
            public void run() {
            }

            @Override
            public void setCompletionLatch(CountDownLatch var1) {
               var1.countDown();
            }
         };
      }
   }
}
