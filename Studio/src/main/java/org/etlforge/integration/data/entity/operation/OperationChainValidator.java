package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.Sets;
import java.util.HashSet;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

final class OperationChainValidator {
   private final String name;
   private final OperationChainValidator.NextOperation nextOperation;

   public OperationChainValidator(String var1, OperationChainValidator.NextOperation var2) {
      this.name = var1;
      this.nextOperation = var2;
   }

   public void validate(Operation var1, ValidationMessageCollector var2) {
      HashSet var3 = Sets.newHashSet();
      Operation var4 = var1;
      Operation var5 = this.nextOperation.get(var4);

      while (var5 != null) {
         if (var3.contains(var5)) {
            var2.addMessage("Infinite operation loop detected in " + this.name + " chain.", ValidationStatus.WARNING);
            break;
         }

         var3.add(var5);
         var4 = var5;
         var5 = this.nextOperation.get(var4);
      }
   }

   interface NextOperation {
      Operation get(Operation var1);
   }
}
