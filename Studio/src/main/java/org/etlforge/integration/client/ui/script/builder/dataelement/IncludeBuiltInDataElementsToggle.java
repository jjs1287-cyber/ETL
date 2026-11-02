package org.etlforge.integration.client.ui.script.builder.dataelement;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.integration.client.ui.de.global.GlobalDataElementsBrowser;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;
import org.etlforge.ui.action.ToggleAction;

final class IncludeBuiltInDataElementsToggle extends AbstractAction implements ToggleAction {
   private final GlobalDataElementsBrowser browser;
   private final BooleanPreference pref;

   public IncludeBuiltInDataElementsToggle(GlobalDataElementsBrowser var1, BooleanPreference var2) {
      super(ScriptResources.getString("DataElementSelector.IncludeBuiltIn.Name"));
      this.browser = var1;
      this.pref = var2;
      this.setSelected(!var2.get());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      boolean var2 = !this.isSelected();
      this.setSelected(var2);
      this.pref.set(var2);
   }

   @Override
   public boolean isSelected() {
      Boolean var1 = (Boolean)this.getValue("ToggleAction#ON_OFF");
      return var1 != null ? var1 : true;
   }

   @Override
   public void setSelected(boolean var1) {
      this.putValue("ToggleAction#ON_OFF", var1);
      this.browser.setIncludeBuiltInDataElements(var1);
   }
}
