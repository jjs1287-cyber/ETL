package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.util.id.KongaGuid;

public final class TransformationActivity extends EntityActivity<Transformation> {
   public TransformationActivity(String var1) {
      super(PipelineActivityType.TRANSFORMATION, Transformation.class, var1);
   }

   public TransformationActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.TRANSFORMATION, Transformation.class, var1, var2);
   }

   private TransformationActivity(Transformation var1, String var2) {
      super(PipelineActivityType.TRANSFORMATION, Transformation.class, var1, var2);
   }

   public static TransformationActivity newInstance(Transformation var0, String var1) {
      return var0 != null ? new TransformationActivity(var0, var1) : new TransformationActivity(var1);
   }

   public TransformationActivity clone() {
      return (TransformationActivity)super.clone();
   }

   public boolean isPassThrough() {
      return Transformation.isPassThrough(this.getContent());
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public boolean canHaveScriptedLinks() {
      return true;
   }

   @Override
   public String getTypeName() {
      return EntityType.Transformation.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.Transformation.getPlural();
   }
}
