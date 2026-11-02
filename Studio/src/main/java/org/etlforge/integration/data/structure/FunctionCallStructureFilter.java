package org.etlforge.integration.data.structure;

import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.function.Function;

public final class FunctionCallStructureFilter implements Predicate<DataStructure> {
   private final IntegrationEntityId functionId;

   public FunctionCallStructureFilter(Function var1) {
      this.functionId = var1.getFunctionID();
   }

   public boolean apply(DataStructure var1) {
      return var1 instanceof FunctionCallStructure && this.functionId.equals(((FunctionCallStructure)var1).getFunctionCallId());
   }
}
