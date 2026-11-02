package org.etlforge.ui.graph.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NodesInsertedEvent extends GraphModelEvent {
   private final Object before;
   private final Object after;
   private final List<Object> inserted;

   public NodesInsertedEvent(GraphModel var1, Object var2, Object var3, Object var4) {
      this(var1, var2, var3, Collections.singletonList(var4));
   }

   public NodesInsertedEvent(GraphModel var1, Object var2, Object var3, List<?> var4) {
      super(var1);
      this.before = var2;
      this.after = var3;
      this.inserted = new ArrayList<>(var4);
   }

   public Object getBefore() {
      return this.before;
   }

   public Object getAfter() {
      return this.after;
   }

   public List<Object> getInserted() {
      return new ArrayList<>(this.inserted);
   }
}
