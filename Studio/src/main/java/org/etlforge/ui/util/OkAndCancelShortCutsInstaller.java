package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import javax.swing.Action;
import javax.swing.JComponent;

public final class OkAndCancelShortCutsInstaller {
   private final KeyBindingInstaller bindings;
   private Object okKey = "OK";
   private Object cancelKey = "Cancel";

   public OkAndCancelShortCutsInstaller(JComponent var1, int var2) {
      this.bindings = new KeyBindingInstaller(var1, var2);
   }

   public void install(Action var1, Action var2) {
      if (var1 != null) {
         this.bindings.addBinding(KeyUtils.ENTER, this.okKey, var1);
      }

      if (var2 != null) {
         this.bindings.addBinding(KeyUtils.ESCAPE, this.cancelKey, var2);
      }

      this.bindings.install();
   }

   public void setOkIdentifier(Object var1) {
      Preconditions.checkNotNull(var1, "key");
      this.okKey = var1;
   }

   public void setCancelIdentifier(Object var1) {
      Preconditions.checkNotNull(var1, "key");
      this.cancelKey = var1;
   }
}
