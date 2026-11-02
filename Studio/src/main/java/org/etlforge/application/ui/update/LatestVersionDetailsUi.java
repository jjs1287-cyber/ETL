package org.etlforge.application.ui.update;

import com.google.common.base.Optional;
import java.awt.Component;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.update.LatestVersion;
import org.etlforge.ui.layout.DialogLayoutBuilder;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.util.StorageSizeUnit;

final class LatestVersionDetailsUi {
   private static final boolean SUPPORTS_OVERLAYS = false;
   private final JComponent ui;
   private final KongaLayeredPane layeredPane;

   public LatestVersionDetailsUi(LatestVersion var1, KongaLayeredPane var2) {
      this.layeredPane = var2;
      this.ui = layoutFields(var1);
   }

   private static JComponent layoutFields(LatestVersion var0) {
      DialogLayoutBuilder var1 = new DialogLayoutBuilder(5, 5);
      var1.addRow(label("Version number:"), value(var0.getVersionNumber().toString()));
      var1.addRow(label("Release date:"), value(getReleaseDate(var0)));
      var1.addRow(label("Download URL:"), value(getDownloadUrl(var0)));
      var1.addRow(label("File size:"), value(getFileSize(var0)));
      JPanel var2 = var1.container();
      var2.setBackground(Colors.NEUTRAL_BACKGROUND);
      var2.setBorder(new BorderBuilder().line().empty(12));
      return var2;
   }

   private static JLabel label(String var0) {
      return TextStyles.DefaultBold.makeLabel(var0);
   }

   private static JLabel value(String var0) {
      return TextStyles.DefaultText.makeLabel(var0);
   }

   private static String getDownloadUrl(LatestVersion var0) {
      return var0.hasDownloadUrl() ? var0.getDownloadUrl() : "Unknown";
   }

   private static String getReleaseDate(LatestVersion var0) {
      if (var0.hasReleaseDate()) {
         Date var1 = var0.getReleaseDate();
         return DateFormat.getDateInstance(1).format(var1);
      } else {
         return "Unknown";
      }
   }

   private static String getFileSize(LatestVersion var0) {
      if (!var0.hasFileSize()) {
         return "Unknown";
      } else {
         long var1 = var0.getFileSizeInBytes();
         long var3 = StorageSizeUnit.Megabytes.inBytes();
         if (var1 > var3 / 10L) {
            double var7 = var1 / (1.0 * var3);
            return String.format("%.1f MB", var7);
         } else {
            long var5 = StorageSizeUnit.Kilobytes.convertToThisUnit(var1);
            return MessageFormat.format("{0} KB", var5);
         }
      }
   }

   public void open() {
      if (this.isOverlayOk()) {
         this.showInOverlay();
      } else {
         this.showInPopup(Optional.of(this.layeredPane));
      }
   }

   private boolean isOverlayOk() {
      return false;
   }

   private void showInOverlay() {
      KongaScrollPane var1 = new KongaScrollPane(this.ui);
      var1.makeInconspicuous();
      OverlayDecorator var2 = new OverlayDecorator(var1, "Version Details");
      var2.setBlocking(true);
      var2.useCloseOnly();
      var2.show(this.layeredPane, new OverlayCenterPositioner());
   }

   public void showInPopup(Optional<? extends Component> var1) {
      NonBlockingAlert.message(ApplicationUi.getWindowAncestor(var1), this.ui, "Version Details", Alert.Type.INFORMATION);
   }
}
