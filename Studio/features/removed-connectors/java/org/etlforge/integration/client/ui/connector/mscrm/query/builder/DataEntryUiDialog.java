package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import java.awt.Window;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;

public final class DataEntryUiDialog {
   public static <T> Optional<T> open(DataEntryUi<T> var0, Window var1, String var2) {
      WizardStyleDialog var3 = createDialog(var0, var1, var2);
      var3.setVisible(true);
      return var3.wasOkPressed() ? var0.getEnteredValue() : Optional.absent();
   }

   private static WizardStyleDialog createDialog(final DataEntryUi<?> var0, final Window var1, String var2) {
      WizardStyleDialog var3 = new WizardStyleDialog(ApplicationUi.getAppWinFrame(), var2, var0) {
         @Override
         protected void onOk() {
            EnteredDataValidation var1x = var0.validate();
            if (var1x.isValid()) {
               super.onOk();
            } else {
               this.showValidationError(var1x);
               var0.focusInvalidComonent();
            }
         }

         private void showValidationError(EnteredDataValidation var1x) {
            assert !var1x.isValid();
            Alert.error(var1, var1x.whyNot(), "Error");
         }
      };
      var3.manageLocation(var0.getClass());
      var3.setOkButtonAsDefault();
      return var3;
   }

   private DataEntryUiDialog() {
   }
}
