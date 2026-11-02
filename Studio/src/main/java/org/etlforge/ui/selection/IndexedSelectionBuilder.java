package org.etlforge.ui.selection;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import java.util.List;
import org.etlforge.system.ArgCheck;

public final class IndexedSelectionBuilder {
   private final List<Object> objects = Lists.newArrayList();
   private final List<Integer> indeces = Lists.newArrayList();

   public IndexedSelectionBuilder add(Object var1, int var2) {
      this.objects.add(Preconditions.checkNotNull(var1, "object"));
      this.indeces.add(ArgCheck.checkNotNegative(var2, "index"));
      return this;
   }

   public IndexedSelection build() {
      return SelectionFactory.newIndexedSelection(this.objects.toArray(), Ints.toArray(this.indeces));
   }
}
