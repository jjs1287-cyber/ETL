package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.util.persist.Persistor;

public final class FieldValidation {
   private final boolean required;
   private final Optional<Integer> maxLength;
   private final Optional<StringOptions> stringOptions;
   private final Optional<ValidationRule> rule;
   private final InvalidRecordAction action;

   public FieldValidation(boolean var1, Optional<Integer> var2, Optional<StringOptions> var3, Optional<ValidationRule> var4, InvalidRecordAction var5) {
      this.required = var1;
      this.maxLength = Preconditions.checkNotNull(var2);
      this.stringOptions = Preconditions.checkNotNull(var3);
      this.rule = Preconditions.checkNotNull(var4);
      this.action = Preconditions.checkNotNull(var5);
   }

   public boolean isRequired() {
      return this.required;
   }

   public boolean hasEffect() {
      if (this.required) {
         return true;
      } else {
         return this.maxLength.isPresent() ? true : this.rule.isPresent();
      }
   }

   public Optional<Integer> getMaxLength() {
      return this.maxLength;
   }

   public Optional<StringOptions> getStringOptions() {
      return this.stringOptions;
   }

   public Optional<ValidationRule> getRule() {
      return this.rule;
   }

   public InvalidRecordAction getAction() {
      return this.action;
   }

   public static FieldValidation.Builder builder() {
      return new FieldValidation.Builder();
   }

   public FieldValidation withoutOperator() {
      return this.rule.isPresent() ? new FieldValidation(this.required, this.maxLength, this.stringOptions, Optional.absent(), this.action) : this;
   }

   public FieldValidation withOperator(ValidationRule var1) {
      return var1.equals(this.rule.orNull()) ? this : new FieldValidation(this.required, this.maxLength, this.stringOptions, Optional.of(var1), this.action);
   }

   public FieldValidation transformToType(FieldDataType var1) {
      FieldValidation.Builder var2 = builder();
      var2.setAction(this.action);
      var2.setRequired(this.isRequired());
      if (this.maxLength.isPresent()) {
         var2.setMaxLength(this.maxLength.get());
      }

      if (this.stringOptions.isPresent() && var1 == FieldDataType.String) {
         var2.setStringOptions(this.stringOptions);
      }

      if (this.rule.isPresent() && this.rule.get().isApplicableToField(var1)) {
         var2.setRule(this.rule);
      }

      return var2.build();
   }

   public void writeTo(Persistor var1) {
      this.action.writeTo(var1);
      var1.putBoolean("required", this.required);
      if (this.maxLength.isPresent()) {
         var1.putInteger("length", this.maxLength.get());
      }

      if (this.stringOptions.isPresent()) {
         this.stringOptions.get().writeTo(var1);
      }

      if (this.rule.isPresent()) {
         this.rule.get().writeTo(var1);
      }
   }

   public static FieldValidation readFrom(Persistor var0) {
      FieldValidation.Builder var1 = new FieldValidation.Builder();
      InvalidRecordAction var2 = InvalidRecordAction.readFrom(var0);
      var1.setAction(var2);
      Boolean var3 = var0.getBoolean("required");
      if (var3 != null) {
         var1.setRequired(var3);
      }

      Integer var4 = var0.getInteger("length");
      if (var4 != null) {
         var1.setMaxLength(var4);
      }

      Optional var5 = StringOptions.readFrom(var0);
      if (var5.isPresent()) {
         var1.setStringOptions(var5);
      }

      Optional var6 = ValidationRule.readFrom(var0);
      var1.setRule(var6);
      return var1.build();
   }

   public String[] getFlatDefinition(int var1, int var2) {
      String[] var3 = new String[10];
      var3[0] = "";
      var3[1] = Integer.toString(var1);
      var3[2] = Integer.toString(var2);
      var3[3] = toStringRep(this.required);
      if (this.maxLength.isPresent()) {
         var3[4] = this.maxLength.get().toString();
      }

      if (this.stringOptions.isPresent()) {
         StringOptions var4 = this.stringOptions.get();
         var3[5] = toStringRep(var4.trimToLength);
         var3[6] = toStringRep(var4.ignoreCase);
      }

      if (this.rule.isPresent()) {
         ValidationRule var5 = this.rule.get();
         var3[7] = var5.getOperator().getName();
         var3[8] = var5.getArgument().toStorageForm();
      }

      var3[9] = this.action.name();
      return var3;
   }

   public static FieldValidation fromKeyValues(StringKeyValuePairs var0) {
      boolean var1 = var0.getBoolean("notempty", false);
      Optional var2 = Optional.absent();
      String var3 = var0.get("fieldlength", "");
      if (!var3.isEmpty()) {
         var2 = Optional.of(Integer.parseInt(var3));
      }

      boolean var4 = var0.getBoolean("trimtolength", false);
      boolean var5 = var0.getBoolean("ignorecase", false);
      Optional var6 = Optional.of(new StringOptions(var5, var4));
      Optional var7 = Optional.absent();
      String var8 = var0.get("fieldoperator", "");
      String var9 = var0.get("operatorargument", "");
      if (!var8.isEmpty() && !var9.isEmpty()) {
         Operator var10 = Operator.fromName(var8);
         OperatorArgument var11 = ValidationRule.restoreArgument(var10, var9);
         var7 = Optional.of(new ValidationRule(var10, var11));
      }

      String var12 = var0.get("actiononinvalid");
      InvalidRecordAction var13 = InvalidRecordAction.valueOf(var12);
      return new FieldValidation(var1, var2, var6, var7, var13);
   }

   private static final String toStringRep(boolean var0) {
      return var0 ? "1" : "0";
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.required, this.maxLength, this.stringOptions, this.rule, this.action);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof FieldValidation)) {
         return false;
      }

      FieldValidation var2 = (FieldValidation)var1;
      return this.required == var2.required
         && this.maxLength.equals(var2.maxLength)
         && this.stringOptions.equals(var2.stringOptions)
         && this.rule.equals(var2.rule)
         && this.action == var2.action;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("required", this.required)
         .add("length", this.maxLength)
         .add("string-options", this.stringOptions)
         .add("rule", this.rule)
         .add("action", this.action)
         .toString();
   }

   public static final class Builder {
      private boolean required;
      private Optional<Integer> maxLength = Optional.absent();
      private Optional<StringOptions> stringOptions = Optional.absent();
      private Optional<ValidationRule> rule = Optional.absent();
      private InvalidRecordAction action = InvalidRecordAction.RAISE_ERROR;

      private Builder() {
      }

      public FieldValidation.Builder setMaxLength(int var1) {
         this.maxLength = Optional.of(var1);
         return this;
      }

      public FieldValidation.Builder setStringOptions(StringOptions var1) {
         this.stringOptions = Optional.of(var1);
         return this;
      }

      private FieldValidation.Builder setStringOptions(Optional<StringOptions> var1) {
         this.stringOptions = var1;
         return this;
      }

      public FieldValidation.Builder setRule(ValidationRule var1) {
         this.rule = Optional.of(var1);
         return this;
      }

      private FieldValidation.Builder setRule(Optional<ValidationRule> var1) {
         this.rule = var1;
         return this;
      }

      public FieldValidation.Builder setRequired(boolean var1) {
         this.required = var1;
         return this;
      }

      public FieldValidation.Builder setAction(InvalidRecordAction var1) {
         this.action = Preconditions.checkNotNull(var1);
         return this;
      }

      public FieldValidation build() {
         return new FieldValidation(this.required, this.maxLength, this.stringOptions, this.rule, this.action);
      }
   }
}
