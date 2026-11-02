package com.etlforge.integration.client.ui.sfdc.wizard.prefs;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class NameDisplayPreferences implements PreferenceUi {
   private final FixedSetPreferenceUi<NameDisplayMode> objectNames;
   private final FixedSetPreferenceUi<NameDisplayMode> fieldNames;

   public NameDisplayPreferences(EnumPreference<NameDisplayMode> var1, EnumPreference<NameDisplayMode> var2) {
      this.objectNames = createPart(var1);
      this.fieldNames = createPart(var2);
   }

   private static FixedSetPreferenceUi<NameDisplayMode> createPart(EnumPreference<NameDisplayMode> var0) {
      FixedSetPreferenceUi var1 = new FixedSetPreferenceUi(var0, "", false);
      var1.setSelectedValue(var0.get());
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = new TwoColumnPanel().withBorder(StyleSheet.getDefaultTitleBorder("Object & Field Names"));
      var2.addRow(new JLabel("Use Labels or API Names when displaying objects and fields"));
      var2.addRow(new JLabel("(This does not affect wizards and pages that are already open)"));
      var2.addRow(new JLabel("Objects:"), InvisiblePanel.of(this.objectNames));
      var2.addRow(new JLabel("Fields:"), InvisiblePanel.of(this.fieldNames));
      var1.display(var2.invisible());
   }

   @Override
   public void requestFocus() {
      this.objectNames.requestFocus();
   }

   @Override
   public void apply() {
      this.objectNames.apply();
      this.fieldNames.apply();
   }

   @Override
   public void restore() {
      this.objectNames.restore();
      this.fieldNames.restore();
   }

   @Override
   public void restoreDefault() {
      this.objectNames.restoreDefault();
      this.fieldNames.restoreDefault();
   }

   @Override
   public boolean isDirty() {
      return this.objectNames.isDirty() || this.fieldNames.isDirty();
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.objectNames.addChangeListener(var1);
      this.fieldNames.addChangeListener(var1);
   }

   @Override
   public void share() {
      this.objectNames.share();
      this.fieldNames.share();
   }
}
