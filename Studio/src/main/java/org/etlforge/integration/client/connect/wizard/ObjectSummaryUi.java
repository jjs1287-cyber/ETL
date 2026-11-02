package org.etlforge.integration.client.connect.wizard;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class ObjectSummaryUi implements UiProvider {
   private final ObjectSummaryUi.NameProcessor nameProcessor;
   private final ObjectSummaryUi.Tree tree;

   public ObjectSummaryUi(Folder var1, Folder var2) {
      this.nameProcessor = new ObjectSummaryUi.NameProcessor(var1, var2);
      this.tree = new ObjectSummaryUi.Tree();
   }

   public void setNewWizardName(String var1) {
      String var2 = this.nameProcessor.getOperationName(var1);
      String var3 = this.nameProcessor.getTransformationName(var1);
      this.tree.setNewNames(var2, var3);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.tree.requestFocusInWindow();
   }

   private static class NameProcessor {
      private final Folder operationParent;
      private final Folder transformationParent;

      public NameProcessor(Folder var1, Folder var2) {
         this.operationParent = var1;
         this.transformationParent = var2;
      }

      public String getOperationName(String var1) {
         return this.getAvailabelName(var1, this.operationParent);
      }

      private String getAvailabelName(String var1, Folder var2) {
         return var2 == null ? var1 : EntityUtils.getAvailableChildName(var2, var1);
      }

      public String getTransformationName(String var1) {
         return this.getAvailabelName(var1, this.transformationParent);
      }
   }

   private static class Tree {
      private final ObjectSummaryUi.Tree.ItemNode operationNode = new ObjectSummaryUi.Tree.ItemNode(EntityType.Operation);
      private final KongaTreeNode transformationNode = new ObjectSummaryUi.Tree.ItemNode(EntityType.Transformation);
      public final KongaTree tree = this.createTree();

      public Tree() {
      }

      private KongaTree createTree() {
         KongaTreeModel var1 = this.createModel();
         KongaTree var2 = new KongaTree(var1);
         var2.setRootVisible(false);
         var2.setShowsRootHandles(true);
         var2.setVisibleRowCount(5);
         var2.setCellRenderer(new ObjectSummaryUi.Tree.Renderer());
         var2.expandAll();
         return var2;
      }

      private KongaTreeModel createModel() {
         DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("root", true);
         DefaultKongaTreeNode var2 = new DefaultKongaTreeNode(var1, EntityType.Operation.getPlural(), true);
         var2.addChild(this.operationNode);
         DefaultKongaTreeNode var3 = new DefaultKongaTreeNode(var1, EntityType.Transformation.getPlural(), true);
         var3.addChild(this.transformationNode);
         return new DefaultKongaTreeModel(var1);
      }

      public void setNewNames(String var1, String var2) {
         this.operationNode.setUserObject(var1);
         this.transformationNode.setUserObject(var2);
         this.refreshTree();
      }

      private void refreshTree() {
         DefaultKongaTreeModel var1 = (DefaultKongaTreeModel)this.tree.getModel();
         var1.nodeChanged(this.operationNode);
         var1.nodeChanged(this.transformationNode);
         var1.fireTreeStructureChanged(this.tree, null);
         this.tree.repaint();
      }

      private static class ItemNode extends DefaultKongaTreeNode {
         public final EntityType type;

         public ItemNode(EntityType var1) {
            super("", false);
            this.type = var1;
         }
      }

      private static class Renderer extends DefaultTreeCellRenderer {
         private Renderer() {
         }

         @Override
         public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
            super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
            if (var2 instanceof ObjectSummaryUi.Tree.ItemNode) {
               this.setFont(TextStyles.TreeLeafText.getFont());
               EntityType var8 = ((ObjectSummaryUi.Tree.ItemNode)var2).type;
               Icon var9 = EntityIcons.forType(var8).getDefaultIcon(EntityIcons.Size.SMALL);
               this.setIcon(var9);
            } else {
               this.setFont(TextStyles.TreeRootFolderText.getFont());
               this.setIcon(null);
            }

            return this;
         }
      }
   }
}
