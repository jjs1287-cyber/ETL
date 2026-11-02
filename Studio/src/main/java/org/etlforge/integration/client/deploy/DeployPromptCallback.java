package org.etlforge.integration.client.deploy;

import java.util.List;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DeployPromptCallback extends DeployCallback {
   @Override
   void deployComplete(DeployData var1, DeployResult var2);

   void deployGreenLighted(List<? extends IntegrationEntity> var1);

   @Override
   void caught(Throwable var1);

   @Override
   void cancelled();

   void deployDeclined();
}
