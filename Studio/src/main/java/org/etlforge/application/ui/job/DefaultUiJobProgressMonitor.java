package org.etlforge.application.ui.job;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.DottedTextLabel;

public final class DefaultUiJobProgressMonitor implements UiJobProgressMonitor, UiProvider {
   private UiJob job;
   private JProgressBar progressBar;
   private DottedTextLabel messageLabel;
   private IconLabel iconLabel;
   private JButton cancelButton;
   private String lastMessage;
   private JPanel displayPanel;

   public DefaultUiJobProgressMonitor(UiJob var1, boolean var2) {
      this(var1, var2, true);
   }

   public DefaultUiJobProgressMonitor(UiJob var1, boolean var2, boolean var3) {
      this.job = var1;
      this.createComponents(var2);
      this.layoutComponents(var3);
   }

   @Override
   public void started(UiJob var1) {
      if (var1 == this.job) {
         this.update();
         this.messageLabel.setUpdateAutomatically(250);
      }
   }

   @Override
   public void ended() {
      this.messageLabel.stopAutomaticUpdate();
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DefaultUiJobProgressMonitor.this.iconLabel.setOff();
            if (DefaultUiJobProgressMonitor.this.progressBar.isIndeterminate()) {
               DefaultUiJobProgressMonitor.this.progressBar.setIndeterminate(false);
            }

            DefaultUiJobProgressMonitor.this.progressBar.setValue(DefaultUiJobProgressMonitor.this.progressBar.getMaximum());
            if (DefaultUiJobProgressMonitor.this.cancelButton != null) {
               DefaultUiJobProgressMonitor.this.cancelButton.setEnabled(false);
            }

            DefaultUiJobProgressMonitor.this.job = null;
         }
      });
   }

   @Override
   public void setCompletionLevel(int var1) {
      this.updateProgressBar(var1);
   }

   @Override
   public void setMessage(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (!DefaultUiJobProgressMonitor.areEqual(var1, DefaultUiJobProgressMonitor.this.lastMessage)) {
               DefaultUiJobProgressMonitor.this.lastMessage = var1;
               DefaultUiJobProgressMonitor.this.messageLabel.setText(var1);
            }

            DefaultUiJobProgressMonitor.this.messageLabel.update();
         }
      });
   }

   @Override
   public void setCanBeCancelled(boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DefaultUiJobProgressMonitor.this.updateCancelButton();
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayPanel);
   }

   @Override
   public void requestFocus() {
   }

   private void update() {
      this.updateCancelButton();
      this.updateProgressBar();
      this.setStatus();
   }

   private void updateCancelButton() {
      if (this.cancelButton != null) {
         this.cancelButton.setEnabled(this.job.isRunning() && this.job.canBeCancelled());
      }
   }

   private void updateProgressBar() {
      int var1 = this.job.getCompletionLevel();
      this.updateProgressBar(var1);
   }

   private void updateProgressBar(final int var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var1 == -1) {
               DefaultUiJobProgressMonitor.this.progressBar.setIndeterminate(true);
            } else {
               DefaultUiJobProgressMonitor.this.progressBar.setIndeterminate(false);
               DefaultUiJobProgressMonitor.this.progressBar.setValue(var1);
            }
         }
      });
   }

   private void setStatus() {
      String var1 = this.job.getMessage();
      this.setMessage(var1);
   }

   private void createComponents(boolean var1) {
      this.progressBar = this.createProgressBar();
      this.messageLabel = this.createShortStatusLabel();
      if (var1) {
         this.cancelButton = this.createCancelButton();
      }

      this.displayPanel = new JPanel(new BorderLayout(0, 5));
   }

   private JProgressBar createProgressBar() {
      return new JProgressBar(0, 100) {
         @Override
         public Dimension getPreferredSize() {
            Dimension var1 = super.getPreferredSize();
            return new Dimension(2 * var1.width, var1.height);
         }
      };
   }

   private DottedTextLabel createShortStatusLabel() {
      LabelDecorator var1 = TextStyles.DefaultTextSmall.asLabelDecorator(null);
      return new DottedTextLabel(var1);
   }

   private JButton createCancelButton() {
      KongaButton var1 = new KongaButton(ProcessConsoleIcons.STOP).makeToolbarButton();
      var1.setToolTipText(Strings.get("Job.DefaultDisplayer.Cancel.ToolTip"));
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ApplicationUi.getJobManager().cancelJob(DefaultUiJobProgressMonitor.this.job);
         }
      });
      return var1;
   }

   private void layoutComponents(boolean var1) {
      JPanel var2 = this.createNorthPanel(var1);
      this.displayPanel.add(var2, "North");
      if (var1) {
         JPanel var3 = this.createCenterPanel();
         this.displayPanel.add(var3, "Center");
      }

      JPanel var4 = this.createSouthPanel();
      this.displayPanel.add(var4, "South");
      UiUtils.setOpaqueRecursively(this.displayPanel, false);
   }

   private JPanel createNorthPanel(boolean var1) {
      JPanel var2 = new JPanel(new FlowLayout(3, 5, 0));
      this.iconLabel = new IconLabel();
      this.iconLabel.setOn();
      this.iconLabel.addToParent(var2, null);
      if (!var1) {
         var2.add(this.cancelButton);
      }

      JLabel var3 = new JLabel(this.job.getName());
      var2.add(var3);
      return var2;
   }

   private JPanel createCenterPanel() {
      JPanel var1 = new JPanel(new FlowLayout(3, 10, 0));
      var1.add(this.progressBar);
      if (this.cancelButton != null) {
         var1.add(this.cancelButton);
      }

      return var1;
   }

   private JPanel createSouthPanel() {
      JPanel var1 = new JPanel(new FlowLayout(3, 10, 0));
      this.messageLabel.addToParent(var1, null);
      return var1;
   }

   private static boolean areEqual(String var0, String var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }
}
