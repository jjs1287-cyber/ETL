package org.etlforge.integration.data.structure;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.functors.EntityFilter;

public final class FunctionCallTransformationFilter implements EntityFilter {
   private final FunctionCallStructureFilter structureFilter;

   public FunctionCallTransformationFilter(Function var1) {
      this.structureFilter = new FunctionCallStructureFilter(var1);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      if (!(var1 instanceof Transformation)) {
         return false;
      }

      Transformation var2 = (Transformation)var1;
      return this.structureFilter.apply(var2.getInputStructure()) || this.structureFilter.apply(var2.getOutputStructure());
   }
}
