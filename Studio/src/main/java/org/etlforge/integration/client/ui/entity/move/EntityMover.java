package org.etlforge.integration.client.ui.entity.move;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityMover {
   void setItemsToMove(List<? extends IntegrationEntity> var1);

   List<IntegrationEntity> getItemsToMove();

   boolean isAllowedTarget(IntegrationEntity var1);

   List<IntegrationEntity> executeMove(IntegrationEntity var1);
}
