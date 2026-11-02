package org.etlforge.integration.data.entity.operation;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Iterator;

public final class OperationTypes implements Iterable<OperationType> {
   private final ImmutableSet<OperationType> types;

   public OperationTypes(Iterable<OperationType> var1, Iterable<OperationType> var2) {
      this.types = new Builder<OperationType>().addAll(var1).addAll(var2).build();
   }

   @Override
   public Iterator<OperationType> iterator() {
      return this.types.iterator();
   }
}
