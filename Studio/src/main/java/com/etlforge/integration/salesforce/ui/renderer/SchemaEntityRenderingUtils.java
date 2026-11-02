package com.etlforge.integration.salesforce.ui.renderer;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import javax.swing.JLabel;
import org.etlforge.integration.salesforce.wsdl.SchemaEntity;

public final class SchemaEntityRenderingUtils {
   public static void decorate(JLabel var0, SchemaEntity var1, NameDisplayMode var2) {
      String var3 = var2.getDisplayName(var1);
      var0.setText(var3);
      String var4 = var2.getToolTip(var1);
      if (var4.equals(var3)) {
         var0.setToolTipText(null);
      } else {
         var0.setToolTipText(var4);
      }
   }

   private SchemaEntityRenderingUtils() {
   }
}
