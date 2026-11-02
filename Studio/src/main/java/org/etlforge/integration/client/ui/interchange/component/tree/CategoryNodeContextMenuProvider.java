package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import javax.annotation.Nullable;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.uidelegate.entity.tree.TopLevelCategoryTreeNode;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class CategoryNodeContextMenuProvider implements Function<KongaTreeNode, JPopupMenu> {
   private final EntityConfigLookup entityConfigs;

   public CategoryNodeContextMenuProvider(EntityConfigLookup var1) {
      this.entityConfigs = var1;
   }

   @Nullable
   public JPopupMenu apply(KongaTreeNode var1) {
      if (!(var1 instanceof TopLevelCategoryTreeNode)) {
         return null;
      }

      ArrayList var2 = Lists.newArrayList();

      for (KongaTreeNode var4 : var1.children()) {
         Object var5 = var4.getUserObject();
         if (!(var5 instanceof RootFolder)) {
            return null;
         }

         EntityType var6 = ((RootFolder)var5).getItemType();
         EntityConfig var7 = this.entityConfigs.getEntityConfig(var6);
         ApplicationAction var8 = var7.newEntityAction();
         var2.add(var8);
      }

      return KongaPopupMenu.fromItems(var2);
   }
}
