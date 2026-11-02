package org.etlforge.application.ui.job.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.ProcessConsoleIcons;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobProgressMonitor;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Flow;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class JobSnapshot implements UiJobProgressMonitor, UiProvider, ActionListener {
   private static final Color DONE = new Color(0, 0, 0);
   private UiJob displayedJob;
   private long startTime = -1L;
   private long endTime = -1L;
   private final JProgressBar progressBar;
   private JButton cancelButton;
   private final JButton iconButton;
   private final JLabel nameLabel;
   private final JLabel messageLabel;
   private String lastMessage;
   private JComponent displayer;
   private InfoDisplayer infoWindow;

   public JobSnapshot(UiJob var1) {
      this.displayedJob = var1;
      this.progressBar = new JProgressBar(0, 100) {
         @Override
         public Dimension getPreferredSize() {
            Dimension var1x = super.getPreferredSize();
            return new Dimension(2 * var1x.width, var1x.height);
         }
      };
      this.iconButton = createIconButton();
      this.nameLabel = new JLabel(" ");
      this.messageLabel = TextStyles.DefaultTextSmall.makeLabel(" ");
      this.createCancelButton();
   }

   @Override
   public void started(UiJob var1) {
      if (var1 == this.displayedJob) {
         this.startTime = System.currentTimeMillis();
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               JobSnapshot.this.nameLabel.setText(JobSnapshot.this.displayedJob.getName());
               JobSnapshot.this.update();
            }
         });
      }
   }

   @Override
   public void ended() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            JobSnapshot.this.endTime = System.currentTimeMillis();
            JobSnapshot.this.decorateIconButtonWhenDone();
            JobSnapshot.this.decorateNameLabelWhenDone();
            JobSnapshot.this.cancelButton.setVisible(false);
            JobSnapshot.this.progressBar.setIndeterminate(false);
            JobSnapshot.this.progressBar.setVisible(false);
            JobSnapshot.this.messageLabel.setText(" ");
            JobSnapshot.this.displayedJob = null;
         }
      });
   }

   @Override
   public void setCompletionLevel(final int var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (var1 == -1) {
               JobSnapshot.this.progressBar.setIndeterminate(true);
            } else {
               JobSnapshot.this.progressBar.setIndeterminate(false);
               JobSnapshot.this.progressBar.setValue(var1);
            }
         }
      });
   }

   @Override
   public void setMessage(String var1) {
      final String var2 = var1 != null ? var1 : " ";
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            if (!JobSnapshot.areEqual(var2, JobSnapshot.this.lastMessage)) {
               JobSnapshot.this.lastMessage = var2;
               JobSnapshot.this.messageLabel.setText(var2);
            }
         }
      });
   }

   @Override
   public void setCanBeCancelled(final boolean var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            JobSnapshot.this.cancelButton.setEnabled(var1);
         }
      });
   }

   public boolean isDone() {
      return this.endTime > 0L;
   }

   private void decorateNameLabelWhenDone() {
      this.nameLabel.setText(getNameStringWhenDone(this.displayedJob));
      this.nameLabel.setForeground(DONE);
   }

   private static JButton createIconButton() {
      return new KongaButton(ProcessConsoleIcons.PROCESS_ON_24).makeToolbarButton();
   }

   private void decorateIconButtonWhenDone() {
      this.iconButton.setIcon(ProcessConsoleIcons.INFO);
      ButtonUtils.enableRolloverForButton(this.iconButton);
      this.iconButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (JobSnapshot.this.infoWindow == null) {
               JobSnapshot.this.infoWindow = new InfoDisplayer(JobSnapshot.this.startTime, JobSnapshot.this.endTime);
            }

            JobSnapshot.this.infoWindow.show(JobSnapshot.this.iconButton);
         }
      });
   }

   private void update() {
      if (!this.displayedJob.canBeCancelled()) {
         this.cancelButton.setEnabled(false);
      }

      int var1 = this.displayedJob.getCompletionLevel();
      this.setCompletionLevel(var1);
      this.setStatus();
   }

   private void setStatus() {
      String var1 = this.displayedJob.getMessage();
      this.setMessage(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.displayedJob != null) {
         ApplicationUi.getJobManager().cancelJob(this.displayedJob);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.displayer == null) {
         this.displayer = this.createDisplayer();
      }

      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
   }

   public Rectangle getDisplayerBounds() {
      return this.displayer.getBounds();
   }

   private JComponent createDisplayer() {
      Flow var1 = Flow.leading(5, 0);
      var1.add(this.iconButton, this.nameLabel);
      Flow var2 = Flow.leading(10, 0);
      var2.add(this.progressBar, this.cancelButton);
      Flow var3 = Flow.leading(10, 0);
      var3.add(this.messageLabel);
      BorderLayoutBuilder var4 = new BorderLayoutBuilder();
      var4.northToSouth(var1, var2, var3);
      UiUtils.setOpaqueRecursively(var4.container(), false);
      return var4.container();
   }

   private void createCancelButton() {
      this.cancelButton = new KongaButton(ProcessConsoleIcons.STOP).makeToolbarButton();
      this.cancelButton.setToolTipText(Strings.get("JobConsole.Snapshot.Cancel.ToolTip"));
      this.cancelButton.addActionListener(this);
   }

   private static boolean areEqual(String var0, String var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   private static String getNameStringWhenDone(UiJob var0) {
      String var1;
      switch (var0.getStatus()) {
         case ERROR:
            var1 = Strings.get("JobConsole.Snapshot.Status.Error");
            break;
         case CANCEL:
            var1 = Strings.get("JobConsole.Snapshot.Status.Cancel");
            break;
         default:
            var1 = Strings.get("JobConsole.Snapshot.Status.Ok");
      }

      return Strings.format("JobConsole.Snapshot.NameWhenDone", var0.getName(), var1);
   }
}
