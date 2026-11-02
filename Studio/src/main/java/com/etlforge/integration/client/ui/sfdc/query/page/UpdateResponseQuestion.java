package com.etlforge.integration.client.ui.sfdc.query.page;

import java.util.concurrent.atomic.AtomicReference;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.label.HtmlLabel;

final class UpdateResponseQuestion {
   public static UpdateResponseQuestion.Choice ask() {
      final AtomicReference var0 = new AtomicReference<>(UpdateResponseQuestion.Choice.CANCEL);
      BorderLayoutBuilder var1 = createMessage();
      KongaDialog var2 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Query Change", true) {
         @Override
         protected void onApply() {
            var0.set(UpdateResponseQuestion.Choice.CONTINUE_BUT_DO_NOT_UPDATE);
            this.onDialogClose();
         }

         @Override
         protected void onOk() {
            var0.set(UpdateResponseQuestion.Choice.CONTINUE_AND_UPDATE);
            super.onOk();
         }
      };
      var2.setFixedButtonSize(false);
      var2.standardLayout(var1, KongaDialog.OK_CANCEL_APPLY);
      var2.getOKButton().setText(UpdateResponseQuestion.Choice.CONTINUE_AND_UPDATE.toString());
      var2.getApplyButton().setText(UpdateResponseQuestion.Choice.CONTINUE_BUT_DO_NOT_UPDATE.toString());
      var2.getCancelButton().setText(UpdateResponseQuestion.Choice.CANCEL.toString());
      var2.setResizable(false);
      var2.setLocationRelativeTo(null);
      var2.setOkButtonAsDefault();
      var2.setVisible(true);
      return (UpdateResponseQuestion.Choice)var0.get();
   }

   private static BorderLayoutBuilder createMessage() {
      BorderLayoutBuilder var0 = Vertical.Anchor.north(AlertIcons.INFO);
      String var1 = String.format(
         "You have changed the query string. As a result, the target %s and its mappings must be updated. ETLForge can do that for you.\n\nClick \"%s\" to let ETLForge perform all necessary changes.\n\nClick \"%s\" if you want to go ahead with the change, but prefer to change the %s and mappings yourself.\n\nClick \"%s\" to cancel the change.\n",
         EntityType.Document,
         UpdateResponseQuestion.Choice.CONTINUE_AND_UPDATE,
         UpdateResponseQuestion.Choice.CONTINUE_BUT_DO_NOT_UPDATE,
         EntityType.Document,
         UpdateResponseQuestion.Choice.CANCEL
      );
      HtmlLabel var2 = new HtmlLabel(var1, 400);
      var2.setBorder(Empty.border(0, 0, 10, 0));
      return new BorderLayoutBuilder(10, 0).west(var0).center(var2);
   }

   private UpdateResponseQuestion() {
   }

   public enum Choice {
      CONTINUE_AND_UPDATE,
      CONTINUE_BUT_DO_NOT_UPDATE,
      CANCEL;

      @Override
      public String toString() {
         switch (this) {
            case CONTINUE_AND_UPDATE:
               return "Continue and update (recommeded)";
            case CONTINUE_BUT_DO_NOT_UPDATE:
               return "Continue but do not update";
            case CANCEL:
               return "Cancel";
            default:
               throw new RuntimeException("Unexpected Choice: " + this.name());
         }
      }
   }
}
