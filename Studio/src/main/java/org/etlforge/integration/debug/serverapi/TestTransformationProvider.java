package org.etlforge.integration.debug.serverapi;

import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.integration.debug.client.file.SourceDataIdentifier;

public interface TestTransformationProvider extends ServerTestCall {
   String ID = TestTransformationProvider.class.getName();

   void testTransformation(DebugSession var1, SourceDataIdentifier var2, String var3, ServerTestCallback var4);
}
