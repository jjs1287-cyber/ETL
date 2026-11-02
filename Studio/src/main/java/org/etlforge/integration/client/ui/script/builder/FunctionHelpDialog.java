package org.etlforge.integration.client.ui.script.builder;

import java.awt.Window;
import java.awt.Dialog.ModalityType;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;

public final class FunctionHelpDialog {
   public static void showFunctionHelp(Function var0, Window var1) {
      FunctionHelpDialog.FunctionDialog var2 = new FunctionHelpDialog.FunctionDialog(var1, var0);
      var2.setVisible(true);
   }

   private FunctionHelpDialog() {
   }

   private static final class FunctionDialog extends KongaDialog {
      private final UiProvider helpArea;

      public FunctionDialog(Window var1, Function var2) {
         super(var1, "Function Description", ModalityType.MODELESS);
         this.helpArea = new FunctionHelpLayout(var2);
         this.standardLayout((UiProvider)this.helpArea, CLOSE_ONLY);
         this.setCloseButtonAsDefault();
         this.manageLocation(FunctionHelpDialog.FunctionDialog.class);
      }
   }
}
