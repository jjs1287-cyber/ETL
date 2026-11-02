package org.etlforge.integration.client.project.deploy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.IntegrationProjectDeployer;
import org.etlforge.integration.client.server.IntegrationServer;

public final class DeployManager {
   private final ImmutableList<DeployCallback> callbacks;
   private final Object preDeployLock = new Object();
   private ImmutableList<PreDeployJobFactory> preDeployJobFactories = ImmutableList.of();

   public DeployManager() {
      this(new ArrayList<>());
   }

   public DeployManager(List<DeployCallback> var1) {
      this.callbacks = ImmutableList.copyOf(var1);
   }

   public void setPreDeployJobFactories(List<PreDeployJobFactory> var1) {
      synchronized (this.preDeployLock) {
         this.preDeployJobFactories = ImmutableList.copyOf(var1);
      }
   }

   public void deploy(DeployData var1, DeployCallback var2) {
      Preconditions.checkNotNull(var1, "data");
      Preconditions.checkNotNull(var2, "callback");
      DeployLog.LOG.info("Running pre-deploy jobs");
      PreDeployStatus var3 = this.runPreDeployJobs(var1);
      if (var3 == PreDeployStatus.CONTINUE) {
         this.sendDataToServer(var1, var2);
      } else {
         var2.cancelled();
      }
   }

   private PreDeployStatus runPreDeployJobs(DeployData var1) {
      ImmutableList var2;
      synchronized (this.preDeployLock) {
         var2 = ImmutableList.copyOf(this.preDeployJobFactories);
      }

      for (Object __etlItem51 : var2) {
         PreDeployJobFactory var4 = (PreDeployJobFactory) __etlItem51;
         PreDeployJob var5 = var4.getJob(var1);
         if (var5 != null) {
            DeployLog.LOG.info("Running PreDeployJob: " + var5.getClass());
            PreDeployStatus var6 = var5.run(var1);
            if (var6 == PreDeployStatus.ABORT) {
               return var6;
            }
         }
      }

      return PreDeployStatus.CONTINUE;
   }

   private void sendDataToServer(DeployData var1, DeployCallback var2) {
      IntegrationServer var3 = IntegrationServer.getInstance();
      IntegrationProjectDeployer var4 = var3.getServerCall(IntegrationProjectDeployer.class);
      DeployLog.LOG.info("Sending data to the InterchangeProjectDeployer");
      ArrayList var5 = Lists.newArrayList(this.callbacks);
      var5.add(var2);
      var4.deployProject(var1, new DeployManager.CallbackDispatcher(var5));
   }

   private static final class CallbackDispatcher implements DeployCallback {
      private final List<DeployCallback> callbacks;

      public CallbackDispatcher(List<DeployCallback> var1) {
         this.callbacks = var1;
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         for (DeployCallback var4 : this.callbacks) {
            var4.deployComplete(var1, var2);
         }
      }

      @Override
      public void cancelled() {
         for (DeployCallback var2 : this.callbacks) {
            var2.cancelled();
         }
      }

      @Override
      public void caught(Throwable var1) {
         for (DeployCallback var3 : this.callbacks) {
            var3.caught(var1);
         }
      }
   }
}
