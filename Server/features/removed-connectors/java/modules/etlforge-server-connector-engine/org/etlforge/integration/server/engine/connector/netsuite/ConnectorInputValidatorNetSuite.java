package org.etlforge.integration.server.engine.connector.netsuite;

import com.google.common.base.Strings;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.design.netsuite.adapter.NetSuiteSelectedObject;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class ConnectorInputValidatorNetSuite {
   final EntityType operation;
   final String operationString;
   final String netSuiteObject;
   final String searchPropertyString;
   final String netSuiteSearchPageSize;

   ConnectorInputValidatorNetSuite(List<String> var1) {
      if (var1.size() > 4) {
         String var2 = "The list (" + var1.size() + ") has too many value! " + var1.toString();
         throw new ConnectorInvalidInputRuntimeException(var2);
      }

      this.operationString = var1.size() > 0 ? (String)var1.get(0) : "";
      this.netSuiteObject = var1.size() > 1 ? (String)var1.get(1) : "";
      this.searchPropertyString = var1.size() > 2 ? (String)var1.get(2) : "";
      this.netSuiteSearchPageSize = var1.size() > 3 ? (String)var1.get(3) : "";
      this.operation = ConnectorNetSuiteOperation.getOperation(this.operationString);
   }

   NetSuiteSelectedObject getNetSuiteConnectorSelectedObject() {
      return Strings.isNullOrEmpty(this.netSuiteObject)
         ? new NetSuiteSelectedObject(this.operation)
         : new NetSuiteSelectedObject(this.operation, this.netSuiteObject, this.searchPropertyString);
   }

   void validatecomplete() {
      if (this.operation != EntityType.NetSuiteDelete) {
         this.checkEmpty(this.netSuiteObject, "NetSuite Object");
      }
   }

   void checkEmpty(String var1, String var2) {
      if (var1 == null || var1.isEmpty()) {
         throw new ConnectorInvalidInputRuntimeException(var2 + " can not be empty");
      }
   }
}
