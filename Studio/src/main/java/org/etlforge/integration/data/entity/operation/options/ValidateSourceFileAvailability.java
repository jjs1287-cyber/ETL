package org.etlforge.integration.data.entity.operation.options;

import com.google.common.base.Optional;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;

public final class ValidateSourceFileAvailability {
   private Optional<TextDocumentId> textDocumentId = Optional.absent();

   public boolean isAvailable(Operation var1) {
      this.textDocumentId = Optional.absent();
      OperationPipeline var2 = var1.getPipeline();
      return this.isAvailable(var2);
   }

   public boolean isAvailable(OperationPipeline var1) {
      TransformationActivity var2 = var1.getFirstActivityOfType(TransformationActivity.class);
      if (var2 != null && !var2.isEmpty()) {
         Transformation var3 = var2.getContent();
         DataStructure var4 = var3.getInputStructure();
         if (var4 instanceof SimpleTextStructure) {
            this.textDocumentId = Optional.fromNullable(var3.getSourceTextDocumentId());
         }

         return this.textDocumentId.isPresent();
      } else {
         return false;
      }
   }

   public TextDocumentId getTextDocumentId() {
      return this.textDocumentId.get();
   }
}
