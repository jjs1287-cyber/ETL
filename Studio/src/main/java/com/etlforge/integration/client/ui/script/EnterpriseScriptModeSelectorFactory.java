package com.etlforge.integration.client.ui.script;

import org.etlforge.integration.client.ui.script.builder.ScriptModeSelector;
import org.etlforge.integration.client.ui.script.builder.ScriptModeSelectorFactory;

public final class EnterpriseScriptModeSelectorFactory implements ScriptModeSelectorFactory {
   @Override
   public ScriptModeSelector newSelector() {
      return new EnterpriseScriptModeSelector();
   }
}
