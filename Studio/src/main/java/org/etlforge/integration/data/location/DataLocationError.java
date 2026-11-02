package org.etlforge.integration.data.location;

import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class DataLocationError implements ValidationIssue {
   public static final DataLocationError MISSING_LOGIN = new DataLocationError("Validation.LoginMissing");
   private final String messageKey;

   public DataLocationError(String var1) {
      this.messageKey = var1;
   }

   @Override
   public void report(ValidationMessageCollector var1) {
      var1.addMessage(this.getMessage(), ValidationStatus.INVALID);
   }

   @Override
   public String getMessage() {
      return LocationMessages.getString(this.messageKey);
   }

   @Override
   public String toString() {
      return this.getMessage();
   }
}
