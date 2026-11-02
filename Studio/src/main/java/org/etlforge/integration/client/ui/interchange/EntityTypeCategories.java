package org.etlforge.integration.client.ui.interchange;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Iterator;

public final class EntityTypeCategories implements Iterable<TopLevelEntityCategory> {
   private final ImmutableList<TopLevelEntityCategory> categories;

   public EntityTypeCategories(Iterable<TopLevelEntityCategory> var1, TopLevelEntityCategory var2, Iterable<TopLevelEntityCategory> var3) {
      this.categories = new Builder<TopLevelEntityCategory>().addAll(var1).add(var2).addAll(var3).build();
   }

   @Override
   public Iterator<TopLevelEntityCategory> iterator() {
      return this.categories.iterator();
   }
}
