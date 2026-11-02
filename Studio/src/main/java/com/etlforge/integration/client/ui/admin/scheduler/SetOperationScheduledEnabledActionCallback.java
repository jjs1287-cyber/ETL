package com.etlforge.integration.client.ui.admin.scheduler;

import java.util.Collection;
import org.etlforge.integration.data.entity.id.OperationId;

public interface SetOperationScheduledEnabledActionCallback {
   void setTalkingToServer(boolean var1);

   void operationsUpdated(Collection<OperationId> var1, boolean var2);
}
