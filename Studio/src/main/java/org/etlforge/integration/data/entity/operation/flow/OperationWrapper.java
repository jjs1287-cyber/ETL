package org.etlforge.integration.data.entity.operation.flow;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineAdapter;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineEvent;
import org.etlforge.integration.data.entity.operation.pipeline.event.OperationPipelineListener;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.Name;

public final class OperationWrapper extends OperationActivityWrapper implements Disposable {
   public static final String PIPELINE_PROPERTY = "pipeline";
   public static final String NAME_PROPERTY = "name";
   public static final String OPTIONS_PROPERTY = "options";
   public static final String SCHEDULE_PROPERTY = "schedule";
   public static final String SCHEDULE_POLICY_PROPERTY = "schedulePolicy";
   public static final String SUCCESS_OP_PROPERTY = "successOp";
   public static final String FAILURE_OP_PROPERTY = "failureOp";
   public static final String SUCCESS_EMAIL_PROPERTY = "successEmail";
   public static final String FAILURE_EMAIL_PROPERTY = "failureEmail";
   public static final String SIBLINGS_PROPERTY = "siblings";
   public static final String HTTP_ENDPOINTS_PROPERTY = "httpEndpoints";
   private OperationPipeline pipeline;
   private OperationOptions options;
   private String name;
   private ScheduleId scheduleId;
   private SchedulePolicy schedulePolicy;
   private OperationId successOpId;
   private OperationId failureOpId;
   private EmailMessageId successEmailId;
   private EmailMessageId failureEmailId;
   private List<OperationId> siblings;
   private List<OperationId> siblingsMemento;
   private ImmutableSet<HttpEndpoint> httpEndpoints;
   private final OperationWrapper.OperationListener opListener;
   private final OperationWrapper.OriginalPipelineListener originalPipelineListener;

   public OperationWrapper(Operation var1) {
      this(var1, new OperationPipeline(var1.getPipeline()));
   }

   private OperationWrapper(Operation var1, OperationPipeline var2) {
      super(var1);
      this.pipeline = var2;
      this.opListener = new OperationWrapper.OperationListener();
      var1.addPropertyChangeListener(this.opListener);
      this.originalPipelineListener = new OperationWrapper.OriginalPipelineListener();
      var1.getPipeline().addPipelineListener(this.originalPipelineListener);
      this.options = var1.getOperationOptions();
      this.name = var1.getName();
      this.scheduleId = var1.getScheduleId();
      this.schedulePolicy = var1.getSchedulePolicy();
      this.successOpId = var1.getSuccessOperationId();
      this.failureOpId = var1.getFailureOperationId();
      this.successEmailId = var1.getSuccessMessageId();
      this.failureEmailId = var1.getFailureMessageId();
      this.siblings = Lists.newArrayList(var1.getSiblingOperations());
      this.httpEndpoints = ImmutableSet.of();
   }

   @Override
   public void dispose() {
      this.getActivity().removePropertyChangeListener(this.opListener);
      this.getActivity().getPipeline().removePipelineListener(this.originalPipelineListener);

      for (PipelineActivity var2 : this.pipeline.getActivities()) {
         var2.dispose();
      }
   }

   public void applyToOperation() throws IllegalNameException {
      this.originalPipelineListener.blocked = true;
      this.opListener.blocked = true;

      try {
         Operation var1 = this.getActivity();
         var1.setName(this.name);
         var1.setOperationType(this.pipeline.getType());
         var1.setPipeline(this.pipeline);
         var1.setOperationOptions(this.options);
         var1.setScheduleId(this.scheduleId);
         var1.setSchedulePolicy(this.schedulePolicy);
         var1.setSuccessOperationId(this.successOpId);
         var1.setFailureOperationId(this.failureOpId);
         var1.setSuccessMessageId(this.successEmailId);
         var1.setFailureMessageId(this.failureEmailId);
         var1.setSiblingOperations(this.siblings);
      } finally {
         this.originalPipelineListener.blocked = false;
         this.opListener.blocked = false;
      }
   }

   public Operation getActivity() {
      return (Operation)super.getActivity();
   }

   public OperationOptions getOptions() {
      return this.options;
   }

   public void setOptions(OperationOptions var1) {
      OperationOptions var2 = this.options;
      this.options = var1;
      this.fireChange("options", var2, var1);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      String var2 = this.name;
      this.name = var1;
      this.fireChange("name", var2, var1);
   }

