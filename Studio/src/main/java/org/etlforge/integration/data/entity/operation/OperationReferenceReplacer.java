package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.operation.options.FileDestination;
import org.etlforge.integration.data.entity.operation.options.SaveTargetRecordsOption;
import org.etlforge.integration.data.entity.operation.options.ValidateSourceFileOption;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public final class OperationReferenceReplacer {
   private final Operation op;
   private final ReferenceReplacementService service;

   public OperationReferenceReplacer(Operation var1, ReferenceReplacementService var2) {
      this.op = var1;
      this.service = var2;
   }

   public void replaceReferences() {
      this.replaceSchedule();
      this.replaceMessages();
      this.replaceChain();
      this.replaceSiblings();
      this.replacePipeline();
      this.replaceSuccessAndFailureRecordTargets();
      this.replaceSourceFileValidationDestinations();
      this.replaceSalesforceWizard();
   }

   private void replaceSchedule() {
      ScheduleId var1 = this.op.getScheduleId();
      if (var1 != null) {
         Schedule var2 = this.service.getReplacement(var1, Schedule.class);
         this.op.setSchedule(var2);
      }
   }

   private void replaceMessages() {
      EmailMessageId var1 = this.op.getSuccessMessageId();
      if (var1 != null) {
         EmailMessage var2 = this.service.getReplacement(var1, EmailMessage.class);
         this.op.setSuccessMessage(var2);
      }

      EmailMessageId var4 = this.op.getFailureMessageId();
      if (var4 != null) {
         EmailMessage var3 = this.service.getReplacement(var4, EmailMessage.class);
         this.op.setFailureMessage(var3);
      }
   }

   private void replaceChain() {
      OperationId var1 = this.op.getSuccessOperationId();
      if (var1 != null) {
         Operation var2 = this.service.getReplacement(var1, Operation.class);
         this.op.setSuccessOperation(var2);
      }

      OperationId var4 = this.op.getFailureOperationId();
      if (var4 != null) {
         Operation var3 = this.service.getReplacement(var4, Operation.class);
         this.op.setFailureOperation(var3);
      }
   }

   private void replaceSiblings() {
      ArrayList var1 = Lists.newArrayList();

      for (OperationId var3 : this.op.getSiblingOperations()) {
         Operation var4 = this.service.getReplacement(var3, Operation.class);
         if (var4 != null) {
            var1.add(var4.getID());
         }
      }

      this.op.setSiblingOperations(var1);
   }

   private void replacePipeline() {
      OperationPipeline var1 = this.op.getPipeline();
      OperationPipeline var2 = new OperationPipeline(var1);

      for (PipelineActivity var4 : var2.getActivities()) {
         this.visit(var4);
      }

      this.op.setPipeline(var2);
   }

   private void visit(PipelineActivity var1) {
      if (var1 instanceof EntityActivity) {
         EntityActivity var2 = (EntityActivity)var1;
         this.visit(var2);
      }
   }

   private void visit(EntityActivity var1) {
      IntegrationEntity var2 = (IntegrationEntity)var1.getContent();
      if (var2 != null) {
         IntegrationEntity var3 = this.service.getReplacement(var2.getID());
         if (var3 == null) {
            var1.setContent((IntegrationEntity)null);
         } else {
            var1.setContent(var3);
         }
      }
   }

   private void replaceSuccessAndFailureRecordTargets() {
      SaveTargetRecordsOption var1 = new SaveTargetRecordsOption(this.op);
      if (var1.isSendingData()) {
         FileDestination var2 = this.replaceValidationRecordDestination(var1.getDestinationForSuccessRecords());
         FileDestination var3 = this.replaceValidationRecordDestination(var1.getDestinationForFailureRecords());
         SaveTargetRecordsOption var4 = new SaveTargetRecordsOption(var2, var3);
         var4.applyTo(this.op);
      }
   }

   private <T extends IntegrationEntity> T getReplacement(Optional<? extends IntegrationEntityId> var1, Class<T> var2) {
      return var1.isPresent() ? this.service.getReplacement((IntegrationEntityId)var1.get(), var2) : null;
   }

   private void replaceSourceFileValidationDestinations() {
      ValidateSourceFileOption var1 = new ValidateSourceFileOption(this.op);
      if (var1.isValidationEnabled() && var1.isSendingData()) {
         FileDestination var2 = this.replaceValidationRecordDestination(var1.getDestinationForValidRecords());
         FileDestination var3 = this.replaceValidationRecordDestination(var1.getDestinationForInvalidRecords());
         ValidateSourceFileOption var4 = ValidateSourceFileOption.validate(var2, var3);
         var4.applyTo(this.op);
      }
   }

   private FileDestination replaceValidationRecordDestination(FileDestination var1) {
      return var1.isEmpty()
         ? var1
         : new FileDestination(this.getReplacement(var1.getTargetId(), Target.class), this.getReplacement(var1.getEmailId(), EmailMessage.class));
   }

   private void replaceSalesforceWizard() {
      IntegrationEntityId var1 = this.op.getSalesforceWizardGuid();
      if (var1 != null) {
         IntegrationEntity var2 = this.service.getReplacement(var1);
         this.op.setSalesforceWizardGuid((SalesforceWizardEntity)var2);
      }
   }
}
