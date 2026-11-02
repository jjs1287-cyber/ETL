package org.etlforge.integration.client.ui;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.LookAndFeelPreference;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.application.ui.prefs.IntPreferenceUi;
import org.etlforge.application.ui.prefs.PreferenceUi;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.application.ui.window.editor.RecentlyClosedEditorsPreference;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiMode;
import org.etlforge.application.ui.window.editor.service.EditorServiceUiModePreference;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class UiPreferencesPage extends AbstractPreferencesPage {
   public UiPreferencesPage() {
      super(new String[]{"General", "UI"});
      this.addControl(new UiPreferencesPage.LookAndFeelPreferenceUi());
      this.addControl(new UiPreferencesPage.EditorModePreferenceUi());
      this.addControl(this.createRecentlyClosedEditorsPrefUi());
      this.addControl(new BooleanPreferenceUi(AnimationPreference.INSTANCE, "Use animation effects in the UI"));
   }

   private PreferenceUi createRecentlyClosedEditorsPrefUi() {
      IntPreferenceUi var1 = new IntPreferenceUi(
         RecentlyClosedEditorsPreference.INSTANCE, "Number of recently closed pages to keep in the list under Windows > Recently Closed:", 2
      );
      var1.setSelectAllWhenFocused(true);
      return var1;
   }

   private static class EditorModePreferenceUi extends FixedSetPreferenceUi<EditorServiceUiMode> {
      public EditorModePreferenceUi() {
         super(EditorServiceUiModePreference.INSTANCE, "Window display mode:", false);
      }

      protected boolean include(EditorServiceUiMode var1) {
         return var1.isSupported();
      }
   }

   private static class LookAndFeelPreferenceUi extends FixedSetPreferenceUi<SupportedLookAndFeel> {
      public LookAndFeelPreferenceUi() {
         super(LookAndFeelPreference.INSTANCE, "", false);
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
         var2.north("Set the preferred Look & Feel");
         JPanel var3 = InvisiblePanel.of(this.getSelector());
         var3.setBorder(Empty.border(0, 25, 0, 0));
         var2.west(var3);
         var2.south(AbstractPreferencesPage.getRequiresRestartNote());
         var2.withBorder(StyleSheet.getDefaultTitleBorder("Look & Feel"));
         var2.decorate(var1);
      }

      @Override
      protected RadioButtonGroupPanel.Layout getRadioButtonLayout() {
         return RadioButtonGroupPanel.Layout.ONE_COLUMN;
      }

      protected String getDisplayString(SupportedLookAndFeel var1) {
         switch (var1) {
            case SYSTEM:
               return "System (Windows look on Windows, etc)";
            case CROSS_PLATFORM:
               return "Cross Platform (the default Java Metal look)";
            case NIMBUS:
               return "Nimbus (requires Java SE 6 update 10, or higher)";
            default:
               throw new RuntimeException("Unexpected SupportedLookAndFeel: " + var1);
         }
      }
   }
}
