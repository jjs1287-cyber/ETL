package org.etlforge.integration.data.location.ftp;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.location.AbstractLocationValidator;
import org.etlforge.integration.data.location.DataLocationError;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.file.FileLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class FtpLocationValidator extends AbstractLocationValidator {
   private final FtpLocation location;
   private Set<ValidationIssue> errors;

   public FtpLocationValidator(FtpLocation var1) {
      this(var1, null);
   }

   public FtpLocationValidator(FtpLocation var1, ValidationMessageCollector var2) {
      this.location = var1;
      this.setMessageCollector(var2);
   }

   public Set<ValidationIssue> validate() {
      this.errors = Sets.newLinkedHashSet();
      this.validateHost();
      this.validateLogin();
      this.validateFilter();
      this.collectMessages(this.errors);
      return this.errors;
   }

   private void validateHost() {
      String var1 = this.location.getHost();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(FtpLocationError.MISSING_HOST);
      }
   }

   private void validateLogin() {
      String var1 = this.location.getLogin();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(DataLocationError.MISSING_LOGIN);
      }
   }

   private void validateFilter() {
      String var1 = this.location.getFileFilter().getFilter();
      if (var1 == null || var1.trim().length() == 0) {
         this.errors.add(FileLocationError.MISSING_FILTER);
      }
   }
}
