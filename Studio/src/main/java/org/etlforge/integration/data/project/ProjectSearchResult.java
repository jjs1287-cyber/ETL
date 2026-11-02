package org.etlforge.integration.data.project;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ProjectSearchResult<T extends IntegrationEntity> implements Iterable<T> {
   private final Iterable<T> items;

   public static <E extends IntegrationEntity> ProjectSearchResult<E> of(Iterable<E> var0) {
      return new ProjectSearchResult<>(var0);
   }

   private ProjectSearchResult(Iterable<T> var1) {
      this.items = var1;
   }

   @Override
   public Iterator<T> iterator() {
      return this.items.iterator();
   }

   public int count() {
      return Iterables.size(this.items);
   }

   public boolean isEmpty() {
      return Iterables.isEmpty(this.items);
   }

   public List<T> asList() {
      return Lists.newArrayList(this.items);
   }

   public Set<T> asSet() {
      return Sets.newHashSet(this.items);
   }
}
