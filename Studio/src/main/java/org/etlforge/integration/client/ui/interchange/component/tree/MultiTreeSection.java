package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeModelEvent;
import org.etlforge.application.ui.action.popup.PopupMenuManager;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.window.event.ActiveObjectProvider;
import org.etlforge.application.ui.window.event.ActiveObjectProviderHelper;
import org.etlforge.application.ui.window.event.ActiveObjectProviderListener;
import org.etlforge.integration.client.ui.entity.DeployDirtyIndicator;
import org.etlforge.integration.client.ui.entity.tree.DynamicProjectTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.validation.ValidationStatusIndicator;
import org.etlforge.integration.client.ui.interchange.content.ContentViewerCategory;
import org.etlforge.integration.client.ui.interchange.core.action.popup.PopupMenuSites;
import org.etlforge.integration.client.ui.interchange.entity.EntityConfigLookup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectPropertyChangeSupport;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.DoubleClick;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.KeyDownReplacementAction;
import org.etlforge.ui.widget.tree.KeyLeftReplacementAction;
import org.etlforge.ui.widget.tree.KeyUpReplacementAction;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.TreeModelAdapter;
import org.etlforge.ui.widget.tree.event.KongaTreeAdapter;
import org.etlforge.ui.widget.tree.event.KongaTreeEvent;

final class MultiTreeSection implements UiProvider, ActiveObjectProvider, DeployDirtyIndicator, ValidationStatusIndicator {
   private static final Border NORMAL_SEPARATOR_BORDER = Empty.border(10, 0, 0, 0);
   private static final Border BORDER_WHEN_EMPTY = createBorderForEmptySection();
   private final MultiTreeContentViewer owner;
   private final ContentViewerCategory category;
   private final ImmutableSet<EntityType> entityTypes;
   private final EntityConfigLookup entityConfigs;
   private final SectionLabel label;
   private final JComponent separator;
   private final String name;
   private final DynamicProjectTree dynamicTree;
   private final PopupMenuManager popupMenuManager;
   final IntegrationEntityTree tree;
   private IntegrationProject project;
   private RootFolder rootFolder;
   private final ActiveObjectProviderHelper activeObjectHelper;
   private boolean active;
   private final MultiTreeSection.DeployDirtyListener deployDirtyListener;
   private final EntityRootDropHandler labelDropHandler;
   private Optional<ConnectOperationNodeService> connectOpNodeService = Optional.absent();
   private boolean expanded;

   private static Border createBorderForEmptySection() {
      TitledBorder var0 = new TitledBorder(NORMAL_SEPARATOR_BORDER, "        empty");
      var0.setTitleFont(TextStyles.DefaultItalic.getFont());
      var0.setTitleColor(Colors.TEXT_FOREGROUND);
      var0.setTitlePosition(1);
      var0.setTitleJustification(1);
      return var0;
   }

   public MultiTreeSection(
      MultiTreeContentViewer var1,
      ContentViewerCategory var2,
      EntityConfigLookup var3,
      DynamicProjectTree var4,
      PopupMenuManager var5,
      EntityRootDropHandler var6
   ) {
      this.owner = var1;
      this.category = var2;
      this.entityConfigs = var3;
      this.entityTypes = ImmutableSet.copyOf(var2.getTypes());
      this.name = var2.getName();
      this.dynamicTree = var4;
      this.tree = var4.getAdaptee();
      this.popupMenuManager = var5;
      this.labelDropHandler = var6;
      this.label = this.createLabel();
      this.separator = this.createSeparator();
      this.configureTree();
      this.activeObjectHelper = new ActiveObjectProviderHelper(this);
      this.deployDirtyListener = new MultiTreeSection.DeployDirtyListener();
      this.label.setTransferHandler(this.labelDropHandler);
      this.expanded = true;
      this.setProject(null);
      this.updateSeparatorBorder();
      var4.addTreeModelListener(new MultiTreeSection.TreeContentListener());
   }

