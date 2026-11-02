package org.etlforge.integration.data.location;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.location.file.FileLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

final class FileShareLocationValidator extends AbstractLocationValidator {
   private final FileShareLocation location;
   private Set<ValidationIssue> errors;

   public FileShareLocationValidator(FileShareLocation var1) {
      this(var1, null);
   }

   public FileShareLocationValidator(FileShareLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.setMessageCollector(var2);
   }

   public Set<? extends ValidationIssue> validate() {
      this.errors = Sets.newLinkedHashSet();
      this.validatePath();
      this.validateLogin();
      this.validateFilter();
      this.collectMessages(this.errors);
      return this.errors;
   }

   private void validatePath() {
      String var1 = this.location.getPath();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(FileLocationError.MISSING_PATH);
      }
   }

   private void validateLogin() {
      String var1 = this.location.getLogin();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(DataLocationError.MISSING_LOGIN);
      }
   }

   private void validateFilter() {
      this.errors.addAll(this.location.getFileFilter().validate());
   }
}
