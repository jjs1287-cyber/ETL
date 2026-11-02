package org.etlforge.integration.server.engine.connector.netsuite.config;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldType;
import org.etlforge.integration.design.util.XmlElementUtils;

public class NetSuiteCustomFieldTypeFormat {
   private final List<NetSuiteCustomFieldType> types;

   NetSuiteCustomFieldTypeFormat(List<NetSuiteCustomFieldType> var1) {
      this.types = var1;
   }

   void format(StringBuilder var1) {
      Collections.sort(this.types);

      for (NetSuiteCustomFieldType var3 : this.types) {
         format(var1, var3);
      }
   }

   private static void format(StringBuilder var0, NetSuiteCustomFieldType var1) {
      XmlElementUtils.startElementTag(var0, "customfieldtype");
      XmlElementUtils.elementValue(var0, "type", var1.getFieldType());
      XmlElementUtils.elementValue(var0, "fieldtype", var1.getCustomFieldType());
      XmlElementUtils.elementValue(var0, "fieldtypesearch", var1.getCustomFieldTypeSearch());
      XmlElementUtils.elementValue(var0, "fieldtypesearchcolumn", var1.getCustomFieldTypeSearchColumn());
      XmlElementUtils.endElementTag(var0, "customfieldtype");
   }
}
