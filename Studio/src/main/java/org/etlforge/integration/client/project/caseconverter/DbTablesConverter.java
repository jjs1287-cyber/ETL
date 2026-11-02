package org.etlforge.integration.client.project.caseconverter;

import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Element;

final class DbTablesConverter {
   private final DbObjectsConverter worker;

   public DbTablesConverter(DbSchemaTransform var1) {
      this.worker = new DbObjectsConverter(var1);
   }

   public void convert(Element var1) {
      String var2 = KongaXmlUtils.getElementValue(var1);
      String var3 = this.convertDbTablesField(var2);
      var1.setTextContent(var3);
   }

   String convertDbTablesField(String var1) {
      StringBuilder var2 = new StringBuilder();
      String[] var3 = var1.split(",");

      for (String var7 : var3) {
         String var8 = this.worker.convertTableName(var7);
         var2.append(var8).append(",");
      }

      return var2.toString();
   }
}
