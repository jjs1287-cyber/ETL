package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptActivity;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TargetActivity;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.project.ProjectUtils;

public class WizardItemBrowser {
   private final SalesforceWizardEntity item;
   private final IntegrationEntityLookup lookup;

   public WizardItemBrowser(SalesforceWizardEntity var1, IntegrationEntityLookup var2) {
      this.item = Preconditions.checkNotNull(var1);
      this.lookup = Preconditions.checkNotNull(var2);
   }

   public WizardItemBrowser(SalesforceWizardEntity var1) {
      this.item = Preconditions.checkNotNull(var1);
      this.lookup = this.getProjectLookup();
   }

   private IntegrationEntityLookup getProjectLookup() {
      IntegrationEntityLookup var1 = ProjectUtils.getLookup((IntegrationEntity)this.item);
      if (var1 == null) {
         var1 = IntegrationEntityLookup.NULL;
      }

      return var1;
   }

   public SalesforceWizardEntity getWizardItem() {
      return this.item;
   }

   @Nullable
   public Transformation getTransformation(SalesforceWizardRole.Position var1) {
      switch (var1) {
         case REQUEST:
            return this.getRequestTransformation();
         case RESPONSE:
            return this.getResponseTransformation();
         default:
            throw new AssertionError(var1);
      }
   }

   @Nullable
   public Transformation getRequestTransformation() {
      WebServicePipelineBrowser var1 = this.createOperationBrowser().orNull();
      return var1 != null ? var1.getRequest() : null;
   }

   private Optional<WebServicePipelineBrowser> createOperationBrowser() {
      OperationPipeline var1 = this.getPipeline();
      return isWebServiceCall(var1) ? Optional.of(new WebServicePipelineBrowser(var1)) : Optional.absent();
   }

   private static boolean isWebServiceCall(@Nullable OperationPipeline var0) {
      return var0 != null && var0.getType() == WebServiceOperationType.TYPE;
   }

   @Nullable
   private OperationPipeline getPipeline() {
      Operation var1 = this.getImplementingOperation();
      return var1 == null ? null : var1.getPipeline();
   }

   @Nullable
   public Transformation getResponseTransformation() {
      WebServicePipelineBrowser var1 = this.createOperationBrowser().orNull();
      return var1 != null ? var1.getResponse() : null;
   }

   @Nullable
   public Operation getImplementingOperation() {
      return this.lookup.getEntity(this.item.getImplementingOperationId(), Operation.class);
   }

   @Nullable
   public WebServiceCall getWebServiceCall() {
      WebServicePipelineBrowser var1 = this.createOperationBrowser().orNull();
      return var1 != null ? var1.getFunction() : null;
   }

   @Nullable
   public SalesforceConnector getSalesforceConnector() {
      return this.lookup.getEntity(this.item.getConnectorId(), SalesforceConnector.class);
   }

   @Nullable
   public Schedule getSchedule() {
      Operation var1 = this.getImplementingOperation();
      return var1 != null ? this.lookup.getEntity(var1.getScheduleId(), Schedule.class) : null;
   }

   @Nullable
   public Script getLoginScript() {
      OperationPipeline var1 = this.getPipeline();
      if (var1 == null) {
         return null;
      }

      for (PipelineActivity var3 : var1.getActivities()) {
         if (var3 instanceof ScriptActivity) {
            Script var4 = ((ScriptActivity)var3).getContent();
            if (var4 != null && var4.isPartOfSalesforceWizard()) {
               return var4;
            }
         }
      }

      return null;
   }

   @Nullable
   public Source getOperationSource() {
      OperationPipeline var1 = this.getPipeline();
      if (var1 == null) {
         return null;
      }

      SourceActivity var2 = var1.getFirstActivityOfType(SourceActivity.class);
      return var2 != null ? var2.getContent() : null;
   }

   @Nullable
   public Target getOperationTarget() {
      OperationPipeline var1 = this.getPipeline();
      if (var1 == null) {
         return null;
      }

      TargetActivity var2 = var1.getFirstActivityOfType(TargetActivity.class);
      return var2 != null ? var2.getContent() : null;
   }

   public Set<IntegrationEntity> getItemsToRename() {
      HashSet var1 = Sets.newHashSet();
      Operation var2 = this.getImplementingOperation();
      if (var2 != null) {
         var1.add(var2);
         this.collectWizardSpecificPipelineObjects(var1);
      }

      return var1;
   }

   private void collectWizardSpecificPipelineObjects(Set<IntegrationEntity> var1) {
      WebServicePipelineBrowser var2 = this.createOperationBrowser().orNull();
      if (var2 != null) {
         Transformation var3 = var2.getRequest();
         if (var3 != null) {
            var1.add(var3);
         }

         Transformation var4 = var2.getResponse();
         if (var4 != null) {
            var1.add(var4);
         }
      }
   }

   public Set<IntegrationEntity> getAllImplementingItems() {
      HashSet var1 = Sets.newHashSet();
      Operation var2 = this.getImplementingOperation();
      if (var2 != null) {
         this.collectOperationItems(var2, var1);
      }

      return var1;
   }

   private void collectOperationItems(Operation var1, Set<IntegrationEntity> var2) {
      DependencyStore var3 = ProjectUtils.getProject(var1).getCurrentDependencies();

      try {
         this.collectItems(var1, var3, var2);
      } finally {
         var3.dispose();
      }
   }

   private void collectItems(IntegrationEntity var1, DependencyStore var2, Set<IntegrationEntity> var3) {
      if (var3.add(var1)) {
         for (IntegrationEntity var5 : var2.getRequiredObjects(var1)) {
            this.collectItems(var5, var2, var3);
         }
      }
   }

   public <T> Optional<T> mapRequest(Function<Transformation, T> var1) {
      Transformation var2 = this.getRequestTransformation();
      return this.map(var2, var1);
   }

   private <T> Optional<T> map(Transformation var1, Function<Transformation, T> var2) {
      if (var1 == null) {
         return Optional.absent();
      }

      Object var3 = var2.apply(var1);
      return var3 == null ? Optional.absent() : Optional.of((T)var3);
   }

   public <T> Optional<T> mapResponse(Function<Transformation, T> var1) {
      Transformation var2 = this.getResponseTransformation();
      return this.map(var2, var1);
   }
}
