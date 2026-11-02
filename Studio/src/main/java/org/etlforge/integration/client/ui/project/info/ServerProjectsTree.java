package org.etlforge.integration.client.ui.project.info;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.tree.ActiveObjectProvidingTree;
import org.etlforge.application.ui.widget.tree.FindInTreeAction;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.application.ui.widget.tree.TreeWaitNodeService;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.ServerInfoNode;
import org.etlforge.integration.client.ui.entity.tree.NodeToEntity;
import org.etlforge.integration.client.ui.entity.tree.ProjectPathFilterFunction;
import org.etlforge.integration.client.ui.project.info.resources.ProjectInfoUiResources;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.ExpansionFilter;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.util.Property;

public final class ServerProjectsTree implements ServerProjectsViewer {
   private final ProjectConsoleModel model;
   private final TreeSupport treeSupport;
   private final KongaTree tree;
   private final TreePanel treePanel;
   private final ActiveObjectProvidingTree activeObjects;
   private TreeFilterControl filterControl;
   private final CachedInfoStatusPanel cachedInfoStatusPanel;
   private ServerInfoNode root;
   private final TreeWaitNodeService waitNodeService;
   private ServerProjectsTree.ProjectsListener projectsListener;
   private final Bindings modelBindings;
   private Predicate<? super KongaTreeNode> expansionCondition;

