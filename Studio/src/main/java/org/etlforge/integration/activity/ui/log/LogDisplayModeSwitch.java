package org.etlforge.integration.activity.ui.log;

import org.etlforge.integration.activity.ui.AbstractOperationLogModel;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.Orientation;
import org.etlforge.ui.util.option.ExclusiveOptionsPanel;
import org.etlforge.ui.util.option.Option;
import org.etlforge.ui.util.option.SelectedOptionListener;

final class LogDisplayModeSwitch implements UiProvider {
   private final ExclusiveOptionsPanel<LogDisplayMode> buttons;

   public LogDisplayModeSwitch(final AbstractOperationLogModel var1) {
      this.buttons = new ExclusiveOptionsPanel<>(LogDisplayMode.values(), var1.getDisplayMode(), Orientation.HORIZONTAL);
      this.addCallback(new Receiver<LogDisplayMode>() {
         public void handle(LogDisplayMode var1x) {
            var1.setDisplayMode(var1x);
         }
      });
   }

   private void addCallback(final Receiver<LogDisplayMode> var1) {
      this.buttons.addSelectionListener(new SelectedOptionListener() {
         @Override
         public void selectionChanged(Option[] var1x) {
            var1.handle((LogDisplayMode)var1x[0]);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.buttons.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.buttons.requestFocus();
   }
}
