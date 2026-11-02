package org.etlforge.integration.client.deploy.history;

import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.util.event.AbstractListenerManager;
import org.etlforge.util.event.ListenerManager;

public class DeployHistoryListeners extends AbstractListenerManager<DeployHistoryListener> implements ListenerManager<DeployHistoryListener> {
   protected DeployHistoryListener createNotifier() {
      return new DeployHistoryListener() {
         @Override
         public void deployResultAdded(DeployResult var1) {
            for (DeployHistoryListener var3 : DeployHistoryListeners.this.getListeners()) {
               var3.deployResultAdded(var1);
            }
         }

         @Override
         public void deployResultRemoved(DeployResult var1) {
            for (DeployHistoryListener var3 : DeployHistoryListeners.this.getListeners()) {
               var3.deployResultRemoved(var1);
            }
         }

         @Override
         public void historyCleared() {
            for (DeployHistoryListener var2 : DeployHistoryListeners.this.getListeners()) {
               var2.historyCleared();
            }
         }
      };
   }
}
