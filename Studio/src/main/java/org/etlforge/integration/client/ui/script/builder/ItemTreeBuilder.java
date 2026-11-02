package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.client.ui.entity.tree.ProjectTreeFragmentCreator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.KongaTreeNode;

class ItemTreeBuilder {
   private final EntityType type;
   private final ProjectTreeFragmentCreator factory;

   public ItemTreeBuilder(IntegrationProject var1, EntityType var2) {
      this.type = var2;
      this.factory = new ProjectTreeFragmentCreator(var1);
      this.factory.setRemoveEmptyFolders(true);
   }

   public void setFilter(EntityFilter var1) {
      this.factory.setFilter(var1);
   }

   public void buildTree(KongaTreeNode var1) {
      this.factory.createFragment(var1, this.type);
   }
}
