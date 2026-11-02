package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.interchange.entity.EntityRenamer;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.RenameNodeStrategy;

public final class RenameEntityNodeStrategy implements RenameNodeStrategy {
   private final EntityConfigLookup configs;

   public RenameEntityNodeStrategy(EntityConfigLookup var1) {
      Preconditions.checkNotNull(var1, "configs");
      this.configs = var1;
   }

   @Override
   public boolean isRenamable(KongaTreeNode var1) {
      return var1 instanceof IntegrationEntityTreeNode && ((IntegrationEntityTreeNode)var1).isRenamable();
   }

   @Override
   public boolean rename(KongaTree var1, KongaTreeNode var2, String var3) {
      Object var4 = var2.getUserObject();
      return var4 instanceof IntegrationEntity ? this.renameImpl((IntegrationEntity)var4, var3) : false;
   }

   private boolean renameImpl(IntegrationEntity var1, String var2) {
      EntityConfig var3 = this.configs.getEntityConfig(var1.getEntityType());
      EntityRenamer var4 = var3.getRenamer();
      return var4.setNewName(var1, var2);
   }
}
