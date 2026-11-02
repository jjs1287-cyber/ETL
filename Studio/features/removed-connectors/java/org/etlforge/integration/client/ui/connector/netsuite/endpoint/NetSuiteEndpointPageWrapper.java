package org.etlforge.integration.client.ui.connector.netsuite.endpoint;

import com.google.common.collect.ImmutableList;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.connector.netsuite.endpoint.page.NetSuiteEndpointPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.design.netsuite.NetSuiteWsdlDownloadUrl;

public class NetSuiteEndpointPageWrapper {
   private final NetSuiteEndpointPage endpointPage;

   NetSuiteEndpointPageWrapper(IntegrationEntityPage var1) {
      this.endpointPage = (NetSuiteEndpointPage)var1;
   }

   public ApplicationWindow getWindow() {
      return this.endpointPage.getWindow();
   }

   boolean isWsdlVersionChange() {
      NetSuiteEndpoint var1 = this.getNetSuiteEndpoint();
      NetSuiteWsdlDownloadUrl var2 = var1.getNetSuiteWsdlDownloadUrl();
      String var3 = var1.getAccount();
      String var4 = this.getNewWsdlDownloadUrl();
      String var5 = this.getNewAccount();
      boolean var6 = var2.isSameVersion(var4);
      return !var6;
   }

   private NetSuiteEndpoint getNetSuiteEndpoint() {
      return this.endpointPage.getObject();
   }

   private String getNewAccount() {
      return this.endpointPage.getNewAccount();
   }

   private String getNewWsdlDownloadUrl() {
      return this.endpointPage.getNewWsdlDownloadUrl();
   }

   public ImmutableList<NetSuiteFunction> collectFunctions() {
      NetSuiteEndpoint var1 = this.getNetSuiteEndpoint();
      return collectFunctions(var1);
   }

   private static ImmutableList<NetSuiteFunction> collectFunctions(NetSuiteEndpoint var0) {
      IntegrationProject var1 = ProjectUtils.getProject(var0);
      if (var1 == null) {
         return ImmutableList.of();
      }

      DependencyStore var2 = var1.getCurrentDependencies();

      try {
         return var2.getDependingObjects(var0).filter(NetSuiteFunction.class).toImmutableList();
      } finally {
         var2.dispose();
      }
   }
}
