package org.etlforge.integration.client.ui.interchange.content;

import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.client.ui.entity.tree.DynamicProjectTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.interchange.ProcessFilterInstaller;
import org.etlforge.integration.client.ui.interchange.component.tree.MultiTreeContentViewer;
import org.etlforge.integration.client.ui.interchange.component.tree.MultiTreeSectionFactory;
import org.etlforge.integration.client.ui.interchange.component.tree.SingleTreeContentViewer;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class ProjectContentViewerFactory {
   private final Iterable<ContentViewerCategory> categories;
   private final ProjectTreeConfiguration treeConfig;
   private final MultiTreeSectionFactory sectionFactory;
   private final ProcessFilterInstaller processFilterInstaller;

   public ProjectContentViewerFactory(
      Iterable<ContentViewerCategory> var1, ProjectTreeConfiguration var2, MultiTreeSectionFactory var3, ProcessFilterInstaller var4
   ) {
      this.categories = var1;
      this.treeConfig = var2;
      this.sectionFactory = var3;
      this.processFilterInstaller = var4;
   }

   public void setPopupMenuManager(PopupMenuManager var1) {
      this.sectionFactory.setPopupMenuManager(var1);
      this.treeConfig.setPopupMenuManager(var1);
   }

   public SingleTreeContentViewer newEmptyTree(WindowSection var1) {
      ProjectContentViewerFactory.ProjectTreeImpl var2 = new ProjectContentViewerFactory.ProjectTreeImpl(this.categories);
      this.treeConfig.configure(var2.getAdaptee(), this.categories);
      var2.getAdaptee().makeNodeExpanded(var2.getAdaptee().getModel().getRoot());
      return new SingleTreeContentViewer(var1, var2, this.processFilterInstaller);
   }

   public MultiTreeContentViewer newEmptyMultiTree(WindowSection var1) {
      return new MultiTreeContentViewer(var1, this.sectionFactory, this.processFilterInstaller);
   }

   private static class ProjectTreeImpl extends DynamicProjectTree {
      private final Iterable<ContentViewerCategory> categories;

      public ProjectTreeImpl(Iterable<ContentViewerCategory> var1) {
         this.categories = var1;
      }

      @Override
      protected IntegrationEntityTreeModel createModelForProject(IntegrationProject var1) {
         ProjectNode var2 = new ProjectNode(var1);

         for (ContentViewerCategory var4 : this.categories) {
            KongaTreeNode var5 = var4.buildTree(var1);
            var2.addChild(var5);
         }

         return new IntegrationEntityTreeModel(var2);
      }
   }
}
