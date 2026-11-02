package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.etlforge.integration.client.sfdc.data.ProjectSalesforceWsdlParser;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.NameChangeConsequence;
import com.etlforge.integration.client.ui.sfdc.WizardItemOperationTester;
import com.etlforge.integration.client.ui.sfdc.page.LinkPanelInstaller;
import com.etlforge.integration.client.ui.sfdc.page.v4.MonitorInstallerV4;
import com.etlforge.integration.client.ui.sfdc.query.SoqlCompletionContext;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QueryValidator;
import com.etlforge.integration.client.ui.sfdc.wsdl.SalesforceWsdlInfoCacheFactory;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import java.util.Arrays;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.ui.text.contentassist.CompletionContext;

public class SalesforceQueryPageConfigV4 extends DefaultPageConfig {
   private final SfdcMethodConfig queryConfig;
   private final OperationRunnerFactory opRunnerFactory;
   private final OperationTesterFactory opTesterFactory;
   private SalesforceQueryPageV4 page;

   public SalesforceQueryPageConfigV4(
      ApplicationWindow var1, ProjectPersistor var2, SfdcMethodConfig var3, OperationRunnerFactory var4, OperationTesterFactory var5
   ) {
      super(var1, var2);
      this.queryConfig = var3;
      this.opRunnerFactory = var4;
      this.opTesterFactory = var5;
   }

   @Override
   protected void postConfiguration(IntegrationEntityPage var1) {
      this.page = (SalesforceQueryPageV4)var1;
      this.installSaveConsequences();
      this.installLinks();
      this.installActions();
      this.installQueryValidator();
      MonitorInstallerV4.installMonitor(this.page);
   }

   private void installSaveConsequences() {
      ProjectPersistor var1 = this.getPersistor();
      this.page.addSaveConsequence(new RequestMappingsConsequenceV4(this.page, this.queryConfig, var1));
      this.page.addSaveConsequence(new ResponseNodeFilterConsequenceV4(this.page));
      this.page.addSaveConsequence(new NameChangeConsequence(this.page, var1));
   }

   private void installLinks() {
      LinkPanelInstaller var1 = new LinkPanelInstaller(this.getPersistor(), this.getProjectExplorer());
      var1.installLinksV4(this.page, Arrays.asList(LinkType.RESPONSE, LinkType.OPERATION));
   }

   private void installActions() {
      if (this.opRunnerFactory != null) {
         this.page.setOperationRunner(this.opRunnerFactory.newRunner());
      }

      this.page.setOperationTester(new WizardItemOperationTester(this.page, this.opTesterFactory));
   }

   private void installQueryValidator() {
      SalesforceQueryPageConfigV4.ValidatorFactory var1 = new SalesforceQueryPageConfigV4.ValidatorFactory(this.page);
      var1.execute();
   }

   private static class ValidatorFactory extends KongaSwingWorker<Pair<? extends QueryValidator, ? extends CompletionContext>, Void> {
      private final SalesforceQueryPageV4 page;

      public ValidatorFactory(SalesforceQueryPageV4 var1) {
         this.page = var1;
      }

      protected Pair<? extends QueryValidator, ? extends CompletionContext> doInBackground() throws Exception {
         this.setLogLevel(ErrorLevel.LOG);
         SalesforceQuery var1 = this.page.getObject();
         ProjectSalesforceWsdlParser var2 = this.createWsdlParser();
         SalesforceWsdlInfo var3 = var2.parseWsdl(var1);
         WsdlBasedQueryValidator var4 = new WsdlBasedQueryValidator(var3);
         var4.setFixedObjectName(var1.getObject());
         SoqlCompletionContext var5 = new SoqlCompletionContext(SalesforceConfig.getInstance());
         var5.setObject(var3.lookupObject(var1.getObject()));
         return Pair.of(var4, var5);
      }

      private ProjectSalesforceWsdlParser createWsdlParser() {
         ProjectSalesforceWsdlParser var1 = new ProjectSalesforceWsdlParser(SalesforceConfig.getInstance());
         var1.setWsdlCache(SalesforceWsdlInfoCacheFactory.getCache());
         return var1;
      }

      protected void onSuccess(Pair<? extends QueryValidator, ? extends CompletionContext> var1) {
         this.page.setQueryValidator((QueryValidator)var1.first);
         this.page.setCompletionContext((CompletionContext)var1.second);
      }

      @Override
      protected void onFailure(Throwable var1) {
      }
   }
}
