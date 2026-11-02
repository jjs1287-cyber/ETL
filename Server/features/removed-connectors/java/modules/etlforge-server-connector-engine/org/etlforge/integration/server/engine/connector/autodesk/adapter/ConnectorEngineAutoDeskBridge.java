package org.etlforge.integration.server.engine.connector.autodesk.adapter;

import java.util.List;
import org.etlforge.integration.etlforgeconnector.AutoDeskConnectorWorkspace;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskConnection;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskCreateOperation;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskDeleteOperation;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskGetFunctions;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskGetOperation;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskMetaField;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskUpdateOperation;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskUpsertOperation;
import org.etlforge.integration.server.engine.connector.autodesk.rest.AutoDeskWorkspace;

public class ConnectorEngineAutoDeskBridge {
   private final AutoDeskConnection autodeskConnection;

   public ConnectorEngineAutoDeskBridge(AutoDeskConnection var1) {
      this.autodeskConnection = var1;
   }

   private void open() {
      this.autodeskConnection.open();
   }

   private void close() {
      this.autodeskConnection.close();
   }

   public String getAutoDeskItemVersionId(String var1, String var2) {
      try {
         this.open();
         AutoDeskGetFunctions var3 = new AutoDeskGetFunctions(this.autodeskConnection.getAutoDeskEndpoint(), var1);
         return var3.getItemVersionId(var2);
      } finally {
         this.close();
      }
   }

   List<AutoDeskConnectorWorkspace> getWorkspaceListList() {
      try {
         this.open();
         return AutoDeskWorkspace.getAutoDeskWorkspaces(this.autodeskConnection.getAutoDeskEndpoint());
      } finally {
         this.close();
      }
   }

   List<AutoDeskMetaField> getMetafields(String var1) {
      try {
         this.open();
         return AutoDeskMetaField.getAutoDeskMetaFields(this.autodeskConnection.getAutoDeskEndpoint(), var1);
      } finally {
         this.close();
      }
   }

   public void executeConnectorFileUtf8_GetOperation(String var1, String var2, String var3) {
      try {
         this.open();
         AutoDeskGetOperation.executeGetOperation(this.autodeskConnection.getAutoDeskEndpoint(), var1, var2, var3);
      } finally {
         this.close();
      }
   }

   public void executeConnectorFileUtf8_DeleteOperation(String var1, String var2, String var3) {
      try {
         this.open();
         AutoDeskDeleteOperation.executeDeleteOperation(this.autodeskConnection.getAutoDeskEndpoint(), var1, var2, var3);
      } finally {
         this.close();
      }
   }

   public void executeConnectorFileUtf8_CreateOrUpdateOperation(boolean var1, String var2, String var3, String var4) {
      try {
         this.open();
         if (var1) {
            AutoDeskUpdateOperation.executeUpdateOperation(this.autodeskConnection.getAutoDeskEndpoint(), var2, var3, var4);
         } else {
            AutoDeskCreateOperation.executeCreateOperation(this.autodeskConnection.getAutoDeskEndpoint(), var2, var3, var4);
         }
      } finally {
         this.close();
      }
   }

   public void executeConnectorFileUtf8_UpsertOperation(String var1, String var2, String var3) {
      try {
         this.open();
         AutoDeskUpsertOperation.executeUpsertOperation(this.autodeskConnection.getAutoDeskEndpoint(), var1, var2, var3);
      } finally {
         this.close();
      }
   }
}
