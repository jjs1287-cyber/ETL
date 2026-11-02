package org.etlforge.application.ui.job;

import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.label.DottedTextLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class SimpleJobProgressMonitor implements UiJobProgressMonitor, UiProvider {
   private final IconLabel iconLabel = new IconLabel();
   private final DottedTextLabel nameLabel;
   private UiJob job;

   public SimpleJobProgressMonitor() {
      LabelDecorator var1 = TextStyles.DefaultText.asLabelDecorator(null);
      this.nameLabel = new DottedTextLabel(var1);
   }

   @Override
   public void ended() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SimpleJobProgressMonitor.this.iconLabel.setOff();
            SimpleJobProgressMonitor.this.nameLabel.setText(SimpleJobProgressMonitor.getJobDoneMessage(SimpleJobProgressMonitor.this.job));
         }
      });
   }

   @Override
   public void setCanBeCancelled(boolean var1) {
   }

   @Override
   public void setCompletionLevel(int var1) {
   }

   @Override
   public void setMessage(String var1) {
   }

   @Override
   public void started(final UiJob var1) {
      this.job = var1;
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SimpleJobProgressMonitor.this.iconLabel.setOn();
            SimpleJobProgressMonitor.this.nameLabel.setText(var1.getName());
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.createUi();
      var1.display(var2);
   }

   private JComponent createUi() {
      JPanel var1 = InvisiblePanel.newPanel();
      this.iconLabel.addToParent(var1, null);
      this.nameLabel.addToParent(var1, null);
      return var1;
   }

   @Override
   public void requestFocus() {
   }

   private static String getJobDoneMessage(UiJob var0) {
      return Strings.format("Job.SimpleDisplayer.Done", var0.getName());
   }
}
