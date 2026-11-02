package com.etlforge.integration.client.ui.script;

import com.google.common.collect.Maps;
import java.util.EnumMap;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderMode;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.client.ui.script.builder.ScriptModeSelector;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.widget.combobox.EnumComboBox;
import org.etlforge.util.preference.KongaPreferences;

final class EnterpriseScriptModeSelector implements ScriptModeSelector {
   private static final String PREF_KEY = "DefaultMode";
   private final KongaPreferences prefs = KongaPreferences.getPreferences(this.getClass());
   private final EnumComboBox<ScriptBuilderMode> selector = createSelector();
   private ScriptBuilderUi builder;

   public EnterpriseScriptModeSelector() {
      this.selector.setSelectedItem(this.prefs.getEnum("DefaultMode", ScriptBuilderMode.class, ScriptBuilderMode.DEVELOPER));
      this.selector.setSelectionReceiver(new EnterpriseScriptModeSelector.SelectorListener());
   }

   private static EnumComboBox<ScriptBuilderMode> createSelector() {
      EnumComboBox var0 = EnumComboBox.of(ScriptBuilderMode.class);
      EnumMap var1 = createIconMap();
      var0.setIcons(var1);
      var0.adjustPrototypeValue();
      return var0;
   }

   private static EnumMap<ScriptBuilderMode, Icon> createIconMap() {
      EnumMap var0 = Maps.newEnumMap(ScriptBuilderMode.class);
      var0.put(ScriptBuilderMode.BUSINESS_ANALYST, ClientIcons.BUSINESS_ANALYST_16);
      var0.put(ScriptBuilderMode.DEVELOPER, ClientIcons.DEVELOPER_16);
      return var0;
   }

   @Override
   public void decorate(UiCanvas var1) {
      Horizontal.box().of("Mode: ", this.selector).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocusInWindow();
   }

   @Override
   public void setScriptBuilder(ScriptBuilderUi var1) {
      this.builder = var1;
      this.updateBuilder();
   }

   private void updateBuilder() {
      if (this.builder != null) {
         ScriptBuilderMode var1 = this.selector.getSelectedItem();
         this.builder.setMode(var1);
      }
   }

   private class SelectorListener implements Receiver<Object> {
      private SelectorListener() {
      }

      @Override
      public void handle(Object var1) {
         this.storeValueInPrefs(var1);
         EnterpriseScriptModeSelector.this.updateBuilder();
      }

      private void storeValueInPrefs(Object var1) {
         ScriptBuilderMode var2 = (ScriptBuilderMode)var1;
         EnterpriseScriptModeSelector.this.prefs.putEnum("DefaultMode", var2);
      }
   }
}
