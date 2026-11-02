package org.etlforge.application.ui.widget.password;

import com.google.common.base.Supplier;
import org.etlforge.ui.UiThreadSupplier;

public class DialogPasswordSupplier implements Supplier<String> {
   private final String caption;
   private final boolean includeReTypeField;

   public DialogPasswordSupplier(String var1, boolean var2) {
      this.caption = var1;
      this.includeReTypeField = var2;
   }

   public String get() {
      UiThreadSupplier var1 = UiThreadSupplier.wrap(new Supplier<String>() {
         public String get() {
            char[] var1 = PasswordDialog.definePassword(DialogPasswordSupplier.this.caption, DialogPasswordSupplier.this.includeReTypeField);
            return var1 != null ? new String(var1) : null;
         }
      });
      return (String)var1.get();
   }
}
