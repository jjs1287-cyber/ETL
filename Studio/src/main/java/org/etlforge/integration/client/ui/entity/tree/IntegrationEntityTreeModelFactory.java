package org.etlforge.integration.client.ui.entity.tree;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class IntegrationEntityTreeModelFactory {
   private final Optional<ImmutableList<TopLevelNode>> topLevelNodes;

   public IntegrationEntityTreeModelFactory() {
      this.topLevelNodes = Optional.absent();
   }

   public IntegrationEntityTreeModelFactory(Iterable<TopLevelNode> var1) {
      this.topLevelNodes = Optional.of(ImmutableList.copyOf(var1));
   }

   public IntegrationEntityTreeModel newModel(IntegrationProject var1) {
      ProjectNode var2 = new ProjectNode(var1);

      for (TopLevelNode var4 : this.getTopLevelNodes(var1)) {
         KongaTreeNode var5 = var4.buildTree(var1);
         var2.addChild(var5);
      }

      return new IntegrationEntityTreeModel(var2);
   }

   private Iterable<TopLevelNode> getTopLevelNodes(IntegrationProject var1) {
      return this.topLevelNodes.isPresent()
         ? this.topLevelNodes.get()
         : Iterables.transform(var1.getSupportedEntityTypes(), new Function<EntityType, TopLevelNode>() {
            public TopLevelNode apply(@Nullable EntityType var1) {
               return new IntegrationEntityTreeModelFactory.TypeNode(var1);
            }
         });
   }

   private static class TypeNode implements TopLevelNode {
      private final EntityType type;

      public TypeNode(EntityType var1) {
         this.type = var1;
      }

      @Override
      public KongaTreeNode buildTree(IntegrationProject var1) {
         IntegrationEntityTreeModel var2 = ProjectTreeFragmentCreator.createTreeModel(var1, this.type);
         return var2.getRoot();
      }
   }
}
