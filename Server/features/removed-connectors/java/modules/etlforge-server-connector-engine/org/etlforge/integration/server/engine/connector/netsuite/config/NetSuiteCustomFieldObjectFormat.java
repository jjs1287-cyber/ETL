package org.etlforge.integration.server.engine.connector.netsuite.config;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomFieldObject;
import org.etlforge.integration.design.util.XmlElementUtils;

public class NetSuiteCustomFieldObjectFormat {
   private final List<NetSuiteCustomFieldObject> customFieldObjects;

   NetSuiteCustomFieldObjectFormat(List<NetSuiteCustomFieldObject> var1) {
      this.customFieldObjects = var1;
   }

   void format(StringBuilder var1) {
      Collections.sort(this.customFieldObjects);

      for (NetSuiteCustomFieldObject var3 : this.customFieldObjects) {
         format(var1, var3);
      }
   }

   private static void format(StringBuilder var0, NetSuiteCustomFieldObject var1) {
      XmlElementUtils.startElementTag(var0, "customfieldobject");
      XmlElementUtils.elementValue(var0, "objectName", var1.getObjectName());
      XmlElementUtils.elementValue(var0, "getCustomizationType", var1.getGetCustomizationType());
      XmlElementUtils.elementValue(var0, "appliesToObject", var1.getAppliesToObject());
      if (var1.hasObjectColumn()) {
         XmlElementUtils.elementValue(var0, "appliesToObjectColumn", var1.getAppliesToObjectColumn());
         XmlElementUtils.elementValue(var0, "objectColumnName", var1.getObjectColumnName());
      }

      XmlElementUtils.endElementTag(var0, "customfieldobject");
   }
}
