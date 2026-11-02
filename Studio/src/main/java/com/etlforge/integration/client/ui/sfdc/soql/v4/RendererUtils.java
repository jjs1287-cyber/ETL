package com.etlforge.integration.client.ui.sfdc.soql.v4;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.util.Fonts;

final class RendererUtils {
   public static void renderField(SObject.Field var0, JComponent var1, JLabel var2, boolean var3) {
      Font var4 = var1.getFont();
      if (!var0.isSimpleType()) {
         var4 = Fonts.italic(var4);
      }

      var2.setFont(var4);
      if (!var3) {
         Color var5;
         if (var0.isSimpleType()) {
            var5 = Color.BLACK;
         } else {
            var5 = Color.DARK_GRAY;
         }

         var2.setForeground(var5);
      }
   }

   private RendererUtils() {
   }
}
