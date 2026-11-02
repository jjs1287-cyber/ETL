package org.etlforge.integration.client.ui.project.recent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.TreeNode;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.decorator.TreeNodeDecorRenderer;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public final class RecentProjectsTreeViewer implements RecentProjectsViewer {
   private final RecentProjects recentProjects;
   private DefaultKongaTreeModel model;
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;

   public RecentProjectsTreeViewer(RecentProjects var1) {
      this.recentProjects = var1;
      this.tree = this.createTree();
      this.selectionSource = new TreeSelectionSource(this.tree);
      this.selectionSource.setNodeObjectConverter(new NodeObjectConverter.Adapter() {
         @Override
         public Object toObject(TreeNode var1) {
            return var1 instanceof ProjectNode ? ((ProjectNode)var1).getUserObject() : null;
         }
      });
      this.update();
   }

   @Override
   public RecentProjects getRecentProjects() {
      return this.recentProjects;
   }

   @Override
   public ProjectLocation getSelectedProject() {
      OrderedSelection var1 = this.getSelection();
      return var1.size() == 1 ? (ProjectLocation)var1.head() : null;
   }

   @Override
   public void update() {
      new RecentProjectsTreeViewer.Builder().update();
   }

   @Override
   public void decorate(UiCanvas var1) {
      TreePanel var2 = new TreePanel("Recent projects", this.tree);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public OrderedSelection getSelection() {
      return this.selectionSource.getSelection();
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.selectionSource.setDefaultAction(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSource.removeSelectionListener(var1);
   }

   private KongaTree createTree() {
      DefaultKongaTreeModel var1 = new DefaultKongaTreeModel(new DefaultKongaTreeNode("Projects", true));
      KongaTree var2 = new KongaTree(var1);
      var2.setCellRenderer(new TreeNodeDecorRenderer());
      var2.setRootVisible(false);
      var2.setShowsRootHandles(true);
      var2.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      return var2;
   }

   private class Builder {
      private List<ProjectNode> projectNodes;

      private Builder() {
      }

      public void update() {
         this.removeAll();
         this.addLocations();
         this.publish();
      }

      private void removeAll() {
         this.projectNodes = new ArrayList<>();
      }

      private void addLocations() {
         for (ProjectLocation var2 : RecentProjectsTreeViewer.this.recentProjects.getExistingLocations()) {
            this.projectNodes.add(new ProjectNode(var2));
         }
      }

      private void publish() {
         this.createModel();
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               RecentProjectsTreeViewer.this.tree.setModel(RecentProjectsTreeViewer.this.model);
            }
         });
      }

      private void createModel() {
         DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("Recent Projects", true);
         RecentProjectsTreeViewer.this.model = new DefaultKongaTreeModel(var1);
         Collections.sort(this.projectNodes, ProjectNodeComparator.LAST_ACCESSED_ORDER);

         for (ProjectNode var3 : this.projectNodes) {
            RecentProjectsTreeViewer.this.model.insertNode(var3, var1);
         }
      }
   }
}
