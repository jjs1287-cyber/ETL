package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.client.ui.script.debug.ScriptDebugModel;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.TestResult;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;

public interface ScriptTestUi extends UiProvider, Disposable {
   ScriptDebugModel getModel();

   void startTest();

   void displayResult(TestResult var1);

   void setItemLookup(IntegrationEntityLookup var1);

   boolean isTestEnabled();
}