   public ServerProjectsTree(ProjectConsoleModel var1) {
      this.model = var1;
      this.treeSupport = new TreeSupport(var1);
      this.tree = this.createTree();
      this.treePanel = this.createTreePanel();
      this.cachedInfoStatusPanel = new CachedInfoStatusPanel();
      this.activeObjects = new ActiveObjectProvidingTree("admin.serverprojectstree", this.treePanel.getTree());
      this.activeObjects.setActiveObjectConverter(new NodeToEntity());
      this.root = (ServerInfoNode)this.tree.getModel().getRoot();
      this.waitNodeService = new TreeWaitNodeService(this.tree, ProjectInfoUiResources.getString("ServerProjectsTree.WaitNode"));
      this.projectsListener = new ServerProjectsTree.ProjectsListener(var1);
      this.filterControl = TreeFilterControl.NO_FILTER;
      this.modelBindings = this.createModelBindings();
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.treeSupport.buildModel());
      var1.setToolTipsEnabled(false);
      var1.setCellRenderer(this.treeSupport.getNodeRenderer());
      var1.asSelectionSource().setNodeObjectConverter(this.treeSupport.getSelectionConverter());
      var1.setVisibleRowCount(6);
      return var1;
   }

   private TreePanel createTreePanel() {
      TreePanel var1 = new TreePanel("Deployed Projects", this.tree) {
         @Override
         protected JButton[] getToolBarButtons() {
            ArrayList var1x = Lists.newArrayList();
            var1x.add(new KongaButton(ServerProjectsTree.this.createSearchAction()).makeToolbarButton());
            var1x.add(null);
            Collections.addAll(var1x, super.getToolBarButtons());
            return (JButton[]) var1x.toArray(new JButton[var1x.size()]);
         }
      };
      var1.setRootVisible(true);
      var1.setLayoutFilterFieldBelowTheTree(true);
      return var1;
   }

   private Action createSearchAction() {
      FindInTreeAction var1 = new FindInTreeAction(this.tree, ProjectInfoUiResources.getString("ServerProjectsTree.Search.Title"));
      var1.setToolTip(ProjectInfoUiResources.getString("ServerProjectsTree.Search.ToolTip"));
      return var1;
   }

   private Bindings createModelBindings() {
      Bindings var1 = new Bindings();
      Property var2 = this.model.getProperty("downloadingInfo");
      var1.add(new ServerProjectsTree.DownloadingIndicatorBinding(var2));
      Property var3 = this.model.getProperty("projects");
      var1.add(new ServerProjectsTree.ProjectListBinding(var3));
      Property var4 = this.model.getProperty("currentServer");
      var1.add(new ServerProjectsTree.CurrentServerBinding(var4));
      Property var5 = this.model.getProperty("cachedInfo");
      var1.add(new ServerProjectsTree.CachedInfoBinding(var5));
      var1.syncUi();
      return var1;
   }

   @Override
   public void dispose() {
      this.projectsListener.dispose();
      this.modelBindings.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.getTreeLayout();
      var2.decorate(var1);
   }

   private UiProvider getTreeLayout() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10).center(this.treePanel).south(this.layoutSouthComponents());
      var1.withBorder(Empty.border(0, 0, 10, 0)).deeplyNonOpaque();
      return var1;
   }

   private JComponent layoutSouthComponents() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(10, 0);
      var1.west(this.filterControl).east(this.cachedInfoStatusPanel);
      return var1.container();
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjects.addActiveObjectListener(var1);
   }

   @Override
   public List<?> getActiveObjects() {
      return this.activeObjects.getActiveObjects();
   }

   @Override
   public String getProviderId() {
      return this.activeObjects.getProviderId();
   }

   @Override
   public boolean isActive() {
      return this.activeObjects.isActive();
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjects.removeActiveObjectListener(var1);
   }

   @Override
   public void setActive(boolean var1) {
      if (var1) {
         this.treePanel.activated();
      } else {
         this.treePanel.deactivated();
      }

      this.activeObjects.setActive(var1);
   }

   @Override
   public SelectionSource getSelectionSource() {
      return this.tree.asSelectionSource();
   }

   public void setFilterControl(TreeFilterControl var1) {
      this.filterControl = var1;
      var1.setTree(this.treePanel);
   }

   public void setShowRootNode(boolean var1) {
      this.treePanel.setRootVisible(var1);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.tree.asSelectionSource().setDefaultAction(var1);
   }

   public void setRefreshAction(Action var1) {
      this.cachedInfoStatusPanel.setRefreshAction(var1);
   }

   public void setToggleClickCount(int var1) {
      this.tree.setToggleClickCount(var1);
   }

   public void setExpansionFilter(Predicate<? super KongaTreeNode> var1) {
      this.expansionCondition = var1;
      this.applyExpansionFilter();
   }

   private void applyExpansionFilter() {
      if (this.expansionCondition != null) {
         ExpansionFilter var1 = new ExpansionFilter(this.expansionCondition);
         var1.applyTo(this.tree);
      }
   }

   @Override
   public void setContextMenuProvider(PopupMenuRequestHandler var1) {
      PopupMenuRequestHandlers.install(this.tree, var1);
   }

   public void setTitle(String var1) {
      this.treePanel.setTitle(var1);
   }

   private class CachedInfoBinding extends AbstractBinding<Boolean> {
      public CachedInfoBinding(Property<Boolean> var2) {
         super(var2);
      }

      protected void updateUiWithValue(final Boolean var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ServerProjectsTree.this.cachedInfoStatusPanel.setVisible(var1);
            }
         });
      }
   }

   private class CurrentServerBinding extends AbstractBinding<ServerInfo> {
      public CurrentServerBinding(Property<ServerInfo> var2) {
         super(var2);
      }

      protected void updateUiWithValue(ServerInfo var1) {
         ServerProjectsTree.this.root.setServerInfo(var1);
         ServerProjectsTree.this.tree.getModel().nodeChanged(ServerProjectsTree.this.root);
      }
   }

   private class DownloadingIndicatorBinding extends AbstractBinding<Boolean> {
      public DownloadingIndicatorBinding(Property<Boolean> var2) {
         super(var2);
      }

      protected void updateUiWithValue(Boolean var1) {
         if (var1) {
            ServerProjectsTree.this.treePanel.clearFilter();
         }

         ServerProjectsTree.this.waitNodeService.setWaiting(var1);
      }
   }

   private class ProjectListBinding extends AbstractBinding<List<IntegrationProject>> {
      public ProjectListBinding(Property<List<IntegrationProject>> var2) {
         super(var2);
      }

      protected void updateUiWithValue(final List<IntegrationProject> var1) {
         final DefaultKongaTreeModel var2 = ServerProjectsTree.this.treeSupport.buildModel();
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               ServerProjectsTree.this.projectsListener.dispose();
               ServerProjectsTree.this.projectsListener = ServerProjectsTree.this.new ProjectsListener(ServerProjectsTree.this.model);
               ServerProjectsTree.this.tree.setModel(var2);
               this.installFilter(var1);
               ServerProjectsTree.this.root = (ServerInfoNode)var2.getRoot();
               ServerProjectsTree.this.applyExpansionFilter();
               ServerProjectsTree.this.requestFocus();
               ServerProjectsTree.this.tree.makeNodeSelected(ServerProjectsTree.this.root, false);
               ServerProjectsTree.this.filterControl.update();
            }

            private void installFilter(List<IntegrationProject> var1x) {
               ProjectPathFilterFunction var2x = new ProjectPathFilterFunction(var1x);
               ServerProjectsTree.this.treePanel.showFilterField(var2x, false);
            }
         });
      }
   }

   private class ProjectsListener implements Disposable, PropertyChangeListener {
      private final List<ProjectPropertyChangeSupport> changeSupports = Lists.newArrayList();

      public ProjectsListener(ProjectConsoleModel var2) {
         for (IntegrationProject var4 : var2.getProjects()) {
            this.listenToProject(var4);
         }
      }

      private void listenToProject(IntegrationProject var1) {
         ProjectPropertyChangeSupport var2 = new ProjectPropertyChangeSupport(var1);
         var2.addPropertyChangeListener("enabled", this);
         var2.addPropertyChangeListener("scheduleEnabled", this);
         this.changeSupports.add(var2);
      }

      @Override
      public void dispose() {
         for (ProjectPropertyChangeSupport var2 : this.changeSupports) {
            var2.dispose();
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ServerProjectsTree.this.tree.repaint();
      }
   }
}
