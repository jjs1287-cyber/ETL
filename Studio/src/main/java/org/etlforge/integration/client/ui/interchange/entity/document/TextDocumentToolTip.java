package org.etlforge.integration.client.ui.interchange.entity.document;

import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class TextDocumentToolTip extends IntegrationEntityToolTip {
   public TextDocumentToolTip(TextDocument var1) {
      super(var1);
      this.addEntry("Type:", getTypeString(var1));
   }

   private static String getTypeString(TextDocument var0) {
      StringBuilder var1 = new StringBuilder();
      if (var0.isSimpleDoc()) {
         var1.append("Simple");
      } else {
         var1.append("Complex");
      }

      var1.append(" / ");
      if (var0.isFixedField()) {
         var1.append("Fixed Width");
      } else {
         var1.append("Delimited");
      }

      return var1.toString();
   }
}
