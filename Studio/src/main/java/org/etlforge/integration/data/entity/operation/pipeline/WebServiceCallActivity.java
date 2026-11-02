package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.filter.EntityFilterProvider;
import org.etlforge.util.id.KongaGuid;

public final class WebServiceCallActivity extends EntityActivity<WebServiceCall> {
   public WebServiceCallActivity(String var1) {
      super(PipelineActivityType.WEBSERVICECALL, WebServiceCall.class, var1);
   }

   public WebServiceCallActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.WEBSERVICECALL, WebServiceCall.class, var1, var2);
   }

   private WebServiceCallActivity(WebServiceCall var1, String var2) {
      super(PipelineActivityType.WEBSERVICECALL, WebServiceCall.class, var1, var2);
   }

   public static WebServiceCallActivity newInstance(WebServiceCall var0, String var1) {
      return var0 != null ? new WebServiceCallActivity(var0, var1) : new WebServiceCallActivity(var1);
   }

   public WebServiceCallActivity clone() {
      return (WebServiceCallActivity)super.clone();
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.WebServiceCall.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.WebServiceCall.getPlural();
   }

   public EntityFilterProvider<WebServiceCall> getWebServiceCallFilter(OperationPipeline var1) {
      OperationType var2 = var1.getType();
      if (var2 == WebServiceOperationType.TYPE) {
         return WebServiceOperationType.TYPE.getFunctionCallFilterProvider(var1);
      }

      assert var2 == HostedWebServiceOperationType.TYPE;
      return HostedWebServiceOperationType.TYPE.getWebServiceCallActivityFilterProvider(var1);
   }
}
