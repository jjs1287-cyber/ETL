package org.etlforge.integration.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.etlforge.integration.data.validation.ValidationContext;
import org.etlforge.integration.data.validation.ValidationMessage;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationResult;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class ValidationMessageCollectorImpl implements ValidationMessageCollector {
   private final ValidationContext context;
   private final Multimap<ValidationStatus, ValidationMessage> messages = ArrayListMultimap.create();

   public ValidationMessageCollectorImpl(ValidationContext var1) {
      this.context = Preconditions.checkNotNull(var1, "context");
   }

   @Override
   public void addMessage(String var1, ValidationStatus var2) {
      Preconditions.checkNotNull(var1, "message");
      Preconditions.checkNotNull(var2, "level");
      String var3 = var1.trim();
      Preconditions.checkArgument(var3.length() > 0, "message cannot be empty");
      ValidationMessage var4 = new ValidationMessage(var2, var3);
      this.messages.put(var2, var4);
   }

   @Override
   public ValidationContext context() {
      return this.context;
   }

   public void collect(IntegrationData var1) {
      this.messages.clear();
      var1.collectValidationMessages(this);
   }

   public ValidationResult result() {
      return new ValidationMessageCollectorImpl.ResultImpl(this.messages);
   }

   private static class ResultImpl implements ValidationResult {
      private final Multimap<ValidationStatus, ValidationMessage> messages;

      public ResultImpl(Multimap<ValidationStatus, ValidationMessage> var1) {
         this.messages = var1;
      }

      @Override
      public ImmutableList<ValidationMessage> allMessages() {
         return ImmutableList.copyOf(this.messages.values());
      }

      @Override
      public String concatenateMessages() {
         StringBuilder var1 = new StringBuilder();

         for (ValidationMessage var3 : this.messages.values()) {
            if (var1.length() > 0) {
               var1.append("\n");
            }

            var1.append(var3.message());
         }

         return var1.toString();
      }

      @Override
      public boolean isEmpty() {
         return this.messages.size() == 0;
      }

      @Override
      public ValidationStatus status() {
         if (this.messages.containsKey(ValidationStatus.INVALID)) {
            return ValidationStatus.INVALID;
         } else {
            return this.messages.containsKey(ValidationStatus.WARNING) ? ValidationStatus.WARNING : ValidationStatus.VALID;
         }
      }

      @Override
      public boolean isFatal() {
         return this.status() == ValidationStatus.INVALID;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 instanceof ValidationResult) {
            ValidationResult var2 = (ValidationResult)var1;
            return Sets.newHashSet(this.allMessages()).equals(Sets.newHashSet(var2.allMessages()));
         } else {
            return false;
         }
      }

      @Override
      public int hashCode() {
         return Sets.newHashSet(this.allMessages()).hashCode();
      }
   }
}
