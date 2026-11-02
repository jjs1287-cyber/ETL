package org.etlforge.ui.text;

import java.awt.Component;
import javax.swing.text.JTextComponent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.Enablable;

public class NonEmptyTextEnablerCondition implements TextEnablerCondition {
   private final boolean trim;

   public static Disposable enableComponentWhenFieldNotEmpty(Component var0, JTextComponent var1) {
      return enableWhenFieldNotEmpty(Enablabes.fromComponent(var0), var1);
   }

   public static Disposable enableWhenFieldNotEmpty(Enablable var0, JTextComponent var1) {
      NonEmptyTextEnablerCondition var2 = new NonEmptyTextEnablerCondition(true);
      final ConditionalTextEnabler var3 = ConditionalTextEnabler.install(var1);
      var3.addDependency(var0, var2);
      return new Disposable() {
         @Override
         public void dispose() {
            var3.uninstall();
         }
      };
   }

   public NonEmptyTextEnablerCondition() {
      this(false);
   }

   public NonEmptyTextEnablerCondition(boolean var1) {
      this.trim = var1;
   }

   @Override
   public boolean isEnabled(TextContent var1) {
      int var2 = this.trim ? var1.getText().trim().length() : var1.getLength();
      return var2 > 0;
   }
}
