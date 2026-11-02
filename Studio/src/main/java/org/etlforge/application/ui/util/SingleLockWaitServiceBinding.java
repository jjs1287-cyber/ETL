package org.etlforge.application.ui.util;

import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.wait.SingleLockWaitService;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class SingleLockWaitServiceBinding extends AbstractBinding<Boolean> {
   private SingleLockWaitService waitService;

   public SingleLockWaitServiceBinding(Property<Boolean> var1) {
      this(var1, null);
   }

   public SingleLockWaitServiceBinding(AbstractPropertyBasedModel var1, String var2) {
      this(var1, var2, null);
   }

   public SingleLockWaitServiceBinding(AbstractPropertyBasedModel var1, String var2, WaitService var3) {
      this(getProperty(var1, var2), var3);
   }

   private static Property<Boolean> getProperty(AbstractPropertyBasedModel var0, String var1) {
      return var0.getProperty(var1);
   }

   public SingleLockWaitServiceBinding(Property<Boolean> var1, WaitService var2) {
      super(var1);
      if (var2 == null) {
         var2 = WaitService.NONE;
      }

      this.waitService = new SingleLockWaitService(var2);
   }

   protected void updateUiWithValue(Boolean var1) {
      this.waitService.setWaiting(var1);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = new SingleLockWaitService(var1);
   }
}
