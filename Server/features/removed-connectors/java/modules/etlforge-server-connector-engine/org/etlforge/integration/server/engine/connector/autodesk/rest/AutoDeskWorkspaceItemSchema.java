package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;

public class AutoDeskWorkspaceItemSchema {
   private static final String METAFIELDELEMENTPLACEHOLDERKEY = "KeyValuePairReplaceHolderElement";
   private static final String METAFIELDELEMENTPLACEHOLDER_PREFIX = "<xsd:element name=\"";
   private static final String METAFIELDELEMENTPLACEHOLDER_POSTFIX = "\" type=\"fieldDataType\" minOccurs=\"0\"></xsd:element>";
   private static final String METAFIELDELEMENTPLACEHOLDER = "<xsd:element name=\"KeyValuePairReplaceHolderElement\" type=\"fieldDataType\" minOccurs=\"0\"></xsd:element>";

   private static final String MetaFieldElementsJB(List<AutoDeskMetaField> var0) {
      StringBuilder var1 = new StringBuilder();

      for (AutoDeskMetaField var3 : var0) {
         var1.append("<xsd:element name=\"");
         var1.append(var3.getfieldID());
         var1.append("\" type=\"fieldDataType\" minOccurs=\"0\"></xsd:element>");
         var1.append('\n');
      }

      return var1.toString();
   }

   private static final String getSchema(String var0, List<AutoDeskMetaField> var1) {
      String var2 = MetaFieldElementsJB(var1);
      return var0.replace("<xsd:element name=\"KeyValuePairReplaceHolderElement\" type=\"fieldDataType\" minOccurs=\"0\"></xsd:element>", var2);
   }

   public static final String getStaticSchema(String var0) {
      File var1 = ConnectorExecutionSystem.getDataInterchangeDir();
      var1 = new File(var1, "xml_dtd/Schemas/Connectors");
      File var2 = new File(var1, var0);
      return ConnectorExecutionSystem.readFileUTF8(var2);
   }

   public static final String getSchema(List<AutoDeskMetaField> var0) {
      String var2 = getStaticSchema("AutoDeskItemsResource_get.xsd");
      return getSchema(var2, var0);
   }

   public static void main(String[] var0) {
      ArrayList var1 = Lists.newArrayList();
      String var2 = getSchema(var1);
      String var3 = "Z:/etlforge/connectors/etlforgeconnectorsample/AUTODESK/Get/test.xsd";
      ConnectorExecutionSystem.writeFileUTF8(var3, var2);
   }
}
