package org.etlforge.integration.data.location;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.location.file.IgnoredLines;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.PortAccessible;

final class FileLocationValidator extends AbstractLocationValidator {
   private final FileLocation location;
   private Set<ValidationIssue> errors;

   public FileLocationValidator(FileLocation var1) {
      this(var1, null);
   }

   public FileLocationValidator(FileLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.setMessageCollector(var2);
   }

   public Set<ValidationIssue> validate() {
      this.errors = Sets.newLinkedHashSet();
      this.validatePort();
      this.validateIgnoredLines();
      if (!this.location.willBeDeletedOnSuccess()) {
         this.validateRenameFileTo();
      }

      this.collectMessages(this.errors);
      return this.errors;
   }

   private void validatePort() {
      if (this.location instanceof PortAccessible) {
         Port var1 = ((PortAccessible)this.location).getPort();
         if (var1 == Port.INVALID) {
            this.errors.add(new DataLocationError("Validation.InvalidPort"));
         }
      }
   }

   private void validateIgnoredLines() {
      IgnoredLines var1 = this.location.getLinesToIgnore();
      this.errors.addAll(var1.validate());
   }

   private void validateRenameFileTo() {
      this.errors.addAll(this.location.getRenameFileTo().validate());
   }
}
