package org.etlforge.application.ui.update;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.viewer.error.StackTraceDisplayer;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class ErrorUi {
   private final JLabel message;
   private final ErrorUi.ShowDetailsAction showDetailsAction;
   private final Action checkAgainAction;

   public ErrorUi(Exception var1, Action var2) {
      this.showDetailsAction = new ErrorUi.ShowDetailsAction(var1);
      this.checkAgainAction = var2;
      this.message = this.createMessageLabel(var1.getMessage());
   }

   private JLabel createMessageLabel(String var1) {
      KongaLabel var2 = TextStyles.AdminHint.makeLabel(var1);
      var2.setIcon(CommonIcons.ERROR_24);
      var2.setBorder(Empty.border(8));
      return var2;
   }

   public JComponent layout() {
      BoxBuilder var1 = Horizontal.box().nonOpaque().add(this.message).rigid(10).add(InvisiblePanel.of(this.showDetailsAction));
      return new BorderLayoutBuilder(0, 4).withEmptyBorder(8).north(var1).south(InvisiblePanel.of(this.checkAgainAction)).nonOpaque().container();
   }

   private static class ShowDetailsAction extends AbstractAction {
      private final Exception error;

      public ShowDetailsAction(Exception var1) {
         super("Error Details...");
         this.error = var1;
         this.putValue("ShortDescription", "Show mode details about the error");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ShowDetailsAction.this.showDetails(UiUtils.getComponentSource(var1));
            }
         });
      }

      private void showDetails(Optional<Component> var1) {
         StackTraceDisplayer var2 = new StackTraceDisplayer();
         var2.displayStackTrace(this.error);
         KongaDialog var3 = new KongaDialog(ApplicationUi.getWindowAncestor(var1), "Error Details");
         var3.standardLayout(var2, KongaDialog.CLOSE_ONLY);
         var3.setCloseButtonAsDefault();
         var3.manageLocation(ErrorUi.class);
         var3.setVisible(true);
      }
   }
}
