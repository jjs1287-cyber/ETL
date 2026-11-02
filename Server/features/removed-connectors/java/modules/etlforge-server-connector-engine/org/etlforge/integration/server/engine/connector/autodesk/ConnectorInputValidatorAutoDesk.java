package org.etlforge.integration.server.engine.connector.autodesk;

import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.etlforgeconnector.AutoDeskConnectorWorkspace;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;

public class ConnectorInputValidatorAutoDesk {
   final EntityType operation;
   final String operationString;
   final String workspaceid;

   ConnectorInputValidatorAutoDesk(List<String> var1) {
      if (var1.size() > 2) {
         String var4 = "The list (" + var1.size() + ") has too many value! " + var1.toString();
         throw new ConnectorInvalidInputRuntimeException(var4);
      }

      this.operationString = var1.size() > 0 ? (String)var1.get(0) : "";
      this.operation = ConnectorAutoDeskOperation.getOperation(this.operationString);
      if (var1.size() == 2) {
         String var2 = (String)var1.get(1);
         AutoDeskConnectorWorkspace var3 = AutoDeskConnectorWorkspace.fromWorkspaceKey(var2);
         this.workspaceid = var3.getWorkspaceID();
         if (this.workspaceid.isEmpty()) {
            throw new ConnectorServerRuntimeException("unexpected autodesk workspace format " + var2);
         }
      } else {
         this.workspaceid = "";
      }
   }

   void validatecomplete() {
      this.checkEmpty(this.workspaceid, "Autodesk workspace id");
   }

   void checkEmpty(String var1, String var2) {
      if (var1 == null || var1.isEmpty()) {
         throw new ConnectorInvalidInputRuntimeException(var2 + " can not be empty");
      }
   }
}
