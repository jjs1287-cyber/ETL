package org.etlforge.integration.client.ui.deploy.history;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.Icon;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.deploy.DeployedEntityDescriptor;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.util.name.NameSorter;

public final class DeployedEntityDescriptorTreeFactory {
   private static final Comparator<DeployedEntityDescriptor> NODE_SORTER = NameSorter.ignoreCase();

   public static KongaTree newTree(DeployedEntityDescriptor var0) {
      KongaTree var1 = new KongaTree(newModel(var0));
      var1.setCellRenderer(new TreeNodeDecorRenderer());
      return var1;
   }

   private static KongaTreeModel newModel(DeployedEntityDescriptor var0) {
      Preconditions.checkArgument(var0.getType() == EntityType.Project, "project must represent an IntegrationProject");
      DeployedEntityDescriptorTreeFactory.Node var1 = DeployedEntityDescriptorTreeFactory.Node.create(null, var0);

      for (DeployedEntityDescriptor var3 : var0.getChildren()) {
         buildTreeBranch(var1, var3);
      }

      return new DefaultKongaTreeModel(var1);
   }

   private static void buildTreeBranch(KongaTreeNode var0, DeployedEntityDescriptor var1) {
      DeployedEntityDescriptorTreeFactory.Node var2 = DeployedEntityDescriptorTreeFactory.Node.create(var0, var1);
      ArrayList var3 = Lists.newArrayList();
      ArrayList var4 = Lists.newArrayList();

      for (DeployedEntityDescriptor var6 : var1.getChildren()) {
         if (var6.getType() == EntityType.Folder) {
            var3.add(var6);
         } else {
            var4.add(var6);
         }
      }

      Collections.sort(var3, NODE_SORTER);
      Collections.sort(var4, NODE_SORTER);

      for (Object __etlItem61 : var3) {
         DeployedEntityDescriptor var9 = (DeployedEntityDescriptor) __etlItem61;
         buildTreeBranch(var2, var9);
      }

      for (Object __etlItem65 : var4) {
         DeployedEntityDescriptor var10 = (DeployedEntityDescriptor) __etlItem65;
         DeployedEntityDescriptorTreeFactory.Node.create(var2, var10);
      }
   }

   private DeployedEntityDescriptorTreeFactory() {
   }

   private static final class Node extends DefaultKongaTreeNode implements TreeNodeDecorator {
      private final String text;
      private final TextStyle style;

      private Node(KongaTreeNode var1, DeployedEntityDescriptor var2, boolean var3, TextStyle var4) {
         super(var1, var2, var3);
         this.style = var4;
         this.text = var2.getType() == EntityType.Project ? "Project: " + var2.getName() : var2.getName();
      }

      public static DeployedEntityDescriptorTreeFactory.Node create(KongaTreeNode var0, DeployedEntityDescriptor var1) {
         boolean var2 = false;
         TextStyle var3 = TextStyles.TreeLeafText;
         EntityType var4 = var1.getType();
         if (var4 == EntityType.Project) {
            var2 = true;
            var3 = TextStyles.TreeRootText;
         } else if (var4 == EntityType.RootFolder) {
            var2 = true;
            var3 = TextStyles.TreeRootFolderText;
         } else if (var4 == EntityType.Folder) {
            var2 = true;
         }

         return new DeployedEntityDescriptorTreeFactory.Node(var0, var1, var2, var3);
      }

      public DeployedEntityDescriptor getUserObject() {
         return (DeployedEntityDescriptor)super.getUserObject();
      }

      @Override
      public String getDisplayName() {
         return this.text;
      }

      @Override
      public Icon getNodeIcon(NodeStatus var1) {
         EntityType var2 = this.getUserObject().getType();
         if (var2 == EntityType.RootFolder) {
            return null;
         }

         EntityIcons var3 = EntityIcons.forType(var2);
         return var3.getDefaultIcon(EntityIcons.Size.SMALL);
      }

      @Override
      public String toString() {
         return this.getDisplayName();
      }

      @Override
      public void decorate(TreeNodeDecor var1) {
         var1.setText(this.text);
         var1.setToolTip(this.text);
         var1.setIcon(this.getNodeIcon(NodeStatus.CLOSED));
         var1.setTextStyle(this.style.asTextDecorator());
      }
   }
}
