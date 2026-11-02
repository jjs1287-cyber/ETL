package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.application.ui.widget.tree.ActiveObjectProvidingTree;
import org.etlforge.application.ui.widget.tree.TreeFocusRootResource;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.application.ui.window.AbstractWindowElement;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.entity.search.ProjectItemOrder;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.DecorateDeployDirtyAction;
import org.etlforge.integration.client.ui.entity.search.ProjectItemTreeOrder;
import org.etlforge.integration.client.ui.entity.tree.DynamicProjectTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.NodeToEntity;
import org.etlforge.integration.client.ui.entity.tree.TreeValidationService;
import org.etlforge.integration.client.ui.entity.validation.DecorateInvalidItemsAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.ProcessFilterInstaller;
import org.etlforge.integration.client.ui.interchange.content.ExpansionMemento;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.SelectionMemento;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.ProcessFilterListener;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class SingleTreeContentViewer extends AbstractWindowElement implements ProjectContentViewer, ActiveObjectProvider, WindowSectionContent {
   private IntegrationProject project;
   private final DynamicProjectTree dynamicTree;
   private final ActiveObjectProvidingTree activeObjects;
   private final TreeValidationService validationService;
   private TreePreferenceListeners treePreferenceListeners;
   private OperationNodeContentService opNodeContentService;
   private ConnectOperationNodeService connectOpNodeService;
   private final WindowSection winSection;
   private final TreePanel treePanel;
   private boolean active;
   private final ShowTreeFilterAction showFilterAction;
   private SingleTreeFilterFunction filterFunction;
   private final ProcessFilterInstaller processFilterInstaller;
   private SingleTreeContentViewer.ProcessFilterService processFilterService;
   private ProcessFilterHeader processFilterHeader;

   public SingleTreeContentViewer(WindowSection var1, DynamicProjectTree var2, ProcessFilterInstaller var3) {
      super("project.single-tree");
      this.winSection = var1;
      this.dynamicTree = var2;
      this.processFilterInstaller = var3;
      this.treePanel = new SingleTreeContentViewer.TreePanelImpl(var2.getAdaptee());
      this.treePanel.setRootVisible(true);
      this.showFilterAction = new ShowTreeFilterAction(this);
      this.activeObjects = new ActiveObjectProvidingTree(this.getId(), var2.getAdaptee());
      this.activeObjects.setActiveObjectConverter(new NodeToEntity());
      this.validationService = new TreeValidationService();
      var2.getAdaptee().setValidationService(this.validationService);
      this.setDropDownActions();
      this.addResource(new TreeFocusRootResource(this.treePanel, this.activeObjects));
   }

   @Override
   public WindowSection getWindowSection() {
      return this.winSection;
   }

   @Override
   public String getTitle() {
      return "Design";
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.INTEGRATION_24;
   }

   @Override
   public void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).center(this.treePanel);
   }

   @Override
   public boolean isDetachable() {
      return false;
   }

   @Override
   public WindowSectionContent getWindowContent() {
      return this;
   }

   @Override
   public void newProjectOpened(final IntegrationProject var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SingleTreeContentViewer.this.project = var1;
            SingleTreeContentViewer.this.dynamicTree.setProject(var1);
            ManagedProject var1x = ProjectUtils.getManagedProject(var1);
            SingleTreeContentViewer.this.opNodeContentService = new OperationNodeContentService(var1x, SingleTreeContentViewer.this.dynamicTree.getModel());
            SingleTreeContentViewer.this.connectOpNodeService = new ConnectOperationNodeService(var1, SingleTreeContentViewer.this.dynamicTree.getModel());
            SingleTreeContentViewer.this.connectOpNodeService.insertOperationNodes();
            SingleTreeContentViewer.this.treePreferenceListeners.setOpNodeContentService(SingleTreeContentViewer.this.opNodeContentService);
            SingleTreeContentViewer.this.filterFunction = new SingleTreeFilterFunction(var1);
            SingleTreeContentViewer.this.showFilterAction.setEnabled(true);
            SingleTreeContentViewer.this.validationService.setModel(var1x.getValidationModel());
         }
      });
   }

   @Override
   public void projectClosed() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SingleTreeContentViewer.this.project = null;
            SingleTreeContentViewer.this.dynamicTree.setProject(null);
            SingleTreeContentViewer.this.validationService.setModel(null);
            SingleTreeContentViewer.this.opNodeContentService.dispose();
            SingleTreeContentViewer.this.connectOpNodeService.dispose();
            SingleTreeContentViewer.this.treePanel.hideFilterField();
            if (SingleTreeContentViewer.this.processFilterService != null) {
               SingleTreeContentViewer.this.processFilterService.dispose();
               SingleTreeContentViewer.this.processFilterService = null;
            }

            SingleTreeContentViewer.this.showFilterAction.setEnabled(false);
            SingleTreeContentViewer.this.dynamicTree.setFilter(null);
            SingleTreeContentViewer.this.filterFunction = null;
         }
      });
   }

   @Override
   public void renameEntity(final IntegrationEntity var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SingleTreeContentViewer.this.getTree().renameIntegrationEntity(var1);
         }
      });
   }

   @Override
   public List<IntegrationEntity> getSelectedEntities() {
      return this.getTree().getSelectedEntities();
   }

   @Override
   public void selectRoot() {
      IntegrationEntityTree var1 = this.getTree();
      var1.makeNodeSelected(var1.getModel().getRoot(), false);
      var1.requestFocusInWindow();
   }

   @Override
   public void selectEntity(final IntegrationEntity var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SingleTreeContentViewer.this.getTree().displayIntegrationEntity(var1);
         }
      });
   }

   @Override
   public List<IntegrationEntity> getExpandedEntities() {
      ArrayList var1 = new ArrayList();

      for (IntegrationEntityTreeNode var3 : this.getTree().getExpandedNodes()) {
         var1.add(var3.getUserObject());
      }

      return var1;
   }

   @Override
   public void setExpandedEntities(final List<IntegrationEntity> var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SingleTreeContentViewer.this.getTree().makeExpanded(Sets.newHashSet(var1));
         }
      });
   }

   @Override
   public void refresh(final Folder var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SingleTreeContentViewer.this.getTree().refresh(var1);
         }
      });
   }

   @Override
   public void giveFocusToFilterField() {
      if (this.treePanel.isShowingFilterField()) {
         this.treePanel.giveFocusToFilterField();
      } else if (this.project != null) {
         if (this.processFilterService != null) {
            this.filterFunction.setProcessFilter(this.processFilterService.processFilter);
         }

         this.treePanel.showFilterField(this.filterFunction);
      }
   }

   public IntegrationEntityTree getTree() {
      return this.dynamicTree.getAdaptee();
   }

   @Override
   public void install() {
      IntegrationEntityTree var1 = this.getTree();
      this.treePreferenceListeners = new TreePreferenceListeners(var1);
   }

   @Override
   public void uninstall() {
      this.dynamicTree.dispose();
      this.treePreferenceListeners.dispose();
      this.treePreferenceListeners = null;
   }

   @Override
   public void activated() {
      if (!this.active) {
         this.active = true;
         this.treePanel.activated();
      }
   }

   @Override
   public void deactivated() {
      if (this.active) {
         this.active = false;
         this.treePanel.deactivated();
      }
   }

   @Override
   public void collapseAll(RootFolder var1) {
      if (var1 == null) {
         this.getTree().collapseAll();
      } else {
         KongaTreeNode var2 = this.getTree().getNodeForEntity(var1);
         if (var2 != null) {
            this.getTree().collapsePath(this.getTree().getPath(var2));
         }
      }
   }

   @Override
   public void expand(RootFolder var1) {
      KongaTreeNode var2 = this.getTree().getNodeForEntity(var1);
      if (var2 != null) {
         this.getTree().makeNodeExpanded(var2);
      }
   }

   @Override
   public void expandAll(RootFolder var1) {
      if (var1 == null) {
         this.getTree().expandAll();
      } else {
         KongaTreeNode var2 = this.getTree().getNodeForEntity(var1);
         if (var2 != null) {
            this.getTree().expandAllUnder(var2);
         }
      }
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
   public List<?> getActiveObjects() {
      return this.activeObjects.getActiveObjects();
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjects.addActiveObjectListener(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjects.removeActiveObjectListener(var1);
   }

   @Override
   public <T extends IntegrationEntity> ProjectItemOrder<T> getItemOrder(EntityType var1, Class<T> var2) {
      return new ProjectItemTreeOrder<>(this.getTree(), var1, var2);
   }

   private void setDropDownActions() {
      DecorateInvalidItemsAction var1 = new DecorateInvalidItemsAction(this.getTree());
      var1.setPreference(InvalidItemsIndicatorPreference.INSTANCE);
      DecorateDeployDirtyAction var2 = new DecorateDeployDirtyAction(this.getTree());
      var2.setPreference(DeployDirtyIndicatorPreference.INSTANCE);
      this.treePanel.setDropdownActions(new Action[]{var1, var2, this.showFilterAction});
   }

   @Override
   public void setProcessFilter(ProcessFilter var1) {
      ExpansionMemento var2 = new ExpansionMemento(this);
      SelectionMemento var3 = new SelectionMemento(this);
      if (this.processFilterService != null) {
         this.processFilterService.dispose();
         this.processFilterService = null;
      }

      if (this.filterFunction != null) {
         if (var1 != null) {
            this.processFilterService = new SingleTreeContentViewer.ProcessFilterService(this.project, var1);
            this.filterFunction.setProcessFilter(this.processFilterService.processFilter);
            this.treePanel.setRemoveFilterWhenFilterFieldClosed(false);
         } else {
            this.filterFunction.setProcessFilter(null);
            this.treePanel.setRemoveFilterWhenFilterFieldClosed(true);
         }

         KongaTreeFilter var4 = this.filterFunction.getCurrentFilter();
         this.dynamicTree.setFilter(var4);
         var2.restore();
         if (var1 == null) {
            var3.restore();
            this.processFilterHeader.dispose();
            this.processFilterHeader = null;
            this.treePanel.setAdditionalTitleComponent(null);
         } else {
            this.processFilterHeader = new ProcessFilterHeader(var1, new ProcessFilterRemover(this.processFilterInstaller));
            this.treePanel.setAdditionalTitleComponent(this.processFilterHeader);
         }
      }
   }

   private class ProcessFilterService extends ProjectListener.Adapter implements Disposable, ProcessFilterListener {
      private final IntegrationProject project;
      private final ContentViewerProcessFilter processFilter;

      public ProcessFilterService(IntegrationProject var2, ProcessFilter var3) {
         this.project = var2;
         this.processFilter = new ContentViewerProcessFilter(var3);
         var2.addProjectListener(this);
         var3.addFilterListener(this);
      }

      @Override
      public void filterChanged(ProcessFilter var1) {
         this.updateFilter();
      }

      private void updateFilter() {
         KongaTreeFilter var1 = SingleTreeContentViewer.this.filterFunction.getCurrentFilter();
         SingleTreeContentViewer.this.dynamicTree.setFilter(var1);
      }

      @Override
      public void dispose() {
         this.project.removeProjectListener(this);
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.processFilter.addAdditionalItems(var1.getEntities());
         this.updateFilter();
      }
   }

   private class TreePanelImpl extends TreePanel {
      public TreePanelImpl(KongaTree var2) {
         super("Project Tree", var2);
      }

      @Override
      public void hideFilterField() {
         SelectionMemento var1 = new SelectionMemento(SingleTreeContentViewer.this);
         super.hideFilterField();
         KongaTreeFilter var2 = SingleTreeContentViewer.this.filterFunction.removePathFilter();
         SingleTreeContentViewer.this.dynamicTree.setFilter(var2);
         var1.restore();
      }
   }
}
