package org.etlforge.application.ui.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.window.ApplicationWindow;

public abstract class ApplicationWindowWizard<T> extends Wizard<T> {
   private final ApplicationWindow appWin;

   protected ApplicationWindowWizard(ApplicationWindow var1) {
      this.appWin = Preconditions.checkNotNull(var1, "appWin");
      this.setWaitService(var1);
   }

   public final ApplicationWindow getWindow() {
      return this.appWin;
   }
}
