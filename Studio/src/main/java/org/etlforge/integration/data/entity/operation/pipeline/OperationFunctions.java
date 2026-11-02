package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.function.Function;

public final class OperationFunctions {
   public static final OperationFunctions NONE = new OperationFunctions(new ArrayList<>(), null);
   private final ImmutableList<Function> functions;
   private final TransformationId firstTfId;

   public OperationFunctions(List<Function> var1, TransformationId var2) {
      this.functions = ImmutableList.copyOf(var1);
      this.firstTfId = var2;
   }

   public TransformationId getFirstTransformationId() {
      return this.firstTfId;
   }

   public List<IntegrationId> getFunctionIds() {
      ArrayList var1 = Lists.newArrayList();

      for (Function var3 : this.functions) {
         var1.add(var3.getFunctionID());
      }

      return var1;
   }
}
