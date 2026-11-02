package org.etlforge.ui.wait;

import java.awt.Component;
import javax.swing.RootPaneContainer;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.WidgetUtils;

public class WindowWaitService extends AbstractWaitService implements WaitService {
   private final WindowWaitService.GlassPaneWaiter delegate;

   public WindowWaitService(RootPaneContainer var1) {
      this.delegate = new WindowWaitService.GlassPaneWaiter(var1);
   }

   @Override
   protected void goToWaitingState() {
      this.delegate.startWait();
   }

   @Override
   protected void stopWaiting() {
      this.delegate.stopWait();
   }

   private static class GlassPaneWaiter {
      private final Component glassPane;
      private final Runnable startWait;
      private final Runnable stopWait;

      public GlassPaneWaiter(RootPaneContainer var1) {
         this.glassPane = var1.getGlassPane();
         WidgetUtils.configureGlassPane(this.glassPane, true);
         this.startWait = new Runnable() {
            @Override
            public void run() {
               GlassPaneWaiter.this.glassPane.setVisible(true);
            }
         };
         this.stopWait = new Runnable() {
            @Override
            public void run() {
               GlassPaneWaiter.this.glassPane.setVisible(false);
            }
         };
      }

      public void startWait() {
         UiUtils.runOnEventThread(this.startWait);
      }

      public void stopWait() {
         UiUtils.runOnEventThread(this.stopWait);
      }
   }
}
