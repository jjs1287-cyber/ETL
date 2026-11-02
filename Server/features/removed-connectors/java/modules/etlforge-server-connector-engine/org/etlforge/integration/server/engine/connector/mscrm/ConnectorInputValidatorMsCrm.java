package org.etlforge.integration.server.engine.connector.mscrm;

import com.google.common.base.Strings;
import java.util.List;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class ConnectorInputValidatorMsCrm {
   FunctionType crmOperation;
   String crmObject;

   ConnectorInputValidatorMsCrm(List<String> var1) {
      String var2 = var1.size() < 1 ? "" : (String)var1.get(0);
      this.crmObject = var1.size() < 2 ? "" : (String)var1.get(1);
      if (Strings.isNullOrEmpty(var2)) {
         this.crmOperation = null;
      } else {
         this.crmOperation = new ConnectorMsCrmOperation().newValidFunctionType(var2);
      }
   }

   void validatecomplete() {
      this.checkEmpty(this.crmObject, "Crm Object");
   }

   void checkEmpty(String var1, String var2) {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorInvalidInputRuntimeException(var2 + " can not be empty");
      }
   }
}
