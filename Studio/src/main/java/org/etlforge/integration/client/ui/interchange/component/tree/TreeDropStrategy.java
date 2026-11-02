package org.etlforge.integration.client.ui.interchange.component.tree;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface TreeDropStrategy {
   boolean canDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2);

   boolean handleDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2);
}
