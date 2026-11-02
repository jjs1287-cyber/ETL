package com.etlforge.integration.client.ui.sfdc.ucu.page.v4;

import com.etlforge.integration.client.sfdc.data.ProjectSalesforceWsdlParser;
import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.NameChangeConsequence;
import com.etlforge.integration.client.ui.sfdc.WizardItemOperationTester;
import com.etlforge.integration.client.ui.sfdc.page.LinkPanelInstaller;
import com.etlforge.integration.client.ui.sfdc.page.v4.MonitorInstallerV4;
import com.etlforge.integration.client.ui.sfdc.wsdl.SalesforceWsdlInfoCacheFactory;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import java.util.Arrays;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.AbstractSalesforceUCU;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;

public final class SalesforceUcuPageConfigV4 extends DefaultPageConfig {
   private final OperationRunnerFactory opRunnerFactory;
   private final OperationTesterFactory opTesterFactory;
   private SalesforceUcuPageV4 page;

   public SalesforceUcuPageConfigV4(ApplicationWindow var1, ProjectPersistor var2, OperationRunnerFactory var3, OperationTesterFactory var4) {
      super(var1, var2);
      this.opRunnerFactory = var3;
      this.opTesterFactory = var4;
   }

   @Override
   protected void postConfiguration(IntegrationEntityPage var1) {
      this.page = (SalesforceUcuPageV4)var1;
      this.installSaveConsequences();
      this.installLinks();
      this.installActions();
      this.installWsdlInfo();
      MonitorInstallerV4.installMonitor(this.page);
   }

   private void installSaveConsequences() {
      ProjectPersistor var1 = this.getPersistor();
      this.page.addSaveConsequence(new NameChangeConsequence(this.page, var1));
      AbstractSalesforceUCU var2 = this.page.getObject();
      SfdcMethod var3 = var2.getMethod();
      SfdcMethodConfig var4 = SalesforceConfig.getInstance().getMethodConfig(var3);
      this.page.addSaveConsequence(new RequestMappingsConsequenceV4(this.page, var4, var1));
   }

   private void installLinks() {
      LinkPanelInstaller var1 = new LinkPanelInstaller(this.getPersistor(), this.getProjectExplorer());
      var1.installLinksV4(this.page, Arrays.asList(LinkType.REQUEST, LinkType.OPERATION));
   }

   private void installActions() {
      if (this.opRunnerFactory != null) {
         this.page.setOperationRunner(this.opRunnerFactory.newRunner());
      }

      this.page.setOperationTester(new WizardItemOperationTester(this.page, this.opTesterFactory));
   }

   private void installWsdlInfo() {
      SalesforceUcuPageConfigV4.WsdlInfoExtractor var1 = new SalesforceUcuPageConfigV4.WsdlInfoExtractor();
      var1.execute();
   }

   private class WsdlInfoExtractor extends KongaSwingWorker<SalesforceWsdlInfo, Void> {
      private WsdlInfoExtractor() {
      }

      protected SalesforceWsdlInfo doInBackground() throws Exception {
         ProjectSalesforceWsdlParser var1 = this.createWsdlParser();
         SalesforceWizardEntity var2 = SalesforceUcuPageConfigV4.this.page.getObject();
         return var1.parseWsdl(var2);
      }

      private ProjectSalesforceWsdlParser createWsdlParser() {
         ProjectSalesforceWsdlParser var1 = new ProjectSalesforceWsdlParser(SalesforceConfig.getInstance());
         var1.setWsdlCache(SalesforceWsdlInfoCacheFactory.getCache());
         return var1;
      }

      protected void onSuccess(SalesforceWsdlInfo var1) {
         SalesforceUcuPageConfigV4.this.page.setWsdlInfo(var1);
      }
   }
}
