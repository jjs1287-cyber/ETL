package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.function.Function;

public abstract class FunctionCallPipelineBrowser<T extends IntegrationEntity & Function, A extends EntityActivity<T>> extends OperationPipelineBrowser {
   private final Class<T> functionClass;
   private final Class<A> activityClass;
   private final Predicate<EntityType> compatibilityFilter;

   protected FunctionCallPipelineBrowser(OperationPipeline var1, Class<T> var2, Class<A> var3, Predicate<EntityType> var4) {
      super(var1);
      this.functionClass = var2;
      this.activityClass = var3;
      this.compatibilityFilter = var4;
   }

   public final boolean isCompatibleWith(EntityType var1) {
      return this.compatibilityFilter.apply(var1);
   }

   public final T getFunction() {
      return this.getFirstContent(this.activityClass, this.functionClass);
   }

   public final A getFunctionActivity() {
      return this.getPipeline().getFirstActivityOfType(this.activityClass);
   }

   public final void setFunction(T var1) {
      EntityActivity var2 = this.getFunctionActivity();
      var2.setContent(var1);
   }

   public final Transformation getRequest() {
      TransformationActivity var1 = this.getRequestActivity();
      return var1 == null ? null : var1.getContent();
   }

   public final TransformationActivity getRequestActivity() {
      EntityActivity var1 = this.getFunctionActivity();
      OperationPipeline var2 = this.getPipeline();

      for (PipelineActivity var3 = var2.getPreviousActivity(var1); var3 != null; var3 = var2.getPreviousActivity(var3)) {
         if (var3 instanceof TransformationActivity) {
            return (TransformationActivity)var3;
         }
      }

      return null;
   }

   public final void setRequest(Transformation var1) {
      this.getRequestActivity().setContent(var1);
   }

   public final Transformation getResponse() {
      EntityActivity var1 = this.getFunctionActivity();
      return this.getNextContent(var1, TransformationActivity.class, Transformation.class);
   }

   public final TransformationActivity getResponseActivity() {
      EntityActivity var1 = this.getFunctionActivity();
      return this.getNextActivity(var1, TransformationActivity.class);
   }

   public final void setResponse(Transformation var1) {
      this.getResponseActivity().setContent(var1);
   }
}
