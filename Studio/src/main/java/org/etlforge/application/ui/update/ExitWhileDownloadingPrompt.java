package org.etlforge.application.ui.update;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.update.ExitWhileDownloadingHandler;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.widget.button.KongaButton;

public final class ExitWhileDownloadingPrompt implements ExitWhileDownloadingHandler {
   @Override
   public boolean isExitAllowed() {
      ExitWhileDownloadingPrompt.Prompt var1 = new ExitWhileDownloadingPrompt.Prompt();
      return UiThreadSupplier.call(var1);
   }

   private static class Prompt implements Supplier<Boolean> {
      private static final int CONTINUE_DOWNLOAD = 0;
      private static final int CANCEL_DOWNLOAD = 1;
      private int choice;
      private KongaDialog dialog;
      private KongaButton continueDownload;

      private Prompt() {
      }

      public Boolean get() {
         this.choice = 0;
         this.dialog = this.createDialog();
         this.dialog.setVisible(true);
         boolean var1 = this.choice == 1;
         return var1;
      }

      private KongaDialog createDialog() {
         KongaDialog var1 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Continue download?", true);
         JLabel var2 = new JLabel("You are currently downloading a new version.");
         JPanel var3 = this.createChoiceButtons(var1);
         BorderLayoutBuilder var4 = new BorderLayoutBuilder(0, 10).withEmptyBorder(10).north(var2).south(var3);
         var1.standardLayout(var4, null);
         var1.getRootPane().setDefaultButton(this.continueDownload);
         var1.setLocationRelativeTo(null);
         return var1;
      }

      private JPanel createChoiceButtons(KongaDialog var1) {
         this.continueDownload = new KongaButton(new ExitWhileDownloadingPrompt.Prompt.Choice(0));
         KongaButton var2 = new KongaButton(new ExitWhileDownloadingPrompt.Prompt.Choice(1));
         ImmutableList var3 = ButtonOrder.okAndCancelOrder(this.continueDownload, var2);
         return var1.getButtonPanel(var3, -1, 10, 0);
      }

      private class Choice extends AbstractAction {
         private final int choice;

         public Choice(int var2) {
            super(var2 == 0 ? "Let download finish" : "Cancel download and exit");
            this.choice = var2;
         }

         @Override
         public void actionPerformed(ActionEvent var1) {
            Prompt.this.choice = this.choice;
            Prompt.this.dialog.dispose();
         }
      }
   }
}
