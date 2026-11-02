package org.etlforge.integration.data.location.tempstore;

import org.etlforge.integration.data.location.TemporaryStorageLocation;
import org.etlforge.integration.data.location.file.PathValidator;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class TemporaryStorageLocationValidator {
   private final TemporaryStorageLocation location;
   private final ValidationMessageCollector collector;

   public TemporaryStorageLocationValidator(TemporaryStorageLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.collector = var2;
   }

   public void validate() {
      this.validatePath();
      this.validateFilter();
   }

   private void validatePath() {
      PathValidator var1 = new PathValidator();
      if (!var1.isValid(this.location.getPath())) {
         this.fatal(var1.getLastError());
      }
   }

   private void validateFilter() {
      for (ValidationIssue var2 : this.location.getFileFilter().validate()) {
         var2.report(this.collector);
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
