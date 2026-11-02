package org.etlforge.integration.data.location.local;

import org.etlforge.integration.data.location.LocalFileLocation;
import org.etlforge.integration.data.location.file.PathValidator;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class LocalFileLocationValidator {
   private final LocalFileLocation location;
   private final ValidationMessageCollector collector;

   public LocalFileLocationValidator(LocalFileLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.collector = var2;
   }

   public void validate() {
      this.validatePath();
      this.validateFilter();
   }

   private void validatePath() {
      PathValidator var1 = this.createPathValidator();
      if (!var1.isValid(this.location.getFolderPath())) {
         this.fatal(var1.getLastError());
      }
   }

   private PathValidator createPathValidator() {
      PathValidator var1 = new PathValidator();
      var1.setRequired(true);
      var1.setAllowDriveLetter(true);
      var1.setAllowFileshares(true);
      return var1;
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
