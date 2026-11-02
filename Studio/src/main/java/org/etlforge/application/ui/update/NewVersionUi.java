package org.etlforge.application.ui.update;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.update.LatestVersion;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;

final class NewVersionUi {
   private final Action startDownloadAction;
   private final Action viewDetailsAction;
   private final JLabel caption;

   public NewVersionUi(LatestVersion var1, KongaLayeredPane var2, Action var3) {
      this.startDownloadAction = var3;
      this.viewDetailsAction = new NewVersionUi.ViewDetailsAction(var1, var2);
      this.caption = TextStyles.AdminHint.makeLabel("A new version (" + var1.getVersionNumber() + ") is available.");
      this.caption.setIcon(CommonIcons.INFO_24);
   }

   public JComponent layout() {
      JPanel var1 = Horizontal.box()
         .withEmptyBorder(8)
         .add(this.caption)
         .rigid(16)
         .addAllWithSpace(10, this.viewDetailsAction, this.startDownloadAction)
         .container();
      UiUtils.setPanelBackgrounds(var1, Colors.NEUTRAL_BACKGROUND);
      return var1;
   }

   private class ViewDetailsAction extends AbstractAction {
      private final LatestVersion version;
      private final KongaLayeredPane layeredPane;

      public ViewDetailsAction(LatestVersion var2, KongaLayeredPane var3) {
         super("&View Details...");
         this.version = var2;
         this.layeredPane = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewDetailsAction.this.viewDetails();
            }
         });
      }

      private void viewDetails() {
         LatestVersionDetailsUi var1 = new LatestVersionDetailsUi(this.version, this.layeredPane);
         var1.open();
      }
   }
}
