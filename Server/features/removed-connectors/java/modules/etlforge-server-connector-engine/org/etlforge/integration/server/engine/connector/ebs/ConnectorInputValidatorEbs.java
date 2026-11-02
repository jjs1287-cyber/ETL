package org.etlforge.integration.server.engine.connector.ebs;

import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorInvalidInputRuntimeException;

public class ConnectorInputValidatorEbs {
   final boolean isLastList;
   final String objectLabel;
   final String catalog;
   final String concurrProgram;

   public ConnectorInputValidatorEbs(List<String> var1) {
      this.catalog = var1.size() == 0 ? null : (String)var1.get(0);
      this.concurrProgram = var1.size() < 2 ? null : (String)var1.get(1);
      this.isLastList = var1.size() > 0;
      this.objectLabel = this.isLastList ? "Concurrent Program" : "EBS Catalog";
   }

   void validateComplete() {
      this.checkEmpty(this.catalog, "catalog");
      this.checkEmpty(this.concurrProgram, "Concurrent Program");
   }

   void checkEmpty(String var1, String var2) {
      if (var1 == null || var1.isEmpty()) {
         throw new ConnectorInvalidInputRuntimeException(var2 + " can not be empty");
      }
   }
}
