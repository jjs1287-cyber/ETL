package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class CompoundTreeDropStrategy implements TreeDropStrategy {
   private final List<TreeDropStrategy> parts;
   private TreeDropStrategy selected;

   public CompoundTreeDropStrategy(TreeDropStrategy... var1) {
      this.parts = Lists.newArrayList(var1);
   }

   public void add(TreeDropStrategy var1) {
      this.parts.add(var1);
   }

   @Override
   public boolean canDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      this.selected = this.getMatchingStrategy(var1, var2);
      return this.selected != null;
   }

   private TreeDropStrategy getMatchingStrategy(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      for (TreeDropStrategy var4 : this.parts) {
         if (var4.canDrop(var1, var2)) {
            return var4;
         }
      }

      return null;
   }

   @Override
   public boolean handleDrop(IntegrationEntity var1, List<? extends IntegrationEntity> var2) {
      return this.selected != null && this.selected.handleDrop(var1, var2);
   }
}
