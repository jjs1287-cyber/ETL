package org.etlforge.integration.client.project.caseconverter;

import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Element;

final class TransformValidator {
   private final DbSchemaTransform transform;

   public TransformValidator(DbSchemaTransform var1) {
      this.transform = var1;
   }

   public boolean isValid(Element var1) {
      for (Element var3 : Elements.under(var1)) {
         String var4 = var3.getNodeName();
         if ("konga.string".equals(var4) && "DbTables".equals(var3.getAttribute("name"))) {
            return this.checkDbTables(var3);
         }
      }

      return true;
   }

   private boolean checkDbTables(Element var1) {
      String var2 = KongaXmlUtils.getElementValue(var1);
      return this.isValidTransform(var2);
   }

   boolean isValidTransform(String var1) {
      BeginEndQuote var2 = this.transform.getOldQuotes();
      String[] var3 = var1.split(",");

      for (String var7 : var3) {
         int var8 = var7.indexOf(46);
         if (var8 == -1) {
            if (var2.equals(BeginEndQuote.ORACLE)) {
               return false;
            }
         } else {
            BeginEndQuote var9 = this.deriveBeginEndQuoteFromTableString(var7);
            if (!var2.equals(var9)) {
               return false;
            }
         }
      }

      return true;
   }

   private BeginEndQuote deriveBeginEndQuoteFromTableString(String var1) {
      char var2 = var1.charAt(0);
      char var3 = var1.charAt(var1.length() - 1);
      return BeginEndQuote.fromStrings(String.valueOf(var2), String.valueOf(var3));
   }
}
