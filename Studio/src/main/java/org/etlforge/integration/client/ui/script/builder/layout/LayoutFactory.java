package org.etlforge.integration.client.ui.script.builder.layout;

import org.etlforge.integration.client.ui.script.builder.ExpressionArea;
import org.etlforge.integration.client.ui.script.builder.ExpressionAreaStatusPanel;
import org.etlforge.integration.client.ui.script.prefs.LayoutTypePreference;

public final class LayoutFactory {
   public static ScriptBuilderLayout createPreferredLayout(ExpressionArea var0, ExpressionAreaStatusPanel var1) {
      LayoutType var2 = LayoutTypePreference.get();
      switch (var2) {
         case STACK:
            return new StackBasedLayout(var0, var1);
         case BUTTONS:
            return new ButtonBasedLayout(var0, var1);
         case TABS:
         default:
            return new TabbedBasedLayout(var0, var1);
      }
   }

   private LayoutFactory() {
   }
}
