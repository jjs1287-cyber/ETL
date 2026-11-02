package org.etlforge.integration.client.deploy.history;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.project.IntegrationProject;

public final class FilteredDeployHistory implements DeployHistory {
   private final DeployHistory decoratee;
   private final DeployResultFilter filter;
   private final DeployHistoryListeners listeners;

   public FilteredDeployHistory(DeployHistory var1, DeployResultFilter var2) {
      this.decoratee = Preconditions.checkNotNull(var1, "decoratee");
      this.filter = Preconditions.checkNotNull(var2, "filter");
      this.listeners = new DeployHistoryListeners();
   }

   @Override
   public IntegrationProject getProject() {
      return this.decoratee.getProject();
   }

   @Override
   public void addResult(DeployResult var1) {
      this.decoratee.removeResult(var1);
      if (this.filter.apply(var1)) {
         this.listeners.call().deployResultAdded(var1);
      }
   }

   @Override
   public void removeResult(DeployResult var1) {
      this.decoratee.removeResult(var1);
      if (this.filter.apply(var1)) {
         this.listeners.call().deployResultRemoved(var1);
      }
   }

   @Override
   public List<DeployResult> getAllResults() {
      return KongaListUtils.filter(this.decoratee.getAllResults(), this.filter);
   }

   @Override
   public void removeAll() {
      for (DeployResult var2 : this.getAllResults()) {
         this.removeResult(var2);
      }

      this.listeners.call().historyCleared();
   }

   @Override
   public void addListener(DeployHistoryListener var1) {
      this.listeners.add(var1);
   }

   @Override
   public void removeListener(DeployHistoryListener var1) {
      this.listeners.remove(var1);
   }
}
