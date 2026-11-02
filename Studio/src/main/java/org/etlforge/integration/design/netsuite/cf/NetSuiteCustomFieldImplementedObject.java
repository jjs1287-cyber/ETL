package org.etlforge.integration.design.netsuite.cf;

import java.io.Serializable;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;

public class NetSuiteCustomFieldImplementedObject implements Serializable {
   public static final String customFieldListComplexTypeName = "CustomFieldList";
   public static final String customFieldListElementName = "customFieldList";
   public final String objectType;
   public final String objectTypeColumn;

   public NetSuiteCustomFieldImplementedObject(String var1, String var2) {
      this.objectType = var1;
      this.objectTypeColumn = var2;
   }

   public String getObjectTypeName() {
      return this.objectType;
   }

   public String getObjectTypeColumnName() {
      return this.objectTypeColumn;
   }

   public boolean hasColumnCustomField() {
      return this.objectTypeColumn != null;
   }

   public static String getCustomFieldListComplexTypeNamespace(NetSuiteDefinition var0) {
      return var0.getConnectorNetSuiteNamespaces().getCoreNamespace();
   }
}
