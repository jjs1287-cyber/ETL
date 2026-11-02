package org.etlforge.integration.client.project.caseconverter;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Element;

final class SqlScriptConverter {
   private static final String SELECT = "SELECT * FROM ";
   private final DbSchemaTransform transform;
   private final DbFieldsConverter fieldsConverter;

   public SqlScriptConverter(DbSchemaTransform var1) {
      this.transform = var1;
      this.fieldsConverter = new DbFieldsConverter(var1);
   }

   public void convertSql(Element var1) throws Exception {
      for (Element var3 : Elements.under(var1)) {
         String var4 = var3.getAttribute("type");
         if (String.valueOf(1).equals(var4)) {
            this.convertSqlStatement(var3);
         } else if (String.valueOf(2).equals(var4)) {
            this.convertFieldList(var3);
         }
      }
   }

   private void convertSqlStatement(Element var1) {
      Element var2 = this.getKongaStringElement(var1);
      String var3 = this.convertSelectQuery(KongaXmlUtils.getElementValue(var2));
      var2.setTextContent(var3);
   }

   private void convertFieldList(Element var1) {
      Element var2 = this.getKongaStringElement(var1);
      String var3 = KongaXmlUtils.getElementValue(var2);
      String var4 = this.fieldsConverter.convertFields(var3);
      var2.setTextContent(var4);
   }

   private Element getKongaStringElement(Element var1) {
      return KongaXmlUtils.getFirstElementChildOfName(var1, "konga.string");
   }

   String convertSelectQuery(String var1) {
      String var2 = this.getTableNameFromInput(var1);
      BeginEndQuote var3 = this.transform.getNewQuotes();
      String var4 = var3.escapeName(var2);
      String var5 = this.transform.getNewSchemaName();
      if (StringUtils.isNotEmpty(var5)) {
         var4 = var3.escapeName(var5) + "." + var4;
      }

      var4 = this.transform.convertCase(var4);
      return "SELECT * FROM " + var4;
   }

   private String getTableNameFromInput(String var1) {
      String var2 = StringUtils.substringAfter(var1, "SELECT * FROM ");
      BeginEndQuote var3 = this.transform.getOldQuotes();
      String var4 = var3.begin + StringUtils.substringBetween(var2, var3.begin, var3.end) + var3.end;
      if (var2.equals(var4)) {
         return var3.unescapeName(var4);
      }

      String var5 = StringUtils.substringAfter(var2, var4);
      var5 = StringUtils.removeStart(var5, ".");
      return var3.unescapeName(var5);
   }
}
