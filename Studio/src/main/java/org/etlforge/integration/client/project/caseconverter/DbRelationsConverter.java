package org.etlforge.integration.client.project.caseconverter;

import org.etlforge.xml.Elements;
import org.w3c.dom.Element;

final class DbRelationsConverter {
   private final DbSchemaTransform transform;
   private final DbObjectsConverter tableNameConverter;

   public DbRelationsConverter(DbSchemaTransform var1) {
      this.transform = var1;
      this.tableNameConverter = new DbObjectsConverter(var1);
   }

   public void convertRelations(Element var1) {
      for (Element var3 : Elements.under(var1)) {
         this.convertTableNames(var3, "parentTable", "childTable");
         this.convertColumnNames(var3, "parentField", "childField");
      }
   }

   private void convertTableNames(Element var1, String... var2) {
      for (String var6 : var2) {
         String var7 = this.tableNameConverter.convertTableName(var1.getAttribute(var6));
         var1.setAttribute(var6, var7);
      }
   }

   private void convertColumnNames(Element var1, String... var2) {
      for (String var6 : var2) {
         String var7 = this.transform.convertCase(var1.getAttribute(var6));
         var1.setAttribute(var6, var7);
      }
   }
}
