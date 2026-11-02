package org.etlforge.integration.data.location;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

final class HttpLocationValidator extends AbstractLocationValidator {
   public static final ValidationIssue MISSING_URL = new DataLocationError("HttpLocationValidator.URL");
   private final HttpLocation location;
   private Set<ValidationIssue> errors;

   public HttpLocationValidator(HttpLocation var1) {
      this.location = var1;
   }

   public HttpLocationValidator(HttpLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.setMessageCollector(var2);
   }

   public Set<ValidationIssue> validate() {
      this.errors = Sets.newLinkedHashSet();
      String var1 = this.location.getUrl();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(MISSING_URL);
      }

      this.collectMessages(this.errors);
      return this.errors;
   }
}
