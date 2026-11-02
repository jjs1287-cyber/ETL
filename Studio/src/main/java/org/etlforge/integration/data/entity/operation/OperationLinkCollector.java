package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.LinkUtils;
import org.etlforge.integration.data.entity.link.PropertyBasedEntityLink;
import org.etlforge.integration.data.entity.link.StrongEntityLink;
import org.etlforge.integration.data.entity.operation.options.SaveTargetRecordsOption;
import org.etlforge.integration.data.entity.operation.options.ValidateSourceFileOption;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.util.id.KongaID;

public final class OperationLinkCollector {
   private final Operation operation;
   private final OperationPipeline pipeline;
   private final IntegrationEntityLookup dataLookup;
   private List<EntityLink> links;

   public OperationLinkCollector(Operation var1, OperationPipeline var2, IntegrationEntityLookup var3) {
      this.operation = var1;
      this.pipeline = var2;
      this.dataLookup = var3;
   }

   public List<? extends EntityLink> collect() {
      this.links = Lists.newArrayList();
      this.collectPipelineLinks();
      this.collectScheduleLink();
      this.collectOperationLinks();
      this.collectMessageLinks();
      this.collectSiblingLinks();
      this.collectSalesforceWizardLink();
      this.collectLinksToSuccessAndFailureRecordTargets();
      this.collectLinksToSourceValidationDestinations();
      return this.links;
   }

   private void collectPipelineLinks() {
      if (this.pipeline != null) {
         for (PipelineActivity var2 : this.pipeline.getActivities()) {
            if (var2 instanceof EntityActivity) {
               EntityLink var3 = ((EntityActivity)var2).getLinkToOperation(this.operation);
               if (var3 != null) {
                  this.links.add(var3);
               }
            }
         }
      }
   }

   private void collectScheduleLink() {
      ScheduleId var1 = this.operation.getScheduleId();
      if (var1 != null) {
         Schedule var2 = this.dataLookup.getEntity(var1, Schedule.class);
         if (var2 != null) {
            this.links.add(new PropertyBasedEntityLink(var2, this.operation, "schedule_id"));
         } else {
            LinkUtils.logMissingLink(this.operation, EntityType.Schedule, var1);
         }
      }
   }

   private void collectSiblingLinks() {
      for (OperationId var2 : this.operation.getSiblingOperations()) {
         Operation var3 = this.dataLookup.getEntity(var2, Operation.class);
         if (var3 != null) {
            OperationSiblingLink var4 = new OperationSiblingLink(var3, this.operation);
            this.links.add(var4);
         } else {
            LinkUtils.logMissingLink(this.operation, EntityType.Operation, var2);
         }
      }
   }

   private void collectOperationLinks() {
      OperationId var1 = this.operation.getSuccessOperationId();
      OperationId var2 = this.operation.getFailureOperationId();
      if (var1 != null || var2 != null) {
         OperationOperationLinkFactory var3 = new OperationOperationLinkFactory(this.operation, this.dataLookup);
         if (KongaID.areEqual(var1, var2)) {
            this.onSameSuccessAndFailureOperation(var3);
         } else {
            this.collectSuccessOperationLink(var3);
            this.collectFailureOperationLink(var3);
         }
      }
   }

   private void onSameSuccessAndFailureOperation(OperationOperationLinkFactory var1) {
      EntityLink var2 = var1.createSuccessAndFailureLink();
      if (var2 != null) {
         this.links.add(var2);
      }
   }

   private void collectSuccessOperationLink(OperationOperationLinkFactory var1) {
      EntityLink var2 = var1.createSuccessLink();
      if (var2 != null) {
         this.links.add(var2);
      }
   }

   private void collectFailureOperationLink(OperationOperationLinkFactory var1) {
      EntityLink var2 = var1.createFailureLink();
      if (var2 != null) {
         this.links.add(var2);
      }
   }

   private void collectMessageLinks() {
      EmailMessageId var1 = this.operation.getSuccessMessageId();
      EmailMessageId var2 = this.operation.getFailureMessageId();
      if (var1 != null || var2 != null) {
         OperationMessageLinkFactory var3 = new OperationMessageLinkFactory(this.operation, this.dataLookup);
         if (KongaID.areEqual(var1, var2)) {
            this.onSameSuccessAndFailureMessage(var3);
         } else {
            this.collectSuccessMessageLink(var3);
            this.collectFailureMessageLink(var3);
         }
      }
   }

   private void onSameSuccessAndFailureMessage(OperationMessageLinkFactory var1) {
      EntityLink var2 = var1.createSuccessAndFailureLink();
      if (var2 != null) {
         this.links.add(var2);
      }
   }

   private void collectSuccessMessageLink(OperationMessageLinkFactory var1) {
      EntityLink var2 = var1.createSuccessLink();
      if (var2 != null) {
         this.links.add(var2);
      }
   }

   private void collectFailureMessageLink(OperationMessageLinkFactory var1) {
      EntityLink var2 = var1.createFailureLink();
      if (var2 != null) {
         this.links.add(var2);
      }
   }

   private void collectSalesforceWizardLink() {
      IntegrationEntity var1 = this.dataLookup.getEntity(this.operation.getSalesforceWizardGuid());
      if (var1 != null && !(var1 instanceof SalesforceBulkLoad)) {
         this.links.add(new StrongEntityLink(var1, this.operation));
      }
   }

   private void collectLinksToSuccessAndFailureRecordTargets() {
      SaveTargetRecordsOption var1 = new SaveTargetRecordsOption(this.operation);
      this.links.addAll(var1.collectLinks(this.operation, this.dataLookup));
   }

   private void collectLinksToSourceValidationDestinations() {
      ValidateSourceFileOption var1 = new ValidateSourceFileOption(this.operation);
      if (var1.isValidationEnabled()) {
         this.links.addAll(var1.collectLinks(this.operation, this.dataLookup));
      }
   }
}
