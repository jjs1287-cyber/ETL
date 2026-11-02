package org.etlforge.integration.activity.ui.progress;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.activity.client.progress.ProgressSnapshot;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.util.time.DateUtils;

public final class ProgressSnapshotUi implements UiProvider {
   private ProgressSnapshot lastSnapshot;
   private final JLabel sectionLabel = new JLabel();
   private final JLabel messageLabel = new JLabel();
   private final JProgressBar progressBar;
   private JComponent ui;

   public ProgressSnapshotUi() {
      TextStyles.DefaultTextSmall.makeOver(this.messageLabel);
      this.progressBar = new JProgressBar(0, 100);
      this.update(null);
   }

   public void update(ProgressSnapshot var1) {
      this.updateUi(var1);
      this.lastSnapshot = var1;
   }

   private void updateUi(ProgressSnapshot var1) {
      this.updateSectionLabel(var1);
      this.updateMessageLabel(var1);
      this.updateProgressBar(var1);
   }

   private void updateSectionLabel(ProgressSnapshot var1) {
      String var2 = "Progress: ";
      if (var1 != null) {
         var2 = var2 + var1.getSection();
         if (!this.isSectionDone(var1)) {
            Date var3 = var1.getEstimatedEndTime();
            if (this.isInTheFuture(var3)) {
               var2 = var2 + " (estimated end time: " + getEstimatedEndTimeDisplay(var3) + ")";
            }
         }
      } else {
         var2 = var2 + "No update available";
      }

      this.sectionLabel.setText(var2);
   }

   private boolean isInTheFuture(Date var1) {
      return var1 != null && var1.getTime() > System.currentTimeMillis();
   }

   private boolean isSectionDone(ProgressSnapshot var1) {
      return var1 != null && var1.getPercentageDone() == 100;
   }

   private static String getEstimatedEndTimeDisplay(Date var0) {
      String var1 = DateFormat.getTimeInstance(3).format(var0);
      if (DateUtils.isToday(var0)) {
         return var1;
      } else {
         return DateUtils.isTomorrow(var0) ? "Tomorrow at " + var1 : DateFormat.getDateInstance().format(var0) + " at " + var1;
      }
   }

   private void updateMessageLabel(ProgressSnapshot var1) {
      String var2 = var1 != null ? var1.getMessage() : " ";
      if (var2.isEmpty()) {
         var2 = " ";
      }

      this.messageLabel.setText(var2);
   }

   private void updateProgressBar(ProgressSnapshot var1) {
      int var2 = var1 != null ? var1.getPercentageDone() : 0;
      if (var2 >= 0) {
         this.progressBar.setValue(var2);
      } else if (this.isNewSection(var1)) {
         this.progressBar.setValue(0);
      }
   }

   private boolean isNewSection(ProgressSnapshot var1) {
      return this.lastSnapshot == null || !this.lastSnapshot.getSection().equals(var1.getSection());
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.doLayout();
      var1.display(var2);
      this.ui = var2;
   }

   private JComponent doLayout() {
      OneColumnPanel var1 = new OneColumnPanel()
         .nonOpaque()
         .withBorder(Empty.border(0, 12, 0, 12))
         .setWeightX(1.0)
         .withInsets(0, 0, 4, 0)
         .of(this.progressBar, this.messageLabel);
      return Vertical.borderLayout(4).withBackground(javax.swing.UIManager.getColor("Panel.background")).withBorder(new BorderBuilder().etched().empty(2)).of(this.sectionLabel, var1).container();
   }

   @Override
   public void requestFocus() {
   }

   public void stop() {
      this.lastSnapshot = null;
      this.sectionLabel.setText(" ");
      this.messageLabel.setText(" ");
      this.progressBar.setValue(100);
   }

   public void setVisible(boolean var1) {
      if (this.ui != null) {
         this.ui.setVisible(var1);
      }
   }
}

