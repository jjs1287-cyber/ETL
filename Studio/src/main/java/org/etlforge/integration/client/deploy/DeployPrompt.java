package org.etlforge.integration.client.deploy;

import java.util.Collection;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface DeployPrompt {
   boolean prompt(Collection<? extends IntegrationEntity> var1, Collection<? extends IntegrationEntity> var2);

   Collection<? extends IntegrationEntity> getDirtyItemsToDeploy();
}
