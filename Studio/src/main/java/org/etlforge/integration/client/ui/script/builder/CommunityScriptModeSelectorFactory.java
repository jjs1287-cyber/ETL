package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.ui.UiCanvas;

public class CommunityScriptModeSelectorFactory implements ScriptModeSelectorFactory {
   @Override
   public ScriptModeSelector newSelector() {
      return new ScriptModeSelector() {
         @Override
         public void setScriptBuilder(ScriptBuilderUi var1) {
            var1.setMode(ScriptBuilderMode.DEVELOPER);
         }

         @Override
         public void decorate(UiCanvas var1) {
         }

         @Override
         public void requestFocus() {
         }
      };
   }
}
