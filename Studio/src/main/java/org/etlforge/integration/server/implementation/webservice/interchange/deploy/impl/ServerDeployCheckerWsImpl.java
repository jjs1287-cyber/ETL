package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.deploy.api.ServerDeployCheckCallback;
import com.etlforge.integration.client.deploy.api.ServerDeployChecker;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CDeployProjectCheckResult;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CIntegrationEntity;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CLastUpdate;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CProjectMembership;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.DeployInterchangeData;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.DeployInterchangeDataServiceLocator;

public final class ServerDeployCheckerWsImpl extends CppWebServiceServerCall implements ServerDeployChecker {
   private static final String SERVICE_NAME = "konga_deploy_interchange_data";

   public ServerDeployCheckerWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void checkProjectForDeploy(IntegrationProject var1, ServerDeployCheckCallback var2) {
      List var3 = var1.getAllEntities();
      ArrayList var4 = Lists.newArrayList();

      for (Object __etlItem36 : var3) {
         IntegrationEntity var6 = (IntegrationEntity) __etlItem36;
         EntityType var7 = var6.getEntityType();
         int var8 = var7.getId();
         if (var7 != EntityType.None && var7 != EntityType.Folder && var7 != EntityType.RootFolder && var8 >= 1 && var6.isDeployDirty()) {
            CIntegrationEntity var9 = new CIntegrationEntity(var7.getServerId(), var6.getID().toString());
            var4.add(var9);
         }
      }

      try {
         DeployInterchangeData var12 = this.getDeployer(var2);
         if (var12 == null) {
            return;
         }

         CDeployProjectCheckResult var13 = var12.checkProjectForDeploy(
            this.getConfiguration().getUserName(),
            this.getConfiguration().getPasswordHash(),
            var1.getID().toString(),
            (CIntegrationEntity[]) var4.toArray(new CIntegrationEntity[var4.size()])
         );
         CLastUpdate[] var14 = var13 == null ? null : var13.getLastUpdated();
         CProjectMembership[] var15 = var13 == null ? null : var13.getProjectMembershipInfo();
         DeployCheckResultImpl var16 = new DeployCheckResultImpl(var14, var15);
         var2.deployCheckComplete(var16);
      } catch (IntegrationServerException var10) {
         var2.caught(var10);
      } catch (RemoteException var11) {
         var2.caught(this.convert(var11));
      }
   }

   private DeployInterchangeData getDeployer(ServerCallback var1) throws IntegrationServerException {
      try {
         DeployInterchangeDataServiceLocator var2 = new DeployInterchangeDataServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_deploy_interchange_data", var3, var1)) {
            return null;
         }

         var2.setkonga_deploy_interchange_dataEndpointAddress(var3.toString());
         return var2.getkonga_deploy_interchange_data();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call webservice: " + var4.getMessage(), var4);
      }
   }
}
