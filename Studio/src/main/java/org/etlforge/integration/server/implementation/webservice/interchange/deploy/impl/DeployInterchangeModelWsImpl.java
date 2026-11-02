package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.List;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.IntegrationProjectDeployer;
import org.etlforge.integration.client.deploy.serverapi.JitterPackServerBackup;
import org.etlforge.integration.client.deploy.serverapi.UploadJitterPackCallback;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

public final class DeployInterchangeModelWsImpl extends CppWebServiceServerCall implements IntegrationProjectDeployer {
   private final ServerConnectionService connectionService;
   private Optional<Predicate<ServerInfo>> localHostDetector = Optional.absent();

   public DeployInterchangeModelWsImpl(CppWebServiceConfiguration var1, ServerConnectionService var2) {
      super(var1);
      this.connectionService = var2;
   }

   public void setLocalHostDetector(Predicate<ServerInfo> var1) {
      this.localHostDetector = Optional.of(var1);
   }

   @Override
   public void deployProject(DeployData var1, DeployCallback var2) {
      WebServiceConfiguration var3 = this.getConfiguration();
      DeployInterchangeModelWsImplWrapper var4 = new DeployInterchangeModelWsImplWrapper(var3, this.connectionService);
      var4.setLocalHostDetector(this.localHostDetector);
      var4.deployInterchangeModel(var1, var2);
   }

   @Override
   public void uploadJitterPack(JitterPackServerBackup var1, UploadJitterPackCallback var2) {
      WebServiceConfiguration var3 = this.getConfiguration();
      DeployInterchangeModelWsImplWrapper var4 = new DeployInterchangeModelWsImplWrapper(var3, this.connectionService);
      var4.uploadJitterpakBackup(var1, var2);
   }

   public void deleteProjects(List<IntegrationProjectId> var1, DeleteProjectsCallback var2) {
      WebServiceConfiguration var3 = this.getConfiguration();
      DeployInterchangeModelWsImplWrapper var4 = new DeployInterchangeModelWsImplWrapper(var3, this.connectionService);
      var4.deleteIntegrationProjects(var1, var2);
   }
}
