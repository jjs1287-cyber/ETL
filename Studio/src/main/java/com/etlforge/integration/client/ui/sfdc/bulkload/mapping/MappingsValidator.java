package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.util.List;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.system.callback.Receiver;

public final class MappingsValidator {
   private final Receiver<MappingsValidator.Error> reporter;

   public MappingsValidator(Receiver<MappingsValidator.Error> var1) {
      this.reporter = var1;
   }

   public boolean validate(HeaderMappings var1) {
      List var2 = var1.getMappedTargetFields();
      if (var2.isEmpty()) {
         this.reporter.handle(new MappingsValidator.Error("Incomplete Mapping", "At least one field must be mapped."));
         return false;
      } else {
         return true;
      }
   }

   public static class Error {
      public final String title;
      public final String message;

      public Error(String var1, String var2) {
         this.title = var1;
         this.message = var2;
      }
   }
}
