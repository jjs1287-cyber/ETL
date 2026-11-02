package org.etlforge.integration.client.ui.script.builder;

import java.util.concurrent.Future;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.debug.serverapi.ServerTestCallback;

public interface FunctionTester {
   Future<?> testFunction(String var1, ScriptDataElementsModel var2, ServerTestCallback var3);

   ScriptDataElementsModel getDataElements();
}
