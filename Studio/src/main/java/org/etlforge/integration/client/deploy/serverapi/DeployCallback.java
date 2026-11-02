package org.etlforge.integration.client.deploy.serverapi;

import org.etlforge.integration.client.server.ServerCallback;

public interface DeployCallback extends ServerCallback {
   void deployComplete(DeployData var1, DeployResult var2);

   abstract class Adapter implements DeployCallback {
      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
      }
   }
}
