package org.etlforge.integration.data.entity.target;

import com.google.common.base.Preconditions;
import java.util.Set;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.file.FileLocationError;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class TargetValidator {
   private final Target target;
   private final ValidationMessageCollector collector;

   public TargetValidator(Target var1, ValidationMessageCollector var2) {
      Preconditions.checkNotNull(var1, "tgt");
      Preconditions.checkNotNull(var2, "collector");
      this.target = var1;
      this.collector = var2;
   }

   public void run() {
      DataLocation var1 = this.target.getLocation();
      if (var1 == null) {
         this.fatal(TargetMessages.getString("TargetValidator.NoLocation"));
      } else {
         this.validateLocation(var1);
      }
   }

   private void validateLocation(DataLocation var1) {
      var1.collectValidationMessages(this.collector);
      if (var1 instanceof FileLocation) {
         this.validateFileName((FileLocation)var1);
      }
   }

   private void validateFileName(FileLocation var1) {
      Set var2 = var1.getTargetFileName().validate();
      var2.remove(FileLocationError.MISSING_FILTER);

      for (Object __etlItem44 : var2) {
         ValidationIssue var4 = (ValidationIssue) __etlItem44;
         var4.report(this.collector);
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