   public Schedule getSchedule() {
      return ProjectUtils.getLookup(this.getActivity()).getEntity(this.scheduleId, Schedule.class);
   }

   public ScheduleId getScheduleId() {
      return this.scheduleId;
   }

   public void setScheduleId(ScheduleId var1) {
      ScheduleId var2 = this.scheduleId;
      this.scheduleId = var1;
      this.fireChange("schedule", var2, var1);
   }

   public SchedulePolicy getSchedulePolicy() {
      return this.schedulePolicy;
   }

   public void setSchedulePolicy(SchedulePolicy var1) {
      SchedulePolicy var2 = this.schedulePolicy;
      this.schedulePolicy = var1;
      this.fireChange("schedulePolicy", var2, var1);
   }

   public void setSuccessOperationId(OperationId var1) {
      OperationId var2 = this.successOpId;
      this.successOpId = var1;
      this.fireChange("successOp", var2, var1);
   }

   public OperationId getSuccessOperationId() {
      return this.successOpId;
   }

   public void setFailureOperationId(OperationId var1) {
      OperationId var2 = this.failureOpId;
      this.failureOpId = var1;
      this.fireChange("failureOp", var2, var1);
   }

   public OperationId getFailureOperationId() {
      return this.failureOpId;
   }

   public void setSuccessEmailId(EmailMessageId var1) {
      EmailMessageId var2 = this.successEmailId;
      this.successEmailId = var1;
      this.fireChange("successEmail", var2, var1);
   }

   public EmailMessageId getSuccessEmailId() {
      return this.successEmailId;
   }

   public void setFailureEmailId(EmailMessageId var1) {
      EmailMessageId var2 = this.failureEmailId;
      this.failureEmailId = var1;
      this.fireChange("failureEmail", var2, var1);
   }

   public EmailMessageId getFailureEmailId() {
      return this.failureEmailId;
   }

   public List<OperationId> getSiblingIds() {
      return Lists.newArrayList(this.siblings);
   }

   public void setSiblings(Collection<Operation> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Operation var4 : var1) {
         var2.add(var4.getID());
      }

