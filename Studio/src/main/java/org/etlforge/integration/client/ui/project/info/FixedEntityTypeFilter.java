package org.etlforge.integration.client.ui.project.info;

import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class FixedEntityTypeFilter extends AbstractTreeFilterControl {
   public FixedEntityTypeFilter(Iterable<EntityType> var1) {
      this.setNonTextFilter(new FixedEntityTypeFilter.TypeFilter(var1));
   }

   @Override
   public void decorate(UiCanvas var1) {
   }

   @Override
   public void requestFocus() {
   }

   private static class TypeFilter implements KongaTreeFilter {
      private final ImmutableSet<EntityType> typesToShow;

      public TypeFilter(Iterable<EntityType> var1) {
         this.typesToShow = ImmutableSet.copyOf(var1);
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         EntityType var2 = null;
         Object var3 = var1.getUserObject();
         if (var3 instanceof IntegrationEntity) {
            var2 = EntityUtils.getItemType((IntegrationEntity)var3);
         }

         return var2 == null || this.typesToShow.contains(var2);
      }
   }
}
