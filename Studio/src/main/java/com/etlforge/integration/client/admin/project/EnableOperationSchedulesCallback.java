package com.etlforge.integration.client.admin.project;

import java.util.Collection;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.data.entity.id.OperationId;

public interface EnableOperationSchedulesCallback extends ServerCallback {
   void done(Collection<OperationId> var1, boolean var2);
}
