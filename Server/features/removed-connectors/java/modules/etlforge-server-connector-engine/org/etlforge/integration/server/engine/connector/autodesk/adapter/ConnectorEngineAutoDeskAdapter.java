package org.etlforge.integration.server.engine.connector.autodesk.adapter;

import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.etlforgeconnector.AutoDeskConnectorWorkspace;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskOperationsSchemaBuilders;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskWorkspaceItemSchema;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;

public class ConnectorEngineAutoDeskAdapter {
   private final ConnectorEngineAutoDeskBridge bridge;

   public ConnectorEngineAutoDeskAdapter(ConnectorEngineAutoDeskBridge var1) {
      this.bridge = var1;
   }

   public String getAutoDeskItemVersionId(String var1, String var2) {
      return this.bridge.getAutoDeskItemVersionId(var1, var2);
   }

   public ConnectorObjectList getConnectorObjectList_workspace() {
      ConnectorObjectList var1 = new ConnectorObjectList();

      for (AutoDeskConnectorWorkspace var4 : this.bridge.getWorkspaceListList()) {
         var1.addConnectorObject(var4.toWorkspaceKey(), "");
      }

      return var1;
   }

   private ConnectorXsdSchemaFiles getOperationXsdSchemas(String var1) {
      List var2 = this.bridge.getMetafields(var1);
      String var3 = AutoDeskWorkspaceItemSchema.getSchema(var2);
      return new ConnectorXsdSchemaFiles(
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <xs:schema version=\"1.0\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\t<xs:element name=\"GetListSetting\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"ListSetting\" minOccurs=\"0\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"Page\" type=\"xs:int\" minOccurs=\"0\" />\t<xs:element name=\"Size\" type=\"xs:int\" minOccurs=\"0\" />\t<xs:element name=\"FilterDeleted\" type=\"xs:boolean\" minOccurs=\"0\" />\t</xs:sequence></xs:complexType>  </xs:element>\t<xs:element name=\"SigleItem\" minOccurs=\"0\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"dmsID\" type=\"xs:int\" />\t</xs:sequence></xs:complexType>  </xs:element>\t<xs:element name=\"ItemsByAlias\" minOccurs=\"0\">\t<xs:complexType><xs:sequence>\t<xs:element name=\"alias\" type=\"xs:string\" />\t</xs:sequence></xs:complexType>  </xs:element>\t</xs:sequence></xs:complexType>  </xs:element></xs:schema>",
         "GetListSetting",
         var3,
         "payload"
      );
   }

   private ConnectorXsdSchemaFiles createandupdateOperationXsdSchemas(EntityType var1, String var2) {
      List var3 = this.bridge.getMetafields(var2);
      return AutoDeskOperationsSchemaBuilders.createandupdateOperationXsdSchemas(var1, var3);
   }

   public ConnectorXsdSchemaFiles getConnectorXsdSchemas(EntityType var1, String var2) {
      switch (var1) {
         case AutoDeskGet:
            return this.getOperationXsdSchemas(var2);
         case AutoDeskDelete:
            return AutoDeskOperationsSchemaBuilders.deleteOperationXsdSchemas();
         case AutoDeskUpdate:
         case AutoDeskCreate:
         case AutoDeskUpsert:
            return this.createandupdateOperationXsdSchemas(var1, var2);
         default:
            throw new ConnectorServerNotSupportedRuntimeException("autodesk operation " + var1.toString() + " not implemented!");
      }
   }

   public void executeConnectorFileUtf8(EntityType var1, String var2, String var3, String var4) {
      switch (var1) {
         case AutoDeskGet:
            this.bridge.executeConnectorFileUtf8_GetOperation(var2, var3, var4);
            break;
         case AutoDeskDelete:
            this.bridge.executeConnectorFileUtf8_DeleteOperation(var2, var3, var4);
            break;
         case AutoDeskUpdate:
            this.bridge.executeConnectorFileUtf8_CreateOrUpdateOperation(true, var2, var3, var4);
            break;
         case AutoDeskCreate:
            this.bridge.executeConnectorFileUtf8_CreateOrUpdateOperation(false, var2, var3, var4);
            break;
         case AutoDeskUpsert:
            this.bridge.executeConnectorFileUtf8_UpsertOperation(var2, var3, var4);
            break;
         default:
            throw new ConnectorServerNotSupportedRuntimeException("autodesk operation " + var1.toString() + " not implemented!");
      }
   }
}
