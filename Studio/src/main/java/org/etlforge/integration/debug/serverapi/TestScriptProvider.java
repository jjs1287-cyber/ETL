package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.debug.client.DebugSession;

public interface TestScriptProvider extends ServerTestCall {
   String ID = TestScriptProvider.class.getName();

   void testExpression(DebugSession var1, String var2, ServerTestCallback var3);
}
