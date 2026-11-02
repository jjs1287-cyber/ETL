package org.etlforge.ui.property;

import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public class WaitStateBinding extends AbstractBinding<Boolean> {
   private WaitService waitService;
   private WaitLock waitLock;

   public WaitStateBinding(Property<Boolean> var1) {
      this(var1, WaitService.NONE);
   }

   public WaitStateBinding(AbstractPropertyBasedModel var1, String var2) {
      this(var1.getProperty(var2), WaitService.NONE);
   }

   public WaitStateBinding(Property<Boolean> var1, WaitService var2) {
      super(var1);
      this.setWaitService(var2);
   }

   public WaitStateBinding(AbstractPropertyBasedModel var1, String var2, WaitService var3) {
      super(var1.getProperty(var2));
      this.setWaitService(var3);
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = var1 == null ? WaitService.NONE : var1;
      this.updateWaitState(this.getCurrentValue());
   }

   private boolean getCurrentValue() {
      Boolean var1 = this.property.get();
      return var1 == null ? false : var1;
   }

   protected final void updateUiWithValue(Boolean var1) {
      this.updateWaitState(var1);
      this.doAdditionalUiUpdate(var1);
   }

   private void updateWaitState(Boolean var1) {
      if (this.waitLock != null) {
         this.waitLock.release();
         this.waitLock = null;
      }

      if (var1) {
         this.waitLock = this.waitService.startWait();
      }
   }

   protected void doAdditionalUiUpdate(boolean var1) {
   }
}
