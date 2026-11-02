package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.DebugSession;

public interface TestOperationProvider extends ServerTestCall {
   String ID = TestOperationProvider.class.getName();

   void testOperation(DebugSession var1, OperationId var2, TransformationId var3, ServerTestCallback var4);
}
