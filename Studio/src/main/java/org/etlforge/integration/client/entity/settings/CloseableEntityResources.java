package org.etlforge.integration.client.entity.settings;

import java.util.Collection;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface CloseableEntityResources extends EntityResources {
   void close(Collection<IntegrationEntity> var1);
}
