package org.etlforge.integration.client.ui.interchange.component.tree;

import java.util.Set;
import org.etlforge.integration.client.ui.entity.tree.EntityTreeFilter;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.ui.widget.tree.KongaTreeFilter;

final class CompoundMultiTreeFilter implements MultiTreeFilter {
   private final ContentViewerProcessFilter processFilter;
   private MultiTreeNameFilter nameFilter;

   public CompoundMultiTreeFilter(ContentViewerProcessFilter var1, MultiTreeNameFilter var2) {
      this.processFilter = var1;
      this.nameFilter = var2;
   }

   public void setNameFilter(MultiTreeNameFilter var1) {
      this.nameFilter = var1;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.processFilter.apply(var1) && this.nameFilter.apply(var1);
   }

   @Override
   public boolean hasMatchingType(ContentViewerCategory var1) {
      return this.processFilter.hasMatchingType(var1) && this.nameFilter.hasMatchingType(var1);
   }

   @Override
   public boolean containsWizards() {
      return this.processFilter.containsWizards() && this.nameFilter.containsWizards();
   }

   @Override
   public boolean isEmpty() {
      return false;
   }

   @Override
   public Set<EntityType> getAllTypes() {
      Set var1 = this.processFilter.getAllTypes();
      var1.retainAll(this.nameFilter.getAllTypes());
      return var1;
   }

   @Override
   public KongaTreeFilter getTopLevelFilter(ContentViewerCategory var1) {
      return this.hasMatchingType(var1) ? this.getTreeFilter() : KongaTreeFilter.NONE;
   }

   private KongaTreeFilter getTreeFilter() {
      EntityFilter var1 = EntityFilters.and(this.processFilter, this.nameFilter);
      return EntityTreeFilter.memoize(var1);
   }
}