   private SectionLabel createLabel() {
      EntityIcons.Size var1 = EntityIcons.Size.MEDIUM;
      Icon var2 = this.category.getIcon(var1, EntityIcons.Category.VALID);
      Icon var3 = this.category.getIcon(var1, EntityIcons.Category.INVALID);
      SectionLabel var4 = new SectionLabel(this.category.getName(), var2, var3);
      var4.setBackground(Colors.ETLFORGE_BLUE);
      var4.setSelectedForeground(Color.WHITE);
      var4.setHighlightedBackground(new Color(209, 220, 233));
      Border var5 = Empty.border(1);
      var4.setBorder(var5);
      var4.setSelectedBorder(var5);
      var4.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1) {
            MultiTreeSection.this.owner.inactivateOtherSections(MultiTreeSection.this);
            MultiTreeSection.this.setSelected(true);
         }
      });
      var4.addMouseListener(new MultiTreeSection.MouseHandler());
      this.installLabelActions(var4);
      return var4;
   }

   private void installLabelActions(JLabel var1) {
      TextStyles.TreeRootFolderText.makeOver(var1);
      MultiTreeSection.ToggleAction var2 = new MultiTreeSection.ToggleAction();
      ActionKeyBinding.install(var2, var1);
      DoubleClick.on(var1).toInvoke(var2);
      ActionKeyBinding.install(new MultiTreeSection.ShowContextMenuAction(), var1);
      ActionKeyBinding.install(new MultiTreeSection.LabelKeyUpAction(), var1);
      ActionKeyBinding.install(new MultiTreeSection.LabelKeyDownAction(), var1);
      MultiTreeSection.LabelKeyLeftAction var3 = new MultiTreeSection.LabelKeyLeftAction();
      ActionKeyBinding.install(var3, var1);
      ActionKeyBinding.install(var3, var1, KeyUtils.NUMPAD_MINUS);
      MultiTreeSection.LabelKeyRightAction var4 = new MultiTreeSection.LabelKeyRightAction();
      ActionKeyBinding.install(var4, var1);
      ActionKeyBinding.install(var4, var1, KeyUtils.NUMPAD_PLUS);
      ActionKeyBinding.install(new MultiTreeSection.RefreshAction(), var1);
      ActionKeyBinding.install(new MultiTreeSection.ExpandEverythingAction(), var1);
   }

   private JComponent createSeparator() {
      JSeparator var1 = new JSeparator();
      BoxBuilder var2 = BoxBuilder.horizontal();
      return var2.add(var1).container();
   }

   private void configureTree() {
      this.tree.addKongaTreeListener(new MultiTreeSection.TreeListener());
      this.tree.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1) {
            MultiTreeSection.this.active = true;
            MultiTreeSection.this.activeObjectHelper.notifyListeners(MultiTreeSection.this.tree.getSelectedEntities());
         }
      });
      new MultiTreeSection.TreeKeyUpAction();
      new MultiTreeSection.TreeKeyDownAction();
      new MultiTreeSection.TreeKeyLeftAction();
   }

   private void updateSeparatorBorder() {
      boolean var1 = this.label.isEnabled();
      if (var1 && this.isExpanded() && this.isEmpty()) {
         this.separator.setBorder(BORDER_WHEN_EMPTY);
      } else {
         this.separator.setBorder(NORMAL_SEPARATOR_BORDER);
      }
   }

   public ContentViewerCategory getCategory() {
      return this.category;
   }

   public ImmutableSet<EntityType> getEntityTypes() {
      return this.entityTypes;
   }

   public boolean isAcceptedByFilter(MultiTreeFilter var1) {
      return var1.hasMatchingType(this.category);
   }

   public IntegrationEntityTreeModel getTreeModel() {
      return this.dynamicTree.getModel();
   }

   public void uninstall() {
      this.dynamicTree.dispose();
   }

   @Override
   public boolean isIndicatingValidationStatus() {
      return this.tree.isIndicatingValidationStatus();
   }

   @Override
   public void setIndicateValidationStatus(boolean var1) {
      this.tree.setIndicateValidationStatus(var1);
      ValidationModel var2 = null;
      if (var1) {
         var2 = this.tree.getValidationModel();
      }

      this.updateValidationIndicator(var2);
   }

   @Override
   public boolean isDecoratingDeployDirtyItems() {
      return this.tree.isDecoratingDeployDirtyItems();
   }

   @Override
   public void setDecorateDeployDirtyItems(boolean var1) {
      this.tree.setDecorateDeployDirtyItems(var1);
      this.updateLabelText();
   }

   private void updateLabelText() {
      String var1 = this.getLabelText();
      this.setLabelText(var1);
   }

   private String getLabelText() {
      return this.project != null && this.isDecoratingDeployDirtyItems() && this.isDeployDirty() ? "> " + this.name : this.name;
   }

   private boolean isDeployDirty() {
      if (this.project == null) {
         return false;
      }

      if (this.rootFolder == null) {
         IntegrationEntityTreeModel var1 = this.tree.getModel();
         KongaTreeNode var2 = var1.getRoot();
         int var3 = var1.getChildCount(var2);

         for (int var4 = 0; var4 < var3; var4++) {
            KongaTreeNode var5 = (KongaTreeNode)var1.getChild(var2, var4);
            Object var6 = var5.getUserObject();
            if (var6 instanceof RootFolder && ((RootFolder)var6).isDeployDirty()) {
               return true;
            }
         }

         return false;
      } else {
         return this.rootFolder.isDeployDirty();
      }
   }

   private void setLabelText(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MultiTreeSection.this.label.setText(var1);
         }
      });
   }

   @Override
   public String getProviderId() {
      return "multitree.section." + this.name;
   }

   @Override
   public boolean isActive() {
      return this.active;
   }

   @Override
   public List<?> getActiveObjects() {
      return this.activeObjectHelper.getActiveObjects();
   }

   @Override
   public void addActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.add(var1);
   }

   @Override
   public void removeActiveObjectListener(ActiveObjectProviderListener var1) {
      this.activeObjectHelper.remove(var1);
   }

   public void setSelected(boolean var1) {
      this.label.setSelected(var1);
      if (var1) {
         this.tree.clearSelection();
         this.active = true;
         if (this.rootFolder != null) {
            this.activeObjectHelper.notifyListeners(this.rootFolder);
         }

         Dimension var2 = this.label.getSize();
         Rectangle var3 = new Rectangle(0, -10, var2.width, var2.height + 20);
         this.label.scrollRectToVisible(var3);
         this.label.requestFocusInWindow();
      }
   }

   public void inactivate() {
      this.setSelected(false);
      this.tree.clearSelectionSilently();
      this.activeObjectHelper.clear();
      this.active = false;
   }

   public void setTreeFilter(KongaTreeFilter var1) {
      this.tree.setFilter(var1);
      if (this.expanded) {
         this.updateContentDisplayWhenExpanded();
      }
   }

   public boolean hasSelection() {
      return this.label.isSelected() || this.isExpanded() && this.tree.getSelectionCount() > 0;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.nonOpaque().setAutomaticallyWrapScrollables(false);
      this.tree.setBorder(Empty.border(0, 20, 5, 0));
      var2.northToSouth(this.label, this.tree, this.separator);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (!this.isExpanded() || this.isEmpty() || this.tree.getSelectionCount() <= 0 && this.label.isSelected()) {
         this.label.requestFocusInWindow();
      } else {
         this.tree.requestFocusInWindow();
      }
   }

   public void collectSelectedItems(List<IntegrationEntity> var1) {
      if (this.label.isSelected()) {
         if (this.rootFolder != null) {
            var1.add(this.rootFolder);
         }
      } else {
         var1.addAll(this.tree.getSelectedEntities());
      }
   }

   public void collectExpandedItems(List<IntegrationEntity> var1) {
      if (this.entityTypes.size() > 1) {
         this.collectExpandedTreeNodes(var1);
      } else if (this.rootFolder != null && this.isExpanded()) {
         var1.add(this.rootFolder);
         this.collectExpandedTreeNodes(var1);
      }
   }

   private void collectExpandedTreeNodes(List<IntegrationEntity> var1) {
      for (IntegrationEntityTreeNode var3 : this.tree.getExpandedNodes()) {
         var1.add(var3.getUserObject());
      }
   }

   public final void restoreExpansionState(List<IntegrationEntity> var1) {
      Set var2 = this.getEntitiesToExpand(var1);
      if (var2.isEmpty()) {
         this.collapse();
      } else {
         this.expand();
         this.tree.makeExpanded(var2);
      }
   }

   public void collapse() {
      this.expanded = false;
      this.tree.setVisible(false);
      this.updateSeparatorBorder();
      this.owner.updateExpansionMode();
   }

   public void collapseTree() {
      this.tree.collapseAll();
   }

   public void expand() {
      this.expanded = true;
      this.updateContentDisplayWhenExpanded();
      this.owner.updateExpansionMode();
   }

   private void updateContentDisplayWhenExpanded() {
      boolean var1 = !this.isEmpty();
      this.tree.setVisible(var1);
      this.updateSeparatorBorder();
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void expandTree() {
      this.tree.expandAll();
   }

   private Set<IntegrationEntity> getEntitiesToExpand(List<IntegrationEntity> var1) {
      HashSet var2 = Sets.newHashSet();

      for (IntegrationEntity var4 : var1) {
         if (this.isInTree(var4)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private boolean isInTree(IntegrationEntity var1) {
      EntityType var2 = EntityUtils.getItemType(var1);
      return this.entityTypes.contains(var2);
   }

   public void selectEntity(IntegrationEntity var1) {
      if (this.rootFolder != null && var1 == this.rootFolder) {
         this.setSelected(true);
      } else {
         this.expand();
         this.tree.displayIntegrationEntity(var1);
      }

      this.owner.inactivateOtherSections(this);
   }

   public void rename(final IntegrationEntity var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MultiTreeSection.this.expand();
            MultiTreeSection.this.tree.renameIntegrationEntity(var1);
         }
      });
   }

   public final void refresh(final Folder var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MultiTreeSection.this.tree.refresh(var1);
         }
      });
   }

   public final void updateValidationIndicator(ValidationModel var1) {
      if (this.entityTypes.size() == 1) {
         if (this.rootFolder != null) {
            ValidationStatus var2 = ValidationStatus.VALID;
            if (var1 != null && this.isIndicatingValidationStatus()) {
               var2 = var1.getStatus(this.rootFolder, true);
            }

            this.label.setValidationStatus(var2);
         }
      }
   }

   public void setProject(IntegrationProject var1) {
      this.project = var1;
      this.disposeConnectOperationNodeService();
      this.label.setEnabled(var1 != null);
      this.dynamicTree.setProject(var1);
      this.tree.setEnabled(var1 != null);
      if (var1 == null) {
         this.rootFolder = null;
         this.inactivate();
      } else {
         KongaTreeNode var2 = this.dynamicTree.getModel().getRoot();
         Object var3 = var2.getUserObject();
         this.rootFolder = var3 instanceof RootFolder ? (RootFolder)var3 : null;
         this.installConnectOperationNodeService(var1);
      }

      this.label.setRootFolder(this.rootFolder);
      this.labelDropHandler.setTarget(this.rootFolder);
      this.deployDirtyListener.setProject(var1);
      this.updateTreeVisibilityAndSeparatorBorder();
      this.updateLabelText();
   }

   private void disposeConnectOperationNodeService() {
      if (this.connectOpNodeService.isPresent()) {
         this.connectOpNodeService.get().dispose();
         this.connectOpNodeService = Optional.absent();
      }
   }

   private void installConnectOperationNodeService(IntegrationProject var1) {
      if (this.supportsConnectOperationNodes()) {
         IntegrationEntityTreeModel var2 = this.getTreeModel();
         ConnectOperationNodeService var3 = new ConnectOperationNodeService(var1, var2);
         var3.insertOperationNodes();
         this.connectOpNodeService = Optional.of(var3);
      }
   }

   private boolean supportsConnectOperationNodes() {
      return this.category.getTypes().contains(org.etlforge.integration.data.entity.EntityType.SalesforceConnector);
   }

   private void updateTreeVisibilityAndSeparatorBorder() {
      this.updateTreeVisibility();
      this.updateSeparatorBorder();
   }

   private void updateTreeVisibility() {
      boolean var1 = !this.isEmpty();
      if (var1) {
         if (this.isExpanded() && !this.tree.isVisible()) {
            this.tree.setVisible(true);
         }
      } else if (this.isExpanded() && this.tree.isVisible()) {
         this.tree.setVisible(false);
      }
   }

   public void moveIntoFromBelow() {
      if (this.isExpanded() && !this.isEmpty()) {
         this.tree.selectLastRow();
         this.tree.requestFocusInWindow();
      } else {
         this.setSelected(true);
      }
   }

   private boolean isEmpty() {
      IntegrationEntityTreeModel var1 = this.tree.getModel();
      KongaTreeNode var2 = var1.getRoot();
      return var1.getChildCount(var2) == 0;
   }

   public void setItemCount(int var1) {
      this.label.setItemCount(var1);
   }

   @Override
   public String toString() {
      return this.name;
   }

   private void showRootContextMenu(Point var1) {
      JPopupMenu var2 = this.createRootPopup();
      if (var2 != null) {
         var2.show(this.label, var1.x, var1.y);
      }
   }

   private JPopupMenu createRootPopup() {
      if (this.rootFolder != null) {
         this.activeObjectHelper.notifyListeners(this.rootFolder);
         return this.popupMenuManager.getPopupMenu(PopupMenuSites.TREE, Collections.singletonList(this.rootFolder));
      } else {
         return this.category.getRootMenu(this.entityConfigs).orNull();
      }
   }

   private class DeployDirtyListener implements PropertyChangeListener, Disposable {
      private ProjectPropertyChangeSupport changeSupport;

      private DeployDirtyListener() {
      }

      public void setProject(IntegrationProject var1) {
         this.dispose();
         if (var1 != null) {
            this.changeSupport = new ProjectPropertyChangeSupport(var1, false);
            this.changeSupport.addPropertyChangeListener("IntegrationEntity#DeployDirty", this);
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (MultiTreeSection.this.isDecoratingDeployDirtyItems()) {
            MultiTreeSection.this.updateLabelText();
         }
      }

      @Override
      public void dispose() {
         if (this.changeSupport != null) {
            this.changeSupport.dispose();
            this.changeSupport = null;
         }
      }
   }

   private class ExpandEverythingAction extends AbstractAction {
      public ExpandEverythingAction() {
         this.putValue("AcceleratorKey", KeyUtils.EXPAND_EVERYTHING);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         MultiTreeSection.this.expand();
         MultiTreeSection.this.expandTree();
      }
   }

   private class LabelKeyDownAction extends AbstractAction {
      public LabelKeyDownAction() {
         this.putValue("AcceleratorKey", KeyUtils.DOWN);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (MultiTreeSection.this.isExpanded() && !MultiTreeSection.this.isEmpty()) {
            MultiTreeSection.this.tree.setSelectionRow(0);
            MultiTreeSection.this.tree.requestFocusInWindow();
         } else {
            MultiTreeSection.this.owner.moveToNextSection(MultiTreeSection.this);
         }
      }
   }

   private class LabelKeyLeftAction extends AbstractAction {
      public LabelKeyLeftAction() {
         this.putValue("AcceleratorKey", KeyUtils.LEFT);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (MultiTreeSection.this.isExpanded()) {
            MultiTreeSection.this.collapse();
         } else {
            MultiTreeSection.this.owner.moveToPreviousSection(MultiTreeSection.this, false);
         }
      }
   }

   private class LabelKeyRightAction extends AbstractAction {
      public LabelKeyRightAction() {
         this.putValue("AcceleratorKey", KeyUtils.RIGHT);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (MultiTreeSection.this.isExpanded()) {
            MultiTreeSection.this.tree.setSelectionRow(0);
            MultiTreeSection.this.tree.requestFocusInWindow();
         } else {
            MultiTreeSection.this.expand();
         }
      }
   }

   private class LabelKeyUpAction extends AbstractAction {
      public LabelKeyUpAction() {
         this.putValue("AcceleratorKey", KeyUtils.UP);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         MultiTreeSection.this.owner.moveToPreviousSection(MultiTreeSection.this, true);
      }
   }

   private class MouseHandler extends MouseActionListener {
      private MouseHandler() {
      }

      @Override
      protected void onPopupTrigger(final MouseEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MultiTreeSection.this.setSelected(true);
               Point var1x = var1.getPoint();
               MultiTreeSection.this.showRootContextMenu(var1x);
            }
         });
      }

      @Override
      public void mouseEntered(MouseEvent var1) {
         ((SectionLabel)var1.getComponent()).setHighlighted(true);
      }

      @Override
      public void mouseExited(MouseEvent var1) {
         ((SectionLabel)var1.getComponent()).setHighlighted(false);
      }
   }

   private class RefreshAction extends AbstractAction {
      public RefreshAction() {
         this.putValue("AcceleratorKey", KeyUtils.REFRESH);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (MultiTreeSection.this.rootFolder != null) {
            MultiTreeSection.this.tree.refresh(MultiTreeSection.this.rootFolder);
         }
      }
   }

   private class ShowContextMenuAction extends AbstractAction {
      public ShowContextMenuAction() {
         this.putValue("AcceleratorKey", KeyUtils.CONTEXT_MENU);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         MultiTreeSection.this.showRootContextMenu(new Point(60, 12));
      }
   }

   private class ToggleAction extends AbstractAction {
      public ToggleAction() {
         this.putValue("AcceleratorKey", KeyUtils.SPACE);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         boolean var2 = MultiTreeSection.this.isExpanded();
         if (var2) {
            MultiTreeSection.this.collapse();
         } else {
            MultiTreeSection.this.expand();
         }
      }
   }

   private class TreeContentListener extends TreeModelAdapter {
      private TreeContentListener() {
      }

      @Override
      public void treeNodesInserted(TreeModelEvent var1) {
         MultiTreeSection.this.updateTreeVisibilityAndSeparatorBorder();
      }

      @Override
      public void treeNodesRemoved(TreeModelEvent var1) {
         MultiTreeSection.this.updateTreeVisibilityAndSeparatorBorder();
      }
   }

   private class TreeKeyDownAction extends KeyDownReplacementAction {
      public TreeKeyDownAction() {
         super(MultiTreeSection.this.tree);
      }

      @Override
      protected void handleSpecialCase() {
         MultiTreeSection.this.owner.moveToNextSection(MultiTreeSection.this);
      }
   }

   private class TreeKeyLeftAction extends KeyLeftReplacementAction {
      public TreeKeyLeftAction() {
         super(MultiTreeSection.this.tree);
      }

      @Override
      protected void handleSpecialCase() {
         MultiTreeSection.this.setSelected(true);
      }
   }

   private class TreeKeyUpAction extends KeyUpReplacementAction {
      public TreeKeyUpAction() {
         super(MultiTreeSection.this.tree);
      }

      @Override
      protected void handleSpecialCase() {
         MultiTreeSection.this.setSelected(true);
      }
   }

   private class TreeListener extends KongaTreeAdapter {
      private TreeListener() {
      }

      @Override
      public void selectionChanged(KongaTreeEvent var1) {
         int var2 = MultiTreeSection.this.tree.getSelectionCount();
         if (var2 > 0) {
            List var3 = MultiTreeSection.this.tree.getSelectedEntities();
            MultiTreeSection.this.activeObjectHelper.notifyListeners(var3);
            MultiTreeSection.this.setSelected(false);
            MultiTreeSection.this.owner.inactivateOtherSections(MultiTreeSection.this);
         }
      }
   }
}

