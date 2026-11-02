package org.etlforge.ui.graph.model;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public final class NodesRemovedEvent extends GraphModelEvent {
   private final List<Object> removed;

   public NodesRemovedEvent(GraphModel var1, Object var2) {
      this(var1, Collections.singletonList(var2));
   }

   public NodesRemovedEvent(GraphModel var1, List<?> var2) {
      super(var1);
      this.removed = Lists.newArrayList(var2);
   }

   public List<Object> getRemoved() {
      return Lists.newArrayList(this.removed);
   }
}
