package org.etlforge.integration.data.entity.operation.options;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class ValidateSourceFileOption implements OperationOption {
   private final boolean validate;
   private final FileDestination validRecordsTo;
   private final FileDestination invalidRecordsTo;

   public static ValidateSourceFileOption noValidation() {
      return new ValidateSourceFileOption(false, FileDestination.none(), FileDestination.none());
   }

   public static ValidateSourceFileOption validate(FileDestination var0, FileDestination var1) {
      return new ValidateSourceFileOption(true, var0, var1);
   }

   private ValidateSourceFileOption(boolean var1, FileDestination var2, FileDestination var3) {
      this.validate = var1;
      this.validRecordsTo = Preconditions.checkNotNull(var2);
      this.invalidRecordsTo = Preconditions.checkNotNull(var3);
   }

   public ValidateSourceFileOption(Operation var1) {
      this.validate = var1.getBooleanProperty("validate_source_text_file", false);
      if (this.validate) {
         this.validRecordsTo = new FileDestination(var1, "valid_source_records.target", "valid_source_records.email");
         this.invalidRecordsTo = new FileDestination(var1, "invalid_source_records.target", "invalid_source_records.email");
      } else {
         this.validRecordsTo = FileDestination.none();
         this.invalidRecordsTo = FileDestination.none();
      }
   }

   @Override
   public void applyTo(Operation var1) {
      var1.setBooleanProperty("validate_source_text_file", this.validate);
      EntityUtils.storeId(this.validRecordsTo.getTargetId(), TargetId.class, var1, "valid_source_records.target");
      EntityUtils.storeId(this.validRecordsTo.getEmailId(), EmailMessageId.class, var1, "valid_source_records.email");
      EntityUtils.storeId(this.invalidRecordsTo.getTargetId(), TargetId.class, var1, "invalid_source_records.target");
      EntityUtils.storeId(this.invalidRecordsTo.getEmailId(), EmailMessageId.class, var1, "invalid_source_records.email");
   }

   @Override
   public boolean isApplicableFor(OperationPipeline var1) {
      ValidateSourceFileAvailability var2 = new ValidateSourceFileAvailability();
      return var2.isAvailable(var1);
   }

   public boolean isValidationEnabled() {
      return this.validate;
   }

   public boolean isSendingData() {
      return !this.validRecordsTo.isEmpty() || !this.invalidRecordsTo.isEmpty();
   }

   public FileDestination getDestinationForValidRecords() {
      return this.validRecordsTo;
   }

   public FileDestination getDestinationForInvalidRecords() {
      return this.invalidRecordsTo;
   }

   public List<EntityLink> collectLinks(Operation var1, IntegrationEntityLookup var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.addAll(this.validRecordsTo.collectLinks(var1, var2, "valid_source_records.target", "valid_source_records.email"));
      var3.addAll(this.invalidRecordsTo.collectLinks(var1, var2, "invalid_source_records.target", "invalid_source_records.email"));
      return var3;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.validate, this.validRecordsTo, this.invalidRecordsTo);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ValidateSourceFileOption)) {
         return false;
      }

      ValidateSourceFileOption var2 = (ValidateSourceFileOption)var1;
      return this.validate == var2.validate && this.validRecordsTo.equals(var2.validRecordsTo) && this.invalidRecordsTo.equals(var2.invalidRecordsTo);
   }

   @Override
   public String toString() {
      ToStringHelper var1 = Objects.toStringHelper(this).add("validate", this.validate);
      if (this.validate) {
         var1.add("valid records to", this.validRecordsTo);
         var1.add("invalid records to", this.invalidRecordsTo);
      }

      return var1.toString();
   }
}