      this.setSiblingIds(var2);
   }

   public void setSiblingIds(List<OperationId> var1) {
      List var2 = this.siblings;
      this.siblings = Lists.newArrayList(var1);
      this.fireChange("siblings", var2, var1);
   }

   public OperationPipeline getPipeline() {
      return this.pipeline;
   }

   public void setPipeline(OperationPipeline var1) {
      if (!this.pipeline.equals(var1)) {
         if (this.pipeline.getType() == HostedWebServiceOperationType.TYPE && var1.getType() != HostedWebServiceOperationType.TYPE) {
            this.siblingsMemento = this.siblings;
            this.siblings = Lists.newArrayList();
         } else if (this.pipeline.getType() != HostedWebServiceOperationType.TYPE && var1.getType() == HostedWebServiceOperationType.TYPE) {
            if (this.siblingsMemento != null) {
               this.siblings = this.siblingsMemento;
               this.siblingsMemento = null;
            }

            this.siblingsMemento = null;
         }

         OperationPipeline var2 = this.pipeline;

         for (OperationPipelineListener var4 : var2.getAllPipelineListeners()) {
            var2.removePipelineListener(var4);
            var1.addPipelineListener(var4);
         }

         this.pipeline = var1;
         this.fireChange("pipeline", var2, var1);
      }
   }

   public boolean removeOutgoingRouteAndUpdateCorrespondingProperty(Route var1) {
      if (super.removeOutgoingRoute(var1)) {
         boolean var2 = var1.getTo() instanceof OperationWrapper;
         switch (var1.getType()) {
            case SUCCESS:
               if (var2) {
                  this.setSuccessOperationId(null);
               } else {
                  this.setSuccessEmailId(null);
               }
               break;
            case FAILURE:
               if (var2) {
                  this.setFailureOperationId(null);
               } else {
                  this.setFailureEmailId(null);
               }
               break;
            case SIBLING:
               this.removeSiblingOperation(((OperationWrapper)var1.getTo()).getActivity());
               break;
            case SCRIPTED:
               return true;
         }
      }

      return false;
   }

   public void removeSiblingOperation(Operation var1) {
      ArrayList var2 = Lists.newArrayList(this.siblings);
      this.siblings.remove(var1.getID());
      this.fireChange("siblings", var2, this.siblings);
   }

   public void addPipelineListener(OperationPipelineListener var1) {
      this.pipeline.addPipelineListener(var1);
   }

   public void removePipelineListener(OperationPipelineListener var1) {
      this.pipeline.removePipelineListener(var1);
   }

   public boolean isDirty() {
      Operation var1 = this.getActivity();
      return !this.name.equals(var1.getName())
         || !this.pipeline.equals(var1.getPipeline())
         || !this.options.equals(var1.getOperationOptions())
         || !equals(this.scheduleId, var1.getScheduleId())
         || !equals(this.schedulePolicy, var1.getSchedulePolicy())
         || !equals(this.successOpId, var1.getSuccessOperationId())
         || !equals(this.failureOpId, var1.getFailureOperationId())
         || !equals(this.successEmailId, var1.getSuccessMessageId())
         || !equals(this.failureEmailId, var1.getFailureMessageId())
         || !var1.getSiblingOperations().equals(this.siblings);
   }

   public boolean isSchedulable() {
      return this.pipeline.getType().isStartedByEngine() && this.isSourceSchedulable();
   }

   private boolean isSourceSchedulable() {
      OperationPipelineBrowser var1 = new OperationPipelineBrowser(this.pipeline);
      Source var2 = var1.getSource();
      return var2 != SystemSource.SOURCE_FROM_PREVIOUS_OPERATION && var2 != SystemSource.FROM_HTTP_ENDPOINT;
   }

   public void setHttpEndpointTriggers(Iterable<HttpEndpoint> var1) {
      ImmutableSet var2 = this.httpEndpoints;
      this.httpEndpoints = ImmutableSet.copyOf(var1);
      this.fireChange("httpEndpoints", var2, this.httpEndpoints);
   }

   public ImmutableSet<HttpEndpoint> getHttpEndpointTriggers() {
      return this.httpEndpoints;
   }

   public boolean isTriggeredByHttpEndpoint() {
      return !this.httpEndpoints.isEmpty();
   }

   public boolean isOperationTypeChangeable() {
      return !this.getActivity().isPartOfSalesforceWizard();
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }

   private class OperationListener implements PropertyChangeListener {
      public boolean blocked;

      private OperationListener() {
      }

      @Override
      public void propertyChange(final PropertyChangeEvent var1) {
         if (!this.blocked) {
            final String var2 = var1.getPropertyName();
            final Object var3 = var1.getNewValue();
            UiUtils.runOnEventThread(new Runnable() {
               @Override
               public void run() {
                  OperationListener.this.handlePropertyChange(var1, var2, var3);
               }
            });
         }
      }

      private void handlePropertyChange(PropertyChangeEvent var1, String var2, Object var3) {
         if ("Name".equals(var2)) {
            OperationWrapper.this.setName(((Name)var1.getNewValue()).getName());
         } else if ("Operation#OperationConfiguration".equals(var2)) {
            OperationWrapper.this.setOptions((OperationOptions)var3);
         } else if ("schedule_id".equals(var2)) {
            OperationWrapper.this.setScheduleId((ScheduleId)var3);
         } else if (!"schedule_policy".equals(var2)) {
            if ("success_operation_id".equals(var2)) {
               OperationWrapper.this.setSuccessOperationId((OperationId)var3);
            } else if ("failure_operation_id".equals(var2)) {
               OperationWrapper.this.setFailureOperationId((OperationId)var3);
            } else if ("success_message_id".equals(var2)) {
               OperationWrapper.this.setSuccessEmailId((EmailMessageId)var3);
            } else if ("failure_message_id".equals(var2)) {
               OperationWrapper.this.setFailureEmailId((EmailMessageId)var3);
            } else if ("Operation#Siblings".equals(var2)) {
               OperationWrapper.this.setSiblingIds((List<OperationId>)var3);
            }
         }
      }
   }

   private class OriginalPipelineListener extends OperationPipelineAdapter {
      public boolean blocked;

      private OriginalPipelineListener() {
      }

      @Override
      public void pipelineChanged(OperationPipelineEvent var1) {
         if (!this.blocked) {
            OperationWrapper.this.pipeline.replaceWith(var1.getSource());
         }
      }
   }
}
