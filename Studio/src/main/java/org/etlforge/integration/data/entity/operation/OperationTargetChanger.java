package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructureType;

public final class OperationTargetChanger {
   private final Operation op;

   public OperationTargetChanger(Operation var1) {
      this.op = Preconditions.checkNotNull(var1);
   }

   public List<IntegrationEntity> setTarget(Target var1) {
      if (this.isSameAsCurrentTarget(var1)) {
         return Collections.emptyList();
      }

      OperationPipeline var2 = new OperationPipeline(this.op.getPipeline());
      OperationPipelineBrowser var3 = new OperationPipelineBrowser(var2);
      Transformation var4 = this.getTransformation(var3);
      this.checkCompatibility(var1, var4);
      ArrayList var5 = Lists.newArrayList();
      var3.setTarget(var1);
      var5.add(this.op);
      boolean var6 = this.updateTransformationIfNeeded(var4, var1);
      if (var6) {
         var5.add(var4);
      }

      this.op.setPipeline(var2);
      return var5;
   }

   private boolean isSameAsCurrentTarget(Target var1) {
      TargetId var2 = this.op.getTargetId();
      return var1 == null ? var2 == null : var1.getID().equals(var2);
   }

   private Transformation getTransformation(OperationPipelineBrowser var1) {
      return var1.getLastContent(TransformationActivity.class, Transformation.class);
   }

   private void checkCompatibility(Target var1, Transformation var2) throws IllegalArgumentException {
      OperationType var3 = this.op.getOperationType();
      Preconditions.checkArgument(var1 == null || var3.isCompatibleWith(var1), "Incompatible Target for Operation of type " + var3);
      if (var2 != null) {
         DataStructureType var4 = var2.getOutputStructure().getStructureType();
         if (var1 == null) {
            Preconditions.checkArgument(!var4.isTiedToSpecificLocationType(), "The Transformation requires a Target");
         } else {
            EnumSet var5 = var4.getCompatibleLocationTypes();
            Preconditions.checkArgument(
               var5.contains(var1.getLocation().getDataLocationType()), "Incompatible Target, only Targets of type " + var5 + " allowed"
            );
         }
      }
   }

   private boolean updateTransformationIfNeeded(Transformation var1, Target var2) {
      if (var1 == null) {
         return false;
      }

      DataStructureType var3 = var1.getOutputStructure().getStructureType();
      if (!var3.isTiedToSpecificLocationType()) {
         return false;
      }

      var3.updateOutputStructureWithNewTargetLocation(var1, var2);
      return true;
   }
}
