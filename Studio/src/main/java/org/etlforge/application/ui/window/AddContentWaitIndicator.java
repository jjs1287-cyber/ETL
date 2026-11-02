package org.etlforge.application.ui.window;

import com.google.common.base.Preconditions;
import org.etlforge.ui.wait.WaitLock;

public class AddContentWaitIndicator implements AddContentCallback {
   private final WaitLock lock;

   public AddContentWaitIndicator(WaitLock var1) {
      Preconditions.checkNotNull(var1, "lock");
      this.lock = var1;
   }

   public AddContentWaitIndicator(ApplicationWindow var1) {
      this(var1.startWait());
   }

   @Override
   public void cancelled() {
      this.lock.release();
   }

   @Override
   public void contentAdded(WindowSection var1, WindowSectionContent var2) {
      this.lock.release();
   }

   @Override
   public void failed(Exception var1) {
      this.lock.release();
   }
}
