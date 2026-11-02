package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.persist.Persistor;

public final class NumberOfFieldsValidation {
   private final int requiredNumber;
   private final InvalidRecordAction action;

   public NumberOfFieldsValidation(int var1, InvalidRecordAction var2) {
      this.requiredNumber = ArgCheck.checkPositive(var1);
      Preconditions.checkArgument(var2 != InvalidRecordAction.SKIP_FIELD, "Unsupported action: " + var2);
      this.action = Preconditions.checkNotNull(var2);
   }

   public int getRequiredNumberOfColumns() {
      return this.requiredNumber;
   }

   public InvalidRecordAction getAction() {
      return this.action;
   }

   public void writeTo(Persistor var1) {
      String var2 = this.toStorageForm();
      var1.putString("numberOfRequiredFields", var2);
   }

   public static Optional<NumberOfFieldsValidation> readFrom(Persistor var0) {
      String var1 = var0.getString("numberOfRequiredFields");
      if (StringUtils.isNotBlank(var1)) {
         try {
            NumberOfFieldsValidation var2 = fromStorageForm(var1);
            return Optional.of(var2);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return Optional.absent();
   }

   public String toStorageForm() {
      return this.requiredNumber + ";" + this.action.name();
   }

   public static NumberOfFieldsValidation fromStorageForm(String var0) {
      String[] var1 = var0.split(";");
      int var2 = Integer.parseInt(var1[0]);
      InvalidRecordAction var3 = InvalidRecordAction.valueOf(var1[1]);
      return new NumberOfFieldsValidation(var2, var3);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.requiredNumber, this.action);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof NumberOfFieldsValidation)) {
         return false;
      }

      NumberOfFieldsValidation var2 = (NumberOfFieldsValidation)var1;
      return this.requiredNumber == var2.requiredNumber && this.action == var2.action;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("numberOfColumns", this.requiredNumber).add("action", this.action).toString();
   }
}
