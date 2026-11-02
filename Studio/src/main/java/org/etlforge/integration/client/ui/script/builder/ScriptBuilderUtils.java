package org.etlforge.integration.client.ui.script.builder;

import javax.swing.JTree;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.style.TextStyle;

public final class ScriptBuilderUtils {
   public static final void setTreeStyle(JTree var0) {
      TextStyle var1 = TextStyle.adjustSize(TextStyles.TreeLeafText, 1);
      var1.makeOver(var0);
   }

   private ScriptBuilderUtils() {
   }
}
