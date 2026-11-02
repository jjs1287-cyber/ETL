package org.etlforge.integration.etlforgeconnector.netsuite;

import java.util.List;
import org.etlforge.integration.design.util.XmlElementUtils;

public class NetSuiteSearchAdvancedPropertiesFormatter {
   static final String ADVANCEDPROPERTY = "advancedproperty";
   private static final String SEARCHTYPE = "searchtype";
   private static final String CUSTOMSEARCHOBJECTS = "customsearchobjects";
   private static final String NAME = "name";
   private final NetSuiteSearchAdvancedProperties advancedProperties;
   private final StringBuilder sb;

   NetSuiteSearchAdvancedPropertiesFormatter(NetSuiteSearchAdvancedProperties var1) {
      this.advancedProperties = var1;
      this.sb = new StringBuilder();
   }

   String toXml() {
      if (this.advancedProperties.hasNoValue()) {
         return "";
      }

      XmlElementUtils.startElementTag(this.sb, "advancedproperty");
      XmlElementUtils.elementValue(this.sb, "searchtype", this.advancedProperties.getSearchType().name());
      this.appendCustomSearchObjects(this.advancedProperties.getCustomSearchObjects());
      NetSuiteSearchRowPropertyiesFormatter var1 = new NetSuiteSearchRowPropertyiesFormatter(this.advancedProperties.getNetSuiteSearchRowPropertyies());
      this.sb.append(var1.toXml());
      XmlElementUtils.endElementTag(this.sb, "advancedproperty");
      return this.sb.toString();
   }

   private void appendCustomSearchObjects(List<String> var1) {
      if (!var1.isEmpty()) {
         XmlElementUtils.startElementTag(this.sb, "customsearchobjects");

         for (String var3 : var1) {
            XmlElementUtils.elementValue(this.sb, "name", var3);
         }

         XmlElementUtils.endElementTag(this.sb, "customsearchobjects");
      }
   }
}
