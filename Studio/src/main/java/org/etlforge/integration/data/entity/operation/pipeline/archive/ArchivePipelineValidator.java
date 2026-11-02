package org.etlforge.integration.data.entity.operation.pipeline.archive;

import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineValidator;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;

public final class ArchivePipelineValidator extends OperationPipelineValidator {
   private final OperationPipelineBrowser browser;

   public ArchivePipelineValidator(Operation var1, OperationPipeline var2, ValidationMessageCollector var3) {
      super(var1, var2, var3);
      this.browser = new OperationPipelineBrowser(var2);
   }

   @Override
   public void validate() {
      this.validateSource();
      this.validateTarget();
   }

   private void validateSource() {
      Source var1 = this.browser.getSource();
      if (var1 == null) {
         this.sourceMissing();
      } else if (!(var1 instanceof SystemSource)) {
         this.validateLocation(SourceTarget.Source, var1.getLocation());
      }
   }

   private void validateTarget() {
      Target var1 = this.browser.getTarget();
      if (var1 != null) {
         this.validateLocation(SourceTarget.Target, var1.getLocation());
      } else {
         this.targetMissing();
      }
   }

   private void validateLocation(SourceTarget var1, DataLocation var2) {
      if (var2 == null) {
         this.fatal("Incomplete " + var1.toString().toLowerCase() + " definition");
      } else if (!var2.isFileBased()) {
         this.fatal("The " + var1.toString().toLowerCase() + " is not applicable for archiving");
      }
   }

   private void sourceMissing() {
      this.fatal("No source specified");
   }

   private void targetMissing() {
      this.fatal("No target specified");
   }
}
