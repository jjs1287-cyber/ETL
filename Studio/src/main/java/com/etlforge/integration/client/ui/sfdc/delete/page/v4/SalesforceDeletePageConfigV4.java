package com.etlforge.integration.client.ui.sfdc.delete.page.v4;

import com.etlforge.integration.client.ui.sfdc.LinkType;
import com.etlforge.integration.client.ui.sfdc.NameChangeConsequence;
import com.etlforge.integration.client.ui.sfdc.WizardItemOperationTester;
import com.etlforge.integration.client.ui.sfdc.page.LinkPanelInstaller;
import com.etlforge.integration.client.ui.sfdc.page.v4.MonitorInstallerV4;
import java.util.Arrays;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SalesforceDeletePageConfigV4 extends DefaultPageConfig {
   private SalesforceDeletePageV4 page;
   private final OperationRunnerFactory opRunnerFactory;
   private final OperationTesterFactory opTesterFactory;

   public SalesforceDeletePageConfigV4(ApplicationWindow var1, ProjectPersistor var2, OperationRunnerFactory var3, OperationTesterFactory var4) {
      super(var1, var2);
      this.opRunnerFactory = var3;
      this.opTesterFactory = var4;
   }

   @Override
   protected void postConfiguration(IntegrationEntityPage var1) {
      this.page = (SalesforceDeletePageV4)var1;
      this.installSaveConsequences();
      this.installLinks();
      this.installActions();
      MonitorInstallerV4.installMonitor(this.page);
   }

   private void installSaveConsequences() {
      this.page.addSaveConsequence(new NameChangeConsequence(this.page, this.getPersistor()));
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
}
