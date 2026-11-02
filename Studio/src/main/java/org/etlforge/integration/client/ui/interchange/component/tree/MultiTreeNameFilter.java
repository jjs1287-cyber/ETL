package org.etlforge.integration.client.ui.interchange.component.tree;

import java.util.Collection;
import java.util.Set;
import org.etlforge.integration.client.ui.entity.tree.MatchingItemsTreeFilter;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityCollectionFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.MemoizingKongaTreeFilter;

public final class MultiTreeNameFilter implements MultiTreeFilter {
   private final EntityCollectionFilter impl;

   public MultiTreeNameFilter(Collection<? extends IntegrationEntity> var1) {
      this.impl = var1 == null ? null : new EntityCollectionFilter(var1);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.impl == null || this.impl.apply(var1);
   }

   @Override
   public boolean hasMatchingType(ContentViewerCategory var1) {
      return this.impl == null || this.impl.hasMatchingType(var1.getTypes());
   }

   @Override
   public boolean containsWizards() {
      return this.impl == null || this.impl.containsWizards();
   }

   @Override
   public Set<EntityType> getAllTypes() {
      return this.impl == null ? EntityType.allTypes() : this.impl.getAllTypes();
   }

   @Override
   public KongaTreeFilter getTopLevelFilter(ContentViewerCategory var1) {
      if (this.isEmpty()) {
         return null;
      } else {
         Collection var2 = this.impl.collectItemsOfTypes(var1.getTypes());
         if (var2.isEmpty()) {
            return KongaTreeFilter.NONE;
         } else {
            return var2.isEmpty() ? KongaTreeFilter.NONE : this.createFilter(var2);
         }
      }
   }

   private KongaTreeFilter createFilter(Collection<IntegrationEntity> var1) {
      MatchingItemsTreeFilter var2 = new MatchingItemsTreeFilter(var1);
      return new MemoizingKongaTreeFilter(var2);
   }

   @Override
   public boolean isEmpty() {
      return this.impl == null;
   }

   public boolean containsSameTypes(MultiTreeNameFilter var1) {
      if (this.impl == null) {
         return var1.impl == null;
      } else {
         return var1.impl == null ? false : this.impl.containsSameTypes(var1.impl);
      }
   }
}
