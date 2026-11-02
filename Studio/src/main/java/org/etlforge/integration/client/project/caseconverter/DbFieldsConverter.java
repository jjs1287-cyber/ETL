package org.etlforge.integration.client.project.caseconverter;

import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Element;

final class DbFieldsConverter {
   private final DbSchemaTransform transform;
   private final DbObjectsConverter tableNameConverter;

   public DbFieldsConverter(DbSchemaTransform var1) {
      this.transform = var1;
      this.tableNameConverter = new DbObjectsConverter(var1);
   }

   public void convert(Element var1) {
      for (Element var3 : Elements.under(var1)) {
         String var4 = var3.getAttribute("type");
         if (!String.valueOf(3).equals(var4) && !String.valueOf(1).equals(var4)) {
            if (String.valueOf(2).equals(var4)) {
            }
         } else {
            Element var5 = KongaXmlUtils.getFirstElementChildOfName(var3, "konga.string");
            String var6 = KongaXmlUtils.getElementValue(var5);
            String var7 = this.convertFields(var6);
            var5.setTextContent(var7);
         }
      }
   }

   String convertFields(String var1) {
      StringBuilder var2 = new StringBuilder();

      for (String var6 : var1.split(",")) {
         var2.append(this.convert(var6)).append(",");
      }

      return var2.toString();
   }

   private String convert(String var1) {
      int var2 = var1.lastIndexOf(46);
      String var3 = var1.substring(0, var2);
      String var4 = var1.substring(var2 + 1);
      return this.transform.convertCase(this.tableNameConverter.convertTableName(var3) + "." + var4);
   }
}
