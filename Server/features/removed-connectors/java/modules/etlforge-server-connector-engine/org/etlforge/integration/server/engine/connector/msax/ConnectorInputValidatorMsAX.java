package org.etlforge.integration.server.engine.connector.msax;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;
import org.etlforge.integration.server.engine.connector.msax.adapter.ConnectorEngineMsAXAdapter;

public class ConnectorInputValidatorMsAX {
   final String wsdlurl;
   final String serviceName;
   final String operation;

   ConnectorInputValidatorMsAX(List<String> var1) {
      this.wsdlurl = var1.size() > 0 ? (String)var1.get(0) : "";
      String var2 = var1.size() > 1 ? ((String)var1.get(1)).trim() : "";
      ImmutableList var3 = ConnectorEngineMsAXAdapter.parseServiceAndOperation(var2);
      this.serviceName = var3.size() > 0 ? (String)var3.get(0) : "";
      this.operation = var3.size() > 1 ? (String)var3.get(1) : "";
   }

   void validateWsdlurl() {
      checkEmpty(this.wsdlurl, "wsdl url");
   }

   void validateComplete() {
      this.validateWsdlurl();
      checkEmpty(this.serviceName, "service name");
      checkEmpty(this.operation, "operation");
   }

   static void checkEmpty(String var0, String var1) {
      if (Strings.isNullOrEmpty(var0)) {
         throw new ConnectorInvalidInputRuntimeException(var1 + " can not be empty");
      }
   }
}
