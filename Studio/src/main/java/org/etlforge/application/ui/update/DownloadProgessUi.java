package org.etlforge.application.ui.update;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.io.File;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.util.version.Version;

final class DownloadProgessUi {
   private final Version buildNumber;
   private final File destination;
   private final JProgressBar progressBar = new JProgressBar(0, 100);
   private final JLabel messageLabel = TextStyles.DefaultTextSmall.makeLabel(" ");
   private Optional<Action> cancelAction = Optional.absent();

   public DownloadProgessUi(Version var1, File var2) {
      this.buildNumber = var1;
      this.destination = var2;
      this.progressBar.setValue(0);
   }

   public void setCancelAction(Action var1) {
      this.cancelAction = Optional.of(var1);
   }

   public void updateProgress(final int var1, final String var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (var1 < 0) {
               DownloadProgessUi.this.progressBar.setIndeterminate(true);
            } else {
               DownloadProgessUi.this.progressBar.setIndeterminate(false);
               DownloadProgessUi.this.progressBar.setValue(Math.min(100, var1));
            }

            DownloadProgessUi.this.messageLabel.setText(var2);
         }
      });
   }

   public JComponent layout() {
      String var1 = "Downloading version " + this.buildNumber + " to " + this.destination.getAbsolutePath();
      OneColumnPanel var2 = new OneColumnPanel().stretchHorizontally().nonOpaque().of(this.progressBar, this.messageLabel);
      return new BorderLayoutBuilder(0, 8)
         .withBackground(Colors.NEUTRAL_BACKGROUND)
         .withEmptyBorder(8)
         .northToSouth(var1, var2, this.cancelAction.orNull())
         .container();
   }
}
