package org.etlforge.integration.client.ui.deploy.page;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.etlforge.application.ui.DoNotShowAgainMessage;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.overlay.OverlayContainer;

final class ReportPanel {
   private final OverlayContainer overlayContainer;
   private JLabel statusLabel;
   private JLabel resultReportArea;
   private JPanel panel;

   public ReportPanel(OverlayContainer var1) {
      this.overlayContainer = var1;
      this.createComponents();
   }

   public JComponent getDisplayer() {
      return this.panel;
   }

   public StatusUpdater getValidationTimer() {
      return new StatusUpdater(this.statusLabel, Strings.get("Deploy.Page.Labels.Status"));
   }

   public void onValidProject() {
      this.statusLabel.setIcon(CommonIcons.SUCCESS_16);
      this.statusLabel.setText(Strings.get("Deploy.Page.Labels.Valid"));
      this.resultReportArea.setText(Strings.get("Deploy.Page.Messages.Valid"));
   }

   public void onInvalidProject() {
      this.statusLabel.setIcon(CommonIcons.ERROR_16);
      this.statusLabel.setText(Strings.get("Deploy.Page.Labels.Invalid"));
      this.resultReportArea.setText(Strings.get("Deploy.Page.Messages.Invalid"));
      DoNotShowAgainMessage var1 = new DoNotShowAgainMessage(this.panel, "Validation Errors", ShowMessageOnInvalidProjectPreference.INSTANCE);
      if (this.overlayContainer != null) {
         var1.showAsOverlay(this.overlayContainer);
      } else {
         var1.showInDialogLater(null);
      }
   }

   private void createComponents() {
      OneColumnPanel var1 = this.createInnerPanel();
      this.createAndAddStatusLabel(var1);
      this.createAndAddReportArea(var1);
      this.panel = this.createOuterPanel(var1);
   }

   private OneColumnPanel createInnerPanel() {
      return new OneColumnPanel()
         .withInsets(2, 0, 4, 0)
         .withBackground(javax.swing.UIManager.getColor("Panel.background"))
         .withBorder(new BorderBuilder().line().empty(5))
         .setFill(OneColumnPanel.NONE);
   }

   private JPanel createOuterPanel(OneColumnPanel var1) {
      JPanel var2 = new JPanel(new FlowLayout(3));
      var2.setOpaque(false);
      var1.addToParent(var2, null);
      return var2;
   }

   private void createAndAddStatusLabel(OneColumnPanel var1) {
      this.statusLabel = TextStyles.DefaultBold.makeLabel(" ");
      var1.add(this.statusLabel);
   }

   private void createAndAddReportArea(OneColumnPanel var1) {
      this.resultReportArea = new JLabel();
      var1.add(this.resultReportArea);
   }
}

