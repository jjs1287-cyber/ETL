package org.etlforge.integration.data.entity.source;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class SourceValidator {
   private final Source source;
   private final ValidationMessageCollector collector;

   public SourceValidator(Source var1, ValidationMessageCollector var2) {
      Preconditions.checkNotNull(var1, "src");
      Preconditions.checkNotNull(var2, "collector");
      this.source = var1;
      this.collector = var2;
   }

   public void run() {
      DataLocation var1 = this.source.getLocation();
      if (var1 == null) {
         this.fatal(SourceMessages.getString("SourceValidator.NoLocation"));
      } else {
         var1.collectValidationMessages(this.collector);
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
