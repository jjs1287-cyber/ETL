package org.etlforge.integration.data.entity.operation.options;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.entity.sfdc.BulkLoadOperationType;

public final class SaveTargetRecordsOption implements OperationOption {
   private final FileDestination successDestination;
   private final FileDestination failureDestination;

   public static SaveTargetRecordsOption no() {
      return new SaveTargetRecordsOption(FileDestination.none(), FileDestination.none());
   }

   public SaveTargetRecordsOption(FileDestination var1, FileDestination var2) {
      this.successDestination = Preconditions.checkNotNull(var1, "successDestination");
      this.failureDestination = Preconditions.checkNotNull(var2, "failureDestination");
   }

   public SaveTargetRecordsOption(Operation var1) {
      this.successDestination = new FileDestination(var1, "target_records_target.success", "target_records_email.success");
      this.failureDestination = new FileDestination(var1, "target_records_target.failure", "target_records_email.failure");
   }

   @Override
   public boolean isApplicableFor(OperationPipeline var1) {
      OperationType var2 = var1.getType();
      if (var2 == BulkLoadOperationType.TYPE) {
         return true;
      }

      if (var2 != WebServiceOperationType.TYPE) {
         return false;
      }

      WebServicePipelineBrowser var3 = new WebServicePipelineBrowser(var1);
      Transformation var4 = var3.getRequest();
      return var4 != null && var4.isPartOfSalesforcePartnerWizard() && var4.getInputStructure().isFlat();
   }

   @Override
   public void applyTo(Operation var1) {
      EntityUtils.storeId(this.successDestination.getTargetId(), TargetId.class, var1, "target_records_target.success");
      EntityUtils.storeId(this.successDestination.getEmailId(), EmailMessageId.class, var1, "target_records_email.success");
      EntityUtils.storeId(this.failureDestination.getTargetId(), TargetId.class, var1, "target_records_target.failure");
      EntityUtils.storeId(this.failureDestination.getEmailId(), EmailMessageId.class, var1, "target_records_email.failure");
   }

   public FileDestination getDestinationForSuccessRecords() {
      return this.successDestination;
   }

   public FileDestination getDestinationForFailureRecords() {
      return this.failureDestination;
   }

   public List<EntityLink> collectLinks(Operation var1, IntegrationEntityLookup var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.addAll(this.successDestination.collectLinks(var1, var2, "target_records_target.success", "target_records_email.success"));
      var3.addAll(this.failureDestination.collectLinks(var1, var2, "target_records_target.failure", "target_records_email.failure"));
      return var3;
   }

   public boolean isSendingData() {
      return !this.successDestination.isEmpty() || !this.failureDestination.isEmpty();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.successDestination, this.failureDestination);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof SaveTargetRecordsOption)) {
         return false;
      }

      SaveTargetRecordsOption var2 = (SaveTargetRecordsOption)var1;
      return this.successDestination.equals(var2.successDestination) && this.failureDestination.equals(var2.failureDestination);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("success", this.successDestination).add("failure", this.failureDestination).toString();
   }
}
