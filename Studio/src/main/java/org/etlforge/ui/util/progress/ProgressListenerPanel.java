package org.etlforge.ui.util.progress;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.LabelAdapter;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.progress.ProgressEvent;
import org.etlforge.util.progress.ProgressListener;

public final class ProgressListenerPanel implements ProgressListener, UiProvider {
   private JPanel panel;
   private ProgressListenerPanel.ProgressBarWrapper progressBar;
   private JLabel label;

   public ProgressListenerPanel() {
      this(true);
   }

   public ProgressListenerPanel(boolean var1) {
      this.createComponents(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.progressBar.requestFocusInWindow();
   }

   @Override
   public void progressEnded(ProgressEvent var1) {
      this.progressBar.setValue(100);
      this.setMessage(var1.getMessage());
   }

   @Override
   public void progressStarted(ProgressEvent var1) {
      this.progressBar.setValue(0);
      this.label.setText(" ");
   }

   public void decorateLabel(LabelDecorator var1) {
      LabelAdapter var2 = new LabelAdapter(this.label);
      var1.decorate(var2);
   }

   @Override
   public void progressUpdated(ProgressEvent var1) {
      float var2 = var1.getPercentageCompleted();
      if (var2 == -1.0F) {
         this.progressBar.setIndeterminate(true);
      } else {
         this.progressBar.setIndeterminate(false);
         this.progressBar.setValue((int)(var2 * 100.0F));
      }

      this.setMessage(var1.getMessage());
   }

   private void createComponents(boolean var1) {
      JPanel var2 = new JPanel(new GridBagLayout());
      GridBagConstraints var3 = this.getInitialConstraints();
      if (var1) {
         this.createAndAddProgressBar(var2, var3);
      } else {
         this.createNullProgressBar();
      }

      var3.weightx = 0.0;
      this.createAndAddLabel(var2, var3);
      this.panel = InvisiblePanel.of(var2);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.panel, false);
   }

   private void createAndAddProgressBar(JPanel var1, Object var2) {
      JProgressBar var3 = new JProgressBar(0, 100);
      var1.add(var3, var2);
      this.progressBar = new ProgressListenerPanel.ProgressBarWrapper(var3);
   }

   private void createNullProgressBar() {
      this.progressBar = new ProgressListenerPanel.ProgressBarWrapper(null);
   }

   private void createAndAddLabel(JPanel var1, Object var2) {
      this.label = new JLabel(" ");
      var1.add(this.label, var2);
   }

   private GridBagConstraints getInitialConstraints() {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.anchor = 18;
      var1.insets = new Insets(2, 0, 2, 0);
      var1.gridx = 0;
      return var1;
   }

   private void setMessage(String var1) {
      if (var1 != null && var1.length() != 0) {
         this.label.setText(var1);
      } else {
         this.label.setText(" ");
      }
   }

   private static class ProgressBarWrapper {
      private final JProgressBar wrappee;

      public ProgressBarWrapper(JProgressBar var1) {
         this.wrappee = var1;
      }

      public void requestFocusInWindow() {
         if (this.wrappee != null) {
            this.wrappee.requestFocusInWindow();
         }
      }

      public void setValue(int var1) {
         if (this.wrappee != null) {
            this.wrappee.setValue(0);
         }
      }

      public void setIndeterminate(boolean var1) {
         if (this.wrappee != null) {
            this.wrappee.setIndeterminate(var1);
         }
      }
   }
}
