package com.etlforge.integration.client.ui.interchange.project.backup.complete;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.interchange.project.prefs.WarnBeforeRestoringBackupPreference;
import javax.swing.JFrame;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.DoNotShowAgainQuestion;
import org.etlforge.ui.UiThreadSupplier;

public final class PreferencedBasedPrompt implements Supplier<Boolean> {
   private final JFrame applicationFrame;

   public PreferencedBasedPrompt(JFrame var1) {
      this.applicationFrame = var1;
   }

   public Boolean get() {
      return UiThreadSupplier.call(
         new Supplier<Boolean>() {
            public Boolean get() {
               DoNotShowAgainQuestion var1 = new DoNotShowAgainQuestion(
                  "This will restore the complete project from backup. All the existing project data will be deleted.\n\nDo you want to continue?",
                  "Are you sure?",
                  WarnBeforeRestoringBackupPreference.INSTANCE,
                  Alert.Option.CONTINUE_CANCEL,
                  Alert.Option.CONTINUE,
                  Alert.Type.WARNING
               );
               return var1.ask(PreferencedBasedPrompt.this.applicationFrame);
            }
         }
      );
   }
}
