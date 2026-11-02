package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.integration.client.ui.entity.EntityPageDisplayer;
import org.etlforge.integration.client.ui.entity.move.EntityMover;
import org.etlforge.integration.client.ui.entity.tree.DynamicProjectTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.client.ui.interchange.content.ProjectTreeConfiguration;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class MultiTreeSectionFactory {
   private final Iterable<ContentViewerCategory> categories;
   private final ProjectTreeConfiguration treeConfig;
   private final EntityConfigLookup entityConfigs;
   private final EntityPageDisplayer pageDisplayer;
   private final EntityMover entityMover;
   private final ProjectRenamer projectRenamer;
   private final IntegrationEntityTreeRenderer treeRenderer = new IntegrationEntityTreeRenderer();
   private PopupMenuManager popupMenuManager;

   public MultiTreeSectionFactory(
      Iterable<ContentViewerCategory> var1,
      ProjectTreeConfiguration var2,
      EntityConfigLookup var3,
      EntityPageDisplayer var4,
      EntityMover var5,
      ProjectRenamer var6
   ) {
      this.categories = var1;
      this.treeConfig = var2;
      this.entityConfigs = var3;
      this.pageDisplayer = var4;
      this.entityMover = var5;
      this.projectRenamer = var6;
   }

   public void setPopupMenuManager(PopupMenuManager var1) {
      this.popupMenuManager = var1;
   }

   public ProjectSection createProjectSection(MultiTreeContentViewer var1, Iterable<MultiTreeSection> var2) {
      return new ProjectSection(var1, var2, this.popupMenuManager, this.projectRenamer, this.pageDisplayer);
   }

   public ImmutableList<MultiTreeSection> createCategorySections(MultiTreeContentViewer var1) {
      Builder var2 = ImmutableList.builder();

      for (ContentViewerCategory var4 : this.categories) {
         MultiTreeSection var5 = this.createSection(var1, var4);
         var2.add(var5);
      }

      return var2.build();
   }

   private MultiTreeSection createSection(MultiTreeContentViewer var1, ContentViewerCategory var2) {
      MultiTreeSectionFactory.TreeImpl var3 = new MultiTreeSectionFactory.TreeImpl(var2);
      this.treeConfig.configure(var3.getAdaptee(), var2);
      this.customizeTreeAppearance(var3.getAdaptee());
      EntityRootDropHandler var4 = this.createRootDropHandler(var1, var2);
      return new MultiTreeSection(var1, var2, this.entityConfigs, var3, this.popupMenuManager, var4);
   }

   private void customizeTreeAppearance(IntegrationEntityTree var1) {
      var1.setCellRenderer(this.treeRenderer);
      var1.setLineStyle(KongaTree.LineStyle.NONE);
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);
      var1.setOpaque(false);
      var1.setBorder(Empty.border(0, 10, 0, 0));
   }

   private EntityRootDropHandler createRootDropHandler(MultiTreeContentViewer var1, ContentViewerCategory var2) {
      EntityRootDropHandler var3 = new EntityRootDropHandler(this.entityMover);

      for (TreeDropStrategy var5 : var2.getTreeDropStrategies(this.entityConfigs)) {
         var3.addDropStrategy(var5);
      }

      return var3;
   }

   private static final class TreeImpl extends DynamicProjectTree {
      private final ContentViewerCategory category;

      public TreeImpl(ContentViewerCategory var1) {
         this.category = var1;
      }

      @Override
      protected IntegrationEntityTreeModel createEmptyModel() {
         if (this.category == null) {
            return super.createEmptyModel();
         }

         DefaultKongaTreeNode var1 = new DefaultKongaTreeNode(this.category.getName(), false);
         return new IntegrationEntityTreeModel(var1);
      }

      @Override
      protected IntegrationEntityTreeModel createModelForProject(IntegrationProject var1) {
         KongaTreeNode var2 = this.category.buildTree(var1);
         return new IntegrationEntityTreeModel(var2);
      }
   }
}
