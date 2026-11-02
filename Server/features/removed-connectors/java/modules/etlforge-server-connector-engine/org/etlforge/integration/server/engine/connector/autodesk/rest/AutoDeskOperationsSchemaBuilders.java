package org.etlforge.integration.server.engine.connector.autodesk.rest;

import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;

public class AutoDeskOperationsSchemaBuilders {
   static final String deleteRequestSchema = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\t<xs:element name=\"deleteitem\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"dmsID\" type=\"xs:int\" minOccurs=\"0\" />\t<xs:element name=\"undelete\" type=\"xs:boolean\" minOccurs=\"0\" />\t</xs:sequence></xs:complexType>  </xs:element></xs:schema>";
   static final String resultResponseRoot = "etlforgeresult";
   static final String resultResponseSchema = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><xs:element name=\"etlforgeresult\"><xs:complexType><xs:sequence>  <xs:element name=\"action\" type=\"xs:string\" />  <xs:element name=\"success\" type=\"xs:boolean\" />  <xs:element name=\"message\" type=\"xs:string\" minOccurs=\"0\" />  <xs:element name=\"statuscode\" type=\"xs:int\" minOccurs=\"0\" />  <xs:element name=\"statusline\" type=\"xs:string\" minOccurs=\"0\" />  <xs:element name=\"dmsID\" type=\"xs:int\" minOccurs=\"0\" />  <xs:element name=\"workspaceID\" type=\"xs:int\" minOccurs=\"0\" /></xs:sequence></xs:complexType></xs:element></xs:schema>";
   public static final String getRequestRoot = "GetListSetting";
   public static final String getRequestSchema = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\t<xs:element name=\"GetListSetting\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"ListSetting\" minOccurs=\"0\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"Page\" type=\"xs:int\" minOccurs=\"0\" />\t<xs:element name=\"Size\" type=\"xs:int\" minOccurs=\"0\" />\t<xs:element name=\"FilterDeleted\" type=\"xs:boolean\" minOccurs=\"0\" />\t</xs:sequence></xs:complexType>  </xs:element>\t<xs:element name=\"SigleItem\" minOccurs=\"0\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"dmsID\" type=\"xs:int\" />\t</xs:sequence></xs:complexType>  </xs:element>\t<xs:element name=\"ItemsByAlias\" minOccurs=\"0\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"alias\" type=\"xs:string\" />\t</xs:sequence></xs:complexType>  </xs:element>\t</xs:sequence></xs:complexType>  </xs:element></xs:schema>";

   public static ConnectorXsdSchemaFiles deleteOperationXsdSchemas() {
      return new ConnectorXsdSchemaFiles(
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\t<xs:element name=\"deleteitem\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"dmsID\" type=\"xs:int\" minOccurs=\"0\" />\t<xs:element name=\"undelete\" type=\"xs:boolean\" minOccurs=\"0\" />\t</xs:sequence></xs:complexType>  </xs:element></xs:schema>",
         "deleteitem",
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><xs:element name=\"etlforgeresult\"><xs:complexType><xs:sequence>  <xs:element name=\"action\" type=\"xs:string\" />  <xs:element name=\"success\" type=\"xs:boolean\" />  <xs:element name=\"message\" type=\"xs:string\" minOccurs=\"0\" />  <xs:element name=\"statuscode\" type=\"xs:int\" minOccurs=\"0\" />  <xs:element name=\"statusline\" type=\"xs:string\" minOccurs=\"0\" />  <xs:element name=\"dmsID\" type=\"xs:int\" minOccurs=\"0\" />  <xs:element name=\"workspaceID\" type=\"xs:int\" minOccurs=\"0\" /></xs:sequence></xs:complexType></xs:element></xs:schema>",
         "etlforgeresult"
      );
   }

   public static ConnectorXsdSchemaFiles createandupdateOperationXsdSchemas(EntityType var0, List<AutoDeskMetaField> var1) {
      boolean var2 = var0 == EntityType.AutoDeskUpdate;
      boolean var3 = var0 == EntityType.AutoDeskCreate;
      String var4 = getCreateandUpdateRequestSchema(var2, var1);
      if (var3) {
         String var5 = AutoDeskWorkspaceItemSchema.getSchema(var1);
         return new ConnectorXsdSchemaFiles(var4, "item", var5, "etlforgeresult");
      } else {
         return new ConnectorXsdSchemaFiles(
            var4,
            "item",
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><xs:element name=\"etlforgeresult\"><xs:complexType><xs:sequence>  <xs:element name=\"action\" type=\"xs:string\" />  <xs:element name=\"success\" type=\"xs:boolean\" />  <xs:element name=\"message\" type=\"xs:string\" minOccurs=\"0\" />  <xs:element name=\"statuscode\" type=\"xs:int\" minOccurs=\"0\" />  <xs:element name=\"statusline\" type=\"xs:string\" minOccurs=\"0\" />  <xs:element name=\"dmsID\" type=\"xs:int\" minOccurs=\"0\" />  <xs:element name=\"workspaceID\" type=\"xs:int\" minOccurs=\"0\" /></xs:sequence></xs:complexType></xs:element></xs:schema>",
            "etlforgeresult"
         );
      }
   }

   static String getJbElementDefForKeyValue(String var0) {
      return "<xs:element name=\"" + var0 + "\" type=\"xs:string\" minOccurs=\"0\" />";
   }

   static String getCreateandUpdateRequestSchema(boolean var0, List<AutoDeskMetaField> var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> ");
      var2.append("<xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">");
      var2.append(" <xs:element name=\"item\" type=\"itemType_request_jb\"/>");
      var2.append("\t<xs:complexType name=\"itemType_request_jb\">");
      var2.append("\t\t<xs:sequence>");
      if (var0) {
         var2.append("\t<xs:element name=\"dmsID\" type=\"xs:string\" />");
      }

      var2.append("\t\t\t<xs:element name=\"alias\" type=\"xs:string\" minOccurs=\"0\" />");
      var2.append("\t\t\t<xs:element name=\"versionID\" type=\"xs:int\" minOccurs=\"0\" />");
      var2.append("\t\t\t<xs:element name=\"metaFields\" minOccurs=\"0\" >");
      var2.append("\t\t\t\t<xs:complexType>");
      var2.append("\t\t\t\t\t<xs:sequence>");

      for (AutoDeskMetaField var4 : var1) {
         var2.append(getJbElementDefForKeyValue(var4.getfieldID()));
      }

      var2.append("\t\t\t\t\t</xs:sequence>");
      var2.append("\t\t\t\t</xs:complexType>");
      var2.append("\t\t\t</xs:element>");
      var2.append("\t\t</xs:sequence>");
      var2.append("\t</xs:complexType>");
      var2.append("</xs:schema>");
      return var2.toString();
   }
}
