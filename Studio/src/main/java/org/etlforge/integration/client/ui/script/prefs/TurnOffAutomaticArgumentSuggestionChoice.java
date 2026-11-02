package org.etlforge.integration.client.ui.script.prefs;

import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class TurnOffAutomaticArgumentSuggestionChoice implements UiProvider {
   private final KongaCheckBox checkBox = new KongaCheckBox("&Do not show me again");

   public void apply() {
      if (this.checkBox.isSelected()) {
         AutoSuggestFunctionArgumentsPreference.INSTANCE.set(false);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.checkBox);
   }

   @Override
   public void requestFocus() {
      this.checkBox.requestFocusInWindow();
   }
}
