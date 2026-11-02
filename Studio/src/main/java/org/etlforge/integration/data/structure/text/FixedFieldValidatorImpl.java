package org.etlforge.integration.data.structure.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class FixedFieldValidatorImpl implements TextStructureValidator {
   private final ImmutableList<Field> fields;

   public FixedFieldValidatorImpl(Iterable<Field> var1) {
      this.fields = ImmutableList.copyOf(var1);
   }

   @Override
   public Collection<? extends ValidationIssue> validate() {
      ArrayList var1 = Lists.newArrayList();
      if (!this.fields.isEmpty()) {
         this.collectValidationErrors(var1);
      }

      return var1;
   }

   private void collectValidationErrors(List<ValidationIssue> var1) {
      Field var2 = this.fields.get(0);
      if (var2.getBeginPosition() != 1) {
         var1.add(ValidationIssue.FACTORY.createWarning("The first field does not begin at position 1."));
      }

      Field var3 = var2;
      int var4 = 1;

      for (int var5 = this.fields.size(); var4 < var5; var4++) {
         Field var6 = this.fields.get(var4);
         if (var6.getBeginPosition() != var3.getEndPosition() + 1) {
            StringBuilder var7 = new StringBuilder("Field #");
            var7.append(var4 + 1).append(": ");
            var7.append("this field is unconnected or overlapped to the previous field.");
            var1.add(ValidationIssue.FACTORY.createWarning(var7.toString()));
         }

         var3 = var6;
      }
   }
}
