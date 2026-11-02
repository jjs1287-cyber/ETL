package org.etlforge.integration.data.location;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

final class DatabaseLocationValidator extends AbstractLocationValidator {
   public static final ValidationIssue MISSING_DRIVER = new DataLocationError("DatabaseLocationValidator.NoDriver");
   public static final ValidationIssue MISSING_CONNECTION_STRING = new DataLocationError("DatabaseLocationValidator.NoConnectionString");
   public static final ValidationIssue MISSING_SERVER = new DataLocationError("DatabaseLocationValidator.NoServer");
   private final DatabaseLocation location;
   private Set<ValidationIssue> errors;

   public DatabaseLocationValidator(DatabaseLocation var1) {
      this(var1, null);
   }

   public DatabaseLocationValidator(DatabaseLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.setMessageCollector(var2);
   }

   public Set<ValidationIssue> validate() {
      this.errors = Sets.newLinkedHashSet();
      this.validateDriver();
      if (this.location.isManualMode()) {
         this.validateManualMode();
      } else {
         this.validateNonManualMode();
      }

      this.collectMessages(this.errors);
      return this.errors;
   }

   private void validateDriver() {
      if (this.location.getDriver() == null) {
         this.errors.add(MISSING_DRIVER);
      }
   }

   private void validateManualMode() {
      String var1 = this.location.getManualConnectionString();
      if (var1 == null || var1.length() == 0) {
         this.errors.add(MISSING_CONNECTION_STRING);
      }
   }

   private void validateNonManualMode() {
      String var1 = this.location.getServer();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(MISSING_SERVER);
      }
   }
}
