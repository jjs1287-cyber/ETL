package org.etlforge.integration.client.ui.script.builder;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.application.ui.widget.tree.ExpandCollapseToggleAction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.TreeSelectionSource;
import org.etlforge.ui.widget.tree.UserObjectNodeObjectConverter;

public final class ProjectItemTree implements UiProvider, Disposable {
   private final ScriptBuilderModel model;
   private final ProjectItemTreeModelBuilder treeModelBuilder;
   private final TreeSelectionSource selSource;
   private final ProjectPropertyChangeSupport projectChangeSupport;
   private final ProjectListener projectListener;
   private final KongaTree tree;

   public ProjectItemTree(ScriptBuilderModel var1, ProjectItemTreeModelBuilder var2) {
      this.model = var1;
      this.treeModelBuilder = var2;
      this.tree = this.createTree();
      this.selSource = new TreeSelectionSource(this.tree);
      this.selSource.setNodeObjectConverter(new UserObjectNodeObjectConverter(IntegrationEntity.class));
      this.projectListener = new ProjectItemTree.ProjectListenerImpl();
      var1.getProject().addProjectListener(this.projectListener);
      this.projectChangeSupport = new ProjectPropertyChangeSupport(var1.getProject());
      this.projectChangeSupport.addPropertyChangeListener(new ProjectItemTree.ChangeListener());
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.createModel());
      ScriptBuilderUtils.setTreeStyle(var1);
      var1.setCellRenderer(new ProjectItemTree.Renderer());
      var1.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      return var1;
   }

   private KongaTreeModel createModel() {
      IntegrationProject var1 = this.model.getProject();
      return this.treeModelBuilder.buildModel(var1);
   }

   @Override
   public void dispose() {
      this.projectChangeSupport.dispose();
      this.model.getProject().removeProjectListener(this.projectListener);
   }

   public IntegrationProject getProject() {
      return this.model.getProject();
   }

   public KongaTree getTree() {
      return this.tree;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.tree));
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocus();
   }

   public SelectionAction getEntityAction(Receiver<IntegrationEntity> var1, Icon var2, String var3, boolean var4) {
      ProjectItemTree.EntityAction var5 = new ProjectItemTree.EntityAction(var1, var2, var3);
      if (var4) {
         this.selSource.setDefaultAction(var5);
      }

      return var5;
   }

   private void refresh() {
      final KongaTreeModel var1 = this.createModel();
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            ProjectItemTree.this.tree.setNewModelWithExpansionIntact(var1, new KongaTree.DefaultNodeKeyExtractor(ProjectItemTree.this.tree));
         }
      });
   }

   public ExpandCollapseToggleAction getExpansionToggleAction() {
      return new ExpandCollapseToggleAction(this.tree);
   }

   private class ChangeListener implements PropertyChangeListener {
      private ChangeListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (this.doICare(var1)) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ProjectItemTree.this.refresh();
               }
            });
         }
      }

      private boolean doICare(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         return var2 == null || "Name".equals(var2) || "location".equals(var2) || "Location".equals(var2);
      }
   }

   private class EntityAction extends AbstractSingleObjectSelectionAction<IntegrationEntity> {
      private final Receiver<IntegrationEntity> handler;

      public EntityAction(Receiver<IntegrationEntity> var2, Icon var3, String var4) {
         super(IntegrationEntity.class, null, var3);
         this.putValue("ShortDescription", var4);
         this.handler = var2;
         this.setSelectionSource(ProjectItemTree.this.selSource);
      }

      protected void run(IntegrationEntity var1, ActionEvent var2) {
         this.handler.handle(var1);
      }
   }

   private class ProjectListenerImpl implements ProjectListener {
      private ProjectListenerImpl() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         ProjectItemTree.this.refresh();
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         ProjectItemTree.this.refresh();
      }

      @Override
      public void entitiesMoved(IntegrationProjectEvent var1) {
         ProjectItemTree.this.refresh();
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         ProjectItemTree.this.refresh();
      }
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         if (var2 instanceof IntegrationEntityTreeNode) {
            IntegrationEntityTreeNode var8 = (IntegrationEntityTreeNode)var2;
            if (var8.getUserObject().getEntityType() != EntityType.Folder) {
               this.setIcon(var8.getDefaultIcon());
            }

            this.setToolTipText(ToolTipStringFactory.getToolTip(var8.getUserObject()));
         } else {
            this.setToolTipText(null);
         }

         return this;
      }
   }
}
