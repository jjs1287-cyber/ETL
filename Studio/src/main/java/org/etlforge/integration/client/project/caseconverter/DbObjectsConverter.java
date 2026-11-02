package org.etlforge.integration.client.project.caseconverter;

import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Element;

final class DbObjectsConverter {
   private final DbSchemaTransform transform;

   public DbObjectsConverter(DbSchemaTransform var1) {
      this.transform = var1;
   }

   public void convert(Element var1) throws Exception {
      for (Element var3 : Elements.under(var1)) {
         Element var4 = this.findObjectElement(var3);
         this.updateTableNameAttribute(var4);
         this.updateSchemaNameAttribute(var4);

         for (Element var6 : Elements.under(var4)) {
            this.convertAttributeValues(var6, "name");
         }
      }
   }

   private Element findObjectElement(Element var1) throws Exception {
      Element var2 = KongaXmlUtils.getFirstElementChildOfName(var1, "Object");
      if (var2 == null) {
         throw new Exception("Item element without Object child.");
      } else {
         return var2;
      }
   }

   private void updateTableNameAttribute(Element var1) {
      String var2 = var1.getAttribute("name");
      String var3 = this.convertTableName(var2);
      var1.setAttribute("name", var3);
   }

   private void updateSchemaNameAttribute(Element var1) {
      String var2 = this.transform.getNewSchemaName();
      if (StringUtils.isEmpty(var2)) {
         var2 = "dbo";
      }

      var1.setAttribute("schema", var2);
   }

   private void convertAttributeValues(Element var1, String... var2) {
      for (String var6 : var2) {
         String var7 = this.transform.convertCase(var1.getAttribute(var6));
         if (var7 != null) {
            var1.setAttribute(var6, var7);
         }
      }
   }

   String convertTableName(String var1) {
      Pair var2 = this.transform.deriveOldSchemaNameAndTableName(var1);
      String var3 = (String)var2.second;
      return this.transform.transformTableName(var3);
   }
}
