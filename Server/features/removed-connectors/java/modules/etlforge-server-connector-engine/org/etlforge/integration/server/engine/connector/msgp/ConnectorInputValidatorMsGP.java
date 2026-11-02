package org.etlforge.integration.server.engine.connector.msgp;

import com.google.common.base.Strings;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class ConnectorInputValidatorMsGP {
   final String gpOperation;
   final String gpModule;

   ConnectorInputValidatorMsGP(List<String> var1) {
      this.gpOperation = ConnectorMsGPOperation.getOperation(var1.size() > 0 ? (String)var1.get(0) : "");
      this.gpModule = var1.size() > 1 ? (String)var1.get(1) : "";
   }

   void validatecomplete() {
      this.checkEmpty(this.gpOperation, "MsGP Operation");
      this.checkEmpty(this.gpModule, "MsGP Module");
   }

   void checkEmpty(String var1, String var2) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorInvalidInputRuntimeException(var2 + " can not be empty");
      }
   }
}
