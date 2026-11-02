package org.etlforge.integration.client.ui.interchange.component.tree;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.client.ui.entity.tree.EntityTreeFilter;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.ProcessFilterListener;
import org.etlforge.integration.data.functors.EntityCollectionFilter;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.ui.widget.tree.KongaTreeFilter;

final class ContentViewerProcessFilter implements MultiTreeFilter {
   private final ProcessFilter impl;
   private final EntityCollectionFilter additionalItems;

   public ContentViewerProcessFilter(ProcessFilter var1) {
      this(var1, new EntityCollectionFilter());
   }

   private ContentViewerProcessFilter(ProcessFilter var1, EntityCollectionFilter var2) {
      this.impl = var1;
      this.additionalItems = var2;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.impl.apply(var1) || this.additionalItems.apply(var1);
   }

   @Override
   public boolean hasMatchingType(ContentViewerCategory var1) {
      List var2 = var1.getTypes();
      return this.impl.hasMatchingType(var2) || this.additionalItems.hasMatchingType(var2);
   }

   @Override
   public boolean containsWizards() {
      return this.impl.containsWizards() || this.additionalItems.containsWizards();
   }

   @Override
   public Set<EntityType> getAllTypes() {
      Set var1 = this.impl.getAllTypes();
      var1.addAll(this.additionalItems.getAllTypes());
      return var1;
   }

   public void addProcessFilterListener(ProcessFilterListener var1) {
      this.impl.addFilterListener(var1);
   }

   public void addAdditionalItems(Collection<? extends IntegrationEntity> var1) {
      this.additionalItems.addAll(var1);
   }

   @Override
   public KongaTreeFilter getTopLevelFilter(ContentViewerCategory var1) {
      return this.hasMatchingType(var1) ? this.getTreeFilter() : KongaTreeFilter.NONE;
   }

   private KongaTreeFilter getTreeFilter() {
      EntityFilter var1 = EntityFilters.and(this.impl, this.additionalItems);
      return EntityTreeFilter.memoize(var1);
   }

   @Override
   public boolean isEmpty() {
      return false;
   }
}
