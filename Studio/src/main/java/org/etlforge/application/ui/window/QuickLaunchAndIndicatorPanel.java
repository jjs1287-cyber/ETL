package org.etlforge.application.ui.window;

import org.etlforge.application.ui.look.Colors;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;

public class QuickLaunchAndIndicatorPanel implements UiProvider {
   private RunningProcessProgressBar processIndicator;
   private final QuickLauncher[] launchers;

   public QuickLaunchAndIndicatorPanel(QuickLauncher... var1) {
      this.launchers = (QuickLauncher[])var1.clone();
   }

   public void setProcessIndicator(RunningProcessProgressBar var1) {
      this.processIndicator = var1;
   }

   public void publish() {
      if (this.processIndicator != null) {
         this.processIndicator.publish();
      }

      for (QuickLauncher var4 : this.launchers) {
         var4.publish();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.withBackground(Colors.NEUTRAL_BACKGROUND);
      var2.add(this.processIndicator);

      for (QuickLauncher var6 : this.launchers) {
         var2.add(var6);
      }

      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }
}
