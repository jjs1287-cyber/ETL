package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.structure.DataStructureType;

public final class OperationSourceChanger {
   private final Operation op;

   public OperationSourceChanger(Operation var1) {
      this.op = Preconditions.checkNotNull(var1);
   }

   public List<IntegrationEntity> setSource(Source var1) {
      if (this.isSameAsCurrentSource(var1)) {
         return Collections.emptyList();
      }

      OperationPipeline var2 = new OperationPipeline(this.op.getPipeline());
      OperationPipelineBrowser var3 = new OperationPipelineBrowser(var2);
      Transformation var4 = this.getTransformation(var3);
      this.checkCompatibility(var1, var4);
      ArrayList var5 = Lists.newArrayList();
      var3.setSource(var1);
      var5.add(this.op);
      boolean var6 = this.updateTransformationIfNeeded(var4, var1);
      if (var6) {
         var5.add(var4);
      }

      this.op.setPipeline(var2);
      return var5;
   }

   private boolean isSameAsCurrentSource(Source var1) {
      SourceId var2 = this.op.getSourceId();
      return var1 == null ? var2 == null : var1.getID().equals(var2);
   }

   private Transformation getTransformation(OperationPipelineBrowser var1) {
      return var1.getFirstContent(TransformationActivity.class, Transformation.class);
   }

   private void checkCompatibility(Source var1, Transformation var2) throws IllegalArgumentException {
      OperationType var3 = this.op.getOperationType();
      Preconditions.checkArgument(var1 == null || var3.isCompatibleWith(var1), "Incompatible Source for Operation of type " + var3);
      if (var2 != null) {
         DataStructureType var4 = var2.getInputStructure().getStructureType();
         if (var4 == DataStructureType.None) {
            Preconditions.checkArgument(var1 == null, "The Transformation does not have any input, so no Source allowed.");
         } else if (var1 == null) {
            Preconditions.checkArgument(!var4.isTiedToSpecificLocationType(), "The Transformation requires a Source");
         } else {
            EnumSet var5 = var4.getCompatibleLocationTypes();
            Preconditions.checkArgument(
               var5.contains(var1.getLocation().getDataLocationType()), "Incompatible Source, only Sources of type " + var5 + " allowed"
            );
         }
      }
   }

   private boolean updateTransformationIfNeeded(Transformation var1, Source var2) {
      if (var1 == null) {
         return false;
      }

      DataStructureType var3 = var1.getInputStructure().getStructureType();
      if (!var3.isTiedToSpecificLocationType()) {
         return false;
      }

      var3.updateInputStructureWithNewSourceLocation(var1, var2);
      return true;
   }
}
