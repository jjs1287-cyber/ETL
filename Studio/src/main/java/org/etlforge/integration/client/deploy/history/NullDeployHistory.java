package org.etlforge.integration.client.deploy.history;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.project.IntegrationProject;

public final class NullDeployHistory implements DeployHistory {
   @Override
   public IntegrationProject getProject() {
      return null;
   }

   @Override
   public void addResult(DeployResult var1) {
   }

   @Override
   public List<DeployResult> getAllResults() {
      return Lists.newArrayList();
   }

   @Override
   public void removeAll() {
   }

   @Override
   public void removeListener(DeployHistoryListener var1) {
   }

   @Override
   public void addListener(DeployHistoryListener var1) {
   }

   @Override
   public void removeResult(DeployResult var1) {
   }
}
