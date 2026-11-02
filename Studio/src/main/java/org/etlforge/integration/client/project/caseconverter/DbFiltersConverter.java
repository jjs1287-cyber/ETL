package org.etlforge.integration.client.project.caseconverter;

import org.etlforge.xml.Elements;
import org.w3c.dom.Element;

final class DbFiltersConverter {
   private static final String TABLE_ATTRIBUTE = "table";
   private static final String COLUMN_ATTRIBUTE = "column";
   private final DbSchemaTransform transform;
   private final DbObjectsConverter tableNameConverter;

   public DbFiltersConverter(DbSchemaTransform var1) {
      this.transform = var1;
      this.tableNameConverter = new DbObjectsConverter(var1);
   }

   public void convertFilters(Element var1) throws Exception {
      for (Element var3 : Elements.under(var1)) {
         String var4 = var3.getAttribute("table");
         String var5 = this.tableNameConverter.convertTableName(var4);
         var3.setAttribute("table", var5);
         String var6 = var3.getAttribute("column");
         String var7 = this.transform.convertCase(var6);
         var3.setAttribute("column", var7);
      }
   }
}
