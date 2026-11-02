package org.etlforge.application.ui.viewer.error.log;

import java.awt.Container;
import org.etlforge.application.DebugMode;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLogConsoleWritingPreference;
import org.etlforge.application.errorlog.ErrorLogMonitorLevelPreference;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FixedSetPreferenceUi;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.TwoColumnPanel;

public class ErrorLogPreferencesPage extends AbstractPreferencesPage {
   private final FixedSetPreferenceUi<ErrorLevel> monitorPreferenceUi = new ErrorLogPreferencesPage.MonitorLevelUi();
   private final BooleanPreferenceUi writeToConsoleUi = new BooleanPreferenceUi(
      ErrorLogConsoleWritingPreference.INSTANCE, "Write reported errors to System.err"
   );

   public ErrorLogPreferencesPage() {
      super(new String[]{"General", "Error Log"});
      this.addControls(this.monitorPreferenceUi, this.writeToConsoleUi);
   }

   @Override
   public void display(Container var1) {
      TwoColumnPanel var2 = new TwoColumnPanel().withInsets(0, 0, 20, 5).withBorder(Empty.border(10)).withRightWeights(1.0, 0.0);
      this.monitorPreferenceUi.layout(var2);
      var2.addRow(this.writeToConsoleUi);
      BorderLayoutBuilder.from(var1).north(var2);
   }

   private static class MonitorLevelUi extends FixedSetPreferenceUi<ErrorLevel> {
      public MonitorLevelUi() {
         super(ErrorLogMonitorLevelPreference.INSTANCE, "Minimum error level for dialog:", false);
         ErrorLevel var1 = ErrorLogMonitorLevelPreference.getPreferredLevel();
         this.setSelectedValue(var1);
      }

      protected boolean include(ErrorLevel var1) {
         switch (var1) {
            case FATAL:
               return false;
            case INTERNAL:
               return DebugMode.IS_ENABLED;
            default:
               return true;
         }
      }
   }
}
