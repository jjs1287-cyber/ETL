package org.etlforge.integration.data.location.ldap;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.location.AbstractLocationValidator;
import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.util.net.Port;

public final class LdapLocationValidator extends AbstractLocationValidator {
   public static final ValidationIssue MISSING_SERVER = new DataLocationError("Validator.NoServer");
   public static final ValidationIssue INVALID_PORT = new DataLocationError("Validator.InvalidPort");
   private final LdapLocation location;
   private Set<ValidationIssue> errors;

   public LdapLocationValidator(LdapLocation var1) {
      this(var1, null);
   }

   public LdapLocationValidator(LdapLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.setMessageCollector(var2);
   }

   public Set<ValidationIssue> validate() {
      this.errors = Sets.newLinkedHashSet();
      this.validateServer();
      this.validatePort();
      this.collectMessages(this.errors);
      return this.errors;
   }

   private void validateServer() {
      String var1 = this.location.getServer();
      if (var1.length() == 0) {
         this.errors.add(MISSING_SERVER);
      }
   }

   private void validatePort() {
      Port var1 = this.location.getPort();
      if (var1 == Port.INVALID) {
         this.errors.add(INVALID_PORT);
      }
   }
}
