package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.persist.Persistor;

public final class ValidationRule {
   private final Operator operator;
   private final OperatorArgument argument;

   public static ValidationRule callScript(Script var0) {
      return new ValidationRule(Operator.fromName("Script"), new ScriptArgument(var0.getID()));
   }

   public ValidationRule(Operator var1, OperatorArgument var2) {
      this.operator = Preconditions.checkNotNull(var1, "operator");
      this.argument = ArgCheck.checkInstance(var2, var1.getArgumentClass());
   }

   public boolean isApplicableToField(FieldDataType var1) {
      return this.operator.isApplicableToField(var1);
   }

   public Operator getOperator() {
      return this.operator;
   }

   public OperatorArgument getArgument() {
      return this.argument;
   }

   public void writeTo(Persistor var1) {
      var1.putString("op", this.operator.getName());
      var1.putText("arg", this.argument.toStorageForm());
   }

   public static Optional<ValidationRule> readFrom(Persistor var0) {
      String var1 = var0.getString("op");
      String var2 = var0.getText("arg");
      if (!Strings.isNullOrEmpty(var1) && !Strings.isNullOrEmpty(var2)) {
         Operator var3 = Operator.fromName(var1);
         OperatorArgument var4 = restoreArgument(var3, var2);
         return Optional.fromNullable(new ValidationRule(var3, var4));
      } else {
         return Optional.absent();
      }
   }

   public static OperatorArgument restoreArgument(Operator var0, String var1) {
      Class var2 = var0.getArgumentClass();
      if (var2.equals(StringArgument.class)) {
         return StringArgument.fromStorageForm(var1);
      } else if (var2.equals(Range.class)) {
         return Range.fromStorageForm(var1);
      } else if (var2.equals(StringList.class)) {
         return StringList.fromStorageForm(var1);
      } else if (var2.equals(ScriptArgument.class)) {
         return ScriptArgument.fromStorageForm(var1);
      } else {
         throw new IllegalStateException("Unexpected argument type to validation operator: " + var2.getName());
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.operator, this.argument);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ValidationRule)) {
         return false;
      }

      ValidationRule var2 = (ValidationRule)var1;
      return this.operator.equals(var2.operator) && this.argument.equals(var2.argument);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("op", this.operator).add("arg", this.argument).toString();
   }
}
