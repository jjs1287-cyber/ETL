package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.EnumSet;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.SystemTarget;
import org.etlforge.integration.data.entity.SystemTransformation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallTemplate;
import org.etlforge.integration.data.entity.operation.pipeline.ws.WebServicePipelineBrowser;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class WebServiceOperationType extends FunctionCallOperationType<WebServiceCall, WebServiceCallActivity> {
   public static final WebServiceOperationType TYPE = new WebServiceOperationType();

   private WebServiceOperationType() {
      super(6, "WebServiceCall", "Web Service Call", "Call a web service", WebServiceCall.class, PipelineActivityType.WEBSERVICECALL);
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<WebServiceCall, WebServiceCallActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<WebServiceCall, WebServiceCallActivity>() {
         public WebServicePipelineBrowser createBrowser(OperationPipeline var1) {
            return new WebServicePipelineBrowser(var1);
         }
      };
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new WebServiceCallTemplate().getPipeline();
   }

   @Override
   public EntityFilter getSourceFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Source ? var1 != SystemSource.WEB_SERVICE_CALL : false;
         }
      };
   }

   @Override
   public EntityFilter getTargetFilter() {
      return new EntityFilter() {
         @Override
         public boolean apply(IntegrationEntity var1) {
            return var1 instanceof Target ? var1 != SystemTarget.WEB_SERVICE_CALL : false;
         }
      };
   }

   @Override
   public ImmutableList<SystemTransformation> getApplicableSystemTransformations(Operation var1, OperationPipeline var2, TransformationActivity var3) {
      Builder var4 = ImmutableList.builder();
      WebServicePipelineBrowser var5 = new WebServicePipelineBrowser(var2);
      if (var3 == var5.getRequestActivity()) {
         if (this.isQueryWizard(var1)) {
            var4.add(SystemTransformation.SALESFORCE_QUERY_REQUEST);
         }
      } else if (var3 == var5.getResponseActivity()) {
         if (this.isSalesforceModifierWizard(var1)) {
            var4.add(SystemTransformation.SALESFORCE_STATUS_RESPONSE);
         }

         var4.add(SystemTransformation.PASS_THROUGH);
      }

      return var4.build();
   }

   private boolean isQueryWizard(Operation var1) {
      return var1.isImplementingSalesforcePartnerWizard(EnumSet.of(SfdcMethod.QUERY, SfdcMethod.QUERY_ALL));
   }

   private boolean isSalesforceModifierWizard(Operation var1) {
      return var1.isImplementingSalesforcePartnerWizard(EnumSet.of(SfdcMethod.UPSERT, SfdcMethod.UPDATE, SfdcMethod.CREATE, SfdcMethod.DELETE));
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return var1 == EntityType.WebServiceCall;
   }
}
