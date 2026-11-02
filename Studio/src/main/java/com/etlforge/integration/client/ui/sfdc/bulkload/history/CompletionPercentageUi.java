package com.etlforge.integration.client.ui.sfdc.bulkload.history;

import java.text.DecimalFormat;
import javax.swing.JProgressBar;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

final class CompletionPercentageUi implements UiProvider {
   private final JProgressBar completionBar = new JProgressBar(0, 1000);

   public CompletionPercentageUi() {
      this.completionBar.setStringPainted(true);
      this.completionBar.setVisible(false);
   }

   public void setPercentageSent(Double var1) {
      if (var1 == null) {
         this.completionBar.setVisible(false);
      } else {
         this.completionBar.setVisible(true);
         this.completionBar.setValue((int)(var1 * 10.0));
         DecimalFormat var2 = new DecimalFormat("0.0");
         this.completionBar.setString(var2.format(var1) + "% of the file was sent");
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.completionBar);
   }

   @Override
   public void requestFocus() {
      this.completionBar.requestFocusInWindow();
   }
}
