package org.etlforge.application.ui.update;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.widget.button.KongaButton;

final class AbortDownloadPrompt {
   public static final int CLOSE_AND_ABORT = 0;
   public static final int CLOSE_AND_CONTINUE = 1;
   public static final int LEAVE_OPEN = 2;
   private int choice;
   private KongaButton closeAndContinue;

   public int ask(Optional<JComponent> var1) {
      this.choice = 2;
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      return this.choice;
   }

   private KongaDialog createDialog(Optional<JComponent> var1) {
      KongaDialog var2 = new KongaDialog(ApplicationUi.getWindowAncestor(var1), "Continue download?");
      JLabel var3 = new JLabel("You are currently downloading a new version.");
      JPanel var4 = this.createChoiceButtons(var2);
      BorderLayoutBuilder var5 = new BorderLayoutBuilder(0, 10).withEmptyBorder(10).north(var3).south(var4);
      var2.standardLayout(var5, null);
      var2.getRootPane().setDefaultButton(this.closeAndContinue);
      var2.setLocationRelativeTo(null);
      return var2;
   }

   private JPanel createChoiceButtons(KongaDialog var1) {
      KongaButton var2 = new KongaButton(new AbortDownloadPrompt.Choice(var1, "Leave open", 2));
      this.closeAndContinue = new KongaButton(new AbortDownloadPrompt.Choice(var1, "Continue download in background", 1));
      KongaButton var3 = new KongaButton(new AbortDownloadPrompt.Choice(var1, "Cancel download", 0));
      ImmutableList var4 = ButtonOrder.defaultActionCancelAndNoAction(this.closeAndContinue, var3, var2);
      return var1.getButtonPanel(var4, -1, 10, 0);
   }

   private class Choice extends AbstractAction {
      private final KongaDialog dialog;
      private final int choice;

      public Choice(KongaDialog var2, String var3, int var4) {
         super(var3);
         this.dialog = var2;
         this.choice = var4;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               Choice.this.applyChoice();
            }
         });
      }

      private void applyChoice() {
         AbortDownloadPrompt.this.choice = this.choice;
         if (this.choice != 2) {
            this.dialog.dispose();
         }
      }
   }
}
