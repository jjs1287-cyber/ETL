package org.etlforge.integration.client.ui.validation.view;

import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.integration.uidelegate.entity.tree.TopLevelCategoryTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class InvalidItemsTreeFilter implements KongaTreeFilter {
   private final EntityFilter invalid;

   public InvalidItemsTreeFilter(ValidationModel var1) {
      this.invalid = new NotValidFilter(var1);
   }

   @Override
   public boolean apply(KongaTreeNode var1) {
      if (var1 instanceof IntegrationEntityTreeNode) {
         return this.checkEntity((IntegrationEntityTreeNode)var1);
      } else {
         return var1 instanceof TopLevelCategoryTreeNode ? this.checkTopLevelCategoryNode(var1) : false;
      }
   }

   private boolean checkEntity(IntegrationEntityTreeNode var1) {
      IntegrationEntity var2 = var1.getUserObject();
      if (var2 instanceof IntegrationProject) {
         return true;
      } else {
         return !(var2 instanceof Folder) ? this.invalid.apply(var2) : this.invalid.apply(var2) || var1.hasMatchingChild(this.invalid);
      }
   }

   private boolean checkTopLevelCategoryNode(KongaTreeNode var1) {
      for (KongaTreeNode var3 : var1.children()) {
         if (this.apply(var3)) {
            return true;
         }
      }

      return false;
   }
}
