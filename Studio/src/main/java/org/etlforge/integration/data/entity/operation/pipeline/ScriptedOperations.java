package org.etlforge.integration.data.entity.operation.pipeline;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;
import org.etlforge.integration.data.entity.Operation;

public final class ScriptedOperations {
   private final SetMultimap<PipelineActivity, Operation> operations = HashMultimap.create();
   private final SetMultimap<Operation, PipelineActivity> sources = HashMultimap.create();

   void add(PipelineActivity var1, Operation var2) {
      this.operations.put(var1, var2);
      this.sources.put(var2, var1);
   }

   public ImmutableSet<Operation> getOperations() {
      return ImmutableSet.copyOf(this.operations.values());
   }

   public ImmutableSet<PipelineActivity> getSources(Operation var1) {
      return ImmutableSet.copyOf(this.sources.get(var1));
   }
}
