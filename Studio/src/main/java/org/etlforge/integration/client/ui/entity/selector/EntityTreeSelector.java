package org.etlforge.integration.client.ui.entity.selector;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.tree.ExpandCollapseToggleAction;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.ui.entity.tree.DynamicTreeModelUpdater;
import org.etlforge.integration.client.ui.entity.tree.EntityTreeFilter;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.integration.client.ui.entity.tree.ItemListToTreeModelBuilder;
import org.etlforge.integration.client.ui.entity.tree.ProjectTreeFragmentCreator;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionSourceWithDefaultAction;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.focus.Focusable;
import org.etlforge.ui.widget.tree.BreadthFirstTraverser;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeCompositeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter2;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.PreOrderTraverser;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;
import org.etlforge.ui.widget.tree.TreeFilterField;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public final class EntityTreeSelector<T extends IntegrationEntity> implements EntitySelector<T>, Focusable {
   private final IntegrationProject project;
   private final Class<T> clazz;
   private EntitySelector.SelectionMode mode;
   private final IntegrationEntityTreeModel treeModel;
   private final KongaTree tree;
   private final TreeSelectionSource treeSelSrc;
   private boolean hasBeenFocused;
   private boolean includeToolbar;
   private String toolbarTitle;
   private DynamicTreeModelUpdater dynamicUpdater;
   private Function<String, KongaTreeFilter> treeFilterFunction;
   private ExpandCollapseToggleAction expansionToggle;
   private EntityFilter entityFilter = EntityFilters.ACCEPTS_ALL;
   private KongaTreeFilter nonTextFilter;
   private KongaTreeFilter textFilter;

   public static <E extends IntegrationEntity> EntityTreeSelector<E> newTree(IntegrationProject var0, Class<E> var1) {
      return new EntityTreeSelector<>(var0, var1);
   }

   public static <E extends IntegrationEntity> EntityTreeSelector<E> newTree(Iterable<E> var0, Class<E> var1) {
      return new EntityTreeSelector<>(var0, var1);
   }

   public EntityTreeSelector(IntegrationProject var1, Class<T> var2) {
      this(var1, var2, ProjectTreeFragmentCreator.createTreeModel(var1, var2, true));
   }

   public EntityTreeSelector(Iterable<T> var1, Class<T> var2) {
      this(null, var2, createModelFromList(var1, var2));
   }

   private static <E extends IntegrationEntity> IntegrationEntityTreeModel createModelFromList(Iterable<E> var0, Class<E> var1) {
      for (IntegrationEntity var3 : var0) {
         if (!(var3 instanceof IntegrationProject) && !(var3 instanceof Folder) && !var1.isInstance(var3)) {
            throw new RuntimeException("Expected items of type " + var1.getName() + ", but found " + var3.getClass().getName());
         }
      }

      ItemListToTreeModelBuilder var4 = new ItemListToTreeModelBuilder();
      return var4.createModel(var0);
   }

   private EntityTreeSelector(IntegrationProject var1, Class<T> var2, IntegrationEntityTreeModel var3) {
      this.project = var1;
      this.clazz = var2;
      this.treeModel = var3;
      this.mode = EntitySelector.SelectionMode.SINGLE;
      this.tree = this.createTree(var3);
      this.treeSelSrc = this.createSelectionSource();
   }

   private KongaTree createTree(KongaTreeModel var1) {
      KongaTree var2 = new KongaTree(var1);
      IntegrationEntityTreeRenderer var3 = new IntegrationEntityTreeRenderer();
      var3.setDecorateInvalidNodes(true);
      var2.setCellRenderer(var3);
      var2.setShowsRootHandles(true);
      var2.expandAll();
      return var2;
   }

   private TreeSelectionSource createSelectionSource() {
      TreeSelectionSource var1 = new TreeSelectionSource(this.tree);
      var1.setNodeObjectConverter(new EntityTreeSelector.SelectionConverter());
      return var1;
   }

   public void setSelectionMode(EntitySelector.SelectionMode var1) {
      this.mode = Preconditions.checkNotNull(var1, "mode");
   }

   public void setIncludeToolbar(boolean var1) {
      this.includeToolbar = var1;
   }

   public void setToolbarTitle(String var1) {
      this.toolbarTitle = var1;
   }

   public void setProjectVisible(boolean var1) {
      this.tree.setRootVisible(var1);
   }

   public void setTreeFilterFunction(Function<String, KongaTreeFilter> var1) {
      this.treeFilterFunction = var1;
   }

   @Override
   public T getSelectedEntity() {
      return this.mode == EntitySelector.SelectionMode.SINGLE ? this.getSingleSelection() : this.getFirstOfMultipleSelection();
   }

   private T getFirstOfMultipleSelection() {
      List var1 = this.getSelectedEntities();
      return (T)(var1.isEmpty() ? null : var1.get(0));
   }

   private T getSingleSelection() {
      OrderedSelection var1 = this.treeSelSrc.getSelection();
      if (var1.size() == 1) {
         IntegrationEntity var2 = (IntegrationEntity)var1.head();
         if (this.isValidType(var2)) {
            return this.clazz.cast(var2);
         }
      }

      return null;
   }

   private boolean isValidType(IntegrationEntity var1) {
      return this.clazz.isInstance(var1);
   }

   @Override
   public List<T> getSelectedEntities() {
      ArrayList var1 = Lists.newArrayList();

      for (Object var3 : this.treeSelSrc.getSelection().items()) {
         IntegrationEntity var4 = (IntegrationEntity)var3;
         if (this.isValidType(var4)) {
            var1.add(this.clazz.cast(var4));
         }
      }

      return var1;
   }

   @Override
   public void setSelectedEntity(T var1) {
      if (var1 == null) {
         this.tree.clearSelection();
      } else {
         this.selectEntityImpl((T)var1);
      }
   }

   private void selectEntityImpl(T var1) {
      for (Object var3 : new PreOrderTraverser(this.treeModel)) {
         if (((KongaTreeNode)var3).getUserObject() == var1) {
            this.tree.makeNodeSelected(var3, false);
            return;
         }
      }
   }

   public void selectFirst() {
      for (int var1 = 0; var1 < this.tree.getVisibleRowCount(); var1++) {
         TreePath var2 = this.tree.getPathForRow(var1);
         if (var2 == null) {
            return;
         }

         Object var3 = var2.getLastPathComponent();
         if (var3 instanceof IntegrationEntityTreeNode) {
            IntegrationEntity var4 = ((IntegrationEntityTreeNode)var3).getUserObject();
            if (this.isValidType(var4)) {
               this.tree.makeNodeSelected(var3, false);
               return;
            }
         }
      }
   }

   public SelectionSourceWithDefaultAction getSelectionSource() {
      return this.treeSelSrc;
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.treeSelSrc.setDefaultAction(var1);
   }

   @Override
   public void setFilter(EntityFilter var1) {
      this.entityFilter = var1 == null ? EntityFilters.ACCEPTS_ALL : var1;
      KongaTreeFilter var2 = var1 != null ? EntityTreeFilter.memoize(var1) : null;
      this.nonTextFilter = var2;
      this.combineFilters();
   }

   private void combineFilters() {
      final KongaTreeFilter var1 = this.createCombinedFilters();
      if (var1 != null && !(var1 instanceof KongaTreeFilter2)) {
         this.tree.setFilter(new KongaTreeFilter2() {
            @Override
            public boolean apply(KongaTreeNode var1x) {
               return var1.apply(var1x);
            }

            @Override
            public void hasBeenApplied(KongaTree var1x) {
               var1x.expandAllNonEmptyFolderNodes();
            }
         });
      } else {
         this.tree.setFilter(var1);
      }
   }

   private KongaTreeFilter createCombinedFilters() {
      if (this.nonTextFilter != null && this.textFilter != null) {
         return KongaTreeCompositeFilter.memoize(this.nonTextFilter, this.textFilter);
      } else if (this.nonTextFilter != null) {
         return this.nonTextFilter;
      } else {
         return this.textFilter != null ? this.textFilter : null;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.includeToolbar) {
         this.layoutWithToolbar(var1);
      } else if (this.treeFilterFunction == null) {
         this.tree.asUiProvider().decorate(var1);
      } else {
         this.layoutWithFilterFieldButNoToolbar(var1);
      }
   }

   private void layoutWithToolbar(UiCanvas var1) {
      TreePanel var2 = new TreePanel(this.toolbarTitle, this.tree);
      var2.setTitlePanelBackground(Colors.ETLFORGE_BLUE);
      var2.setTitleStyle(TextStyle.newColor(TextStyles.AdminHintBold, Color.WHITE));
      if (this.treeFilterFunction != null) {
         var2.showFilterField(this.treeFilterFunction, false);
         var2.setFilterInterceptor(new EntityTreeSelector.FilterFieldInterceptor());
         var2.setLayoutFilterFieldBelowTheTree(true);
      }

      var2.decorate(var1);
   }

   private void layoutWithFilterFieldButNoToolbar(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(5, 10);
      TreeFilterField var3 = this.createFilterField(this.treeFilterFunction, null);
      var2.center(this.tree).south(var3);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
      if (!this.hasBeenFocused && this.tree.getSelectionCount() == 0) {
         this.tree.makeNodeSelected(this.getInitialSelection(), false);
         this.hasBeenFocused = true;
      }
   }

   private KongaTreeNode getInitialSelection() {
      for (Object var3 : new BreadthFirstTraverser(this.treeModel)) {
         if (var3 instanceof IntegrationEntityTreeNode) {
            IntegrationEntityTreeNode var4 = (IntegrationEntityTreeNode)var3;
            IntegrationEntity var5 = var4.getUserObject();
            if (this.isValidType(var5)) {
               return var4;
            }
         }
      }

      return null;
   }

   public Disposable updateAutomatically() {
      if (this.project == null) {
         return new Disposable() {
            @Override
            public void dispose() {
            }
         };
      }

      if (this.dynamicUpdater == null) {
         this.dynamicUpdater = new DynamicTreeModelUpdater(this.project, this.treeModel);
      }

      return this.dynamicUpdater;
   }

   public void expandAll() {
      this.tree.expandAll();
   }

   public void collapseAll() {
      this.tree.collapseAll();
   }

   public void expandRootFolders() {
      KongaTreeNode var1 = this.treeModel.getRoot();
      if (var1.getUserObject() instanceof IntegrationProject) {
         int var2 = this.treeModel.getChildCount(var1);

         for (int var3 = 0; var3 < var2; var3++) {
            KongaTreeNode var4 = this.treeModel.getChild(var1, var3);
            this.tree.makeNodeExpanded(var4);
         }
      }
   }

   public ExpandCollapseToggleAction getExpansionToggle() {
      if (this.expansionToggle == null) {
         this.expansionToggle = new ExpandCollapseToggleAction(this.tree);
      }

      return this.expansionToggle;
   }

   public TreeFilterField createFilterField(Function<String, KongaTreeFilter> var1, String var2) {
      SimpleTreeFilterField var3 = new SimpleTreeFilterField(this.tree, var1);
      if (var2 != null) {
         var3.setLabel(var2);
      }

      var3.setInterceptor(new EntityTreeSelector.FilterFieldInterceptor());
      return var3;
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.tree.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.tree.removeFocusListener(var1);
   }

   private class FilterFieldInterceptor implements Receiver<KongaTreeFilter> {
      private FilterFieldInterceptor() {
      }

      public void handle(KongaTreeFilter var1) {
         EntityTreeSelector.this.textFilter = var1;
         EntityTreeSelector.this.combineFilters();
      }
   }

   private final class SelectionConverter implements NodeObjectConverter {
      private SelectionConverter() {
      }

      @Override
      public TreeNode toNode(Object var1) {
         return var1 instanceof IntegrationEntity ? EntityTreeSelector.this.treeModel.findNodeForObject((IntegrationEntity)var1) : null;
      }

      @Override
      public Object toObject(TreeNode var1) {
         if (var1 instanceof KongaTreeNode) {
            KongaTreeNode var2 = (KongaTreeNode)var1;
            Object var3 = var2.getUserObject();
            if (this.isSelectable(var3)) {
               return var3;
            }
         }

         return null;
      }

      private boolean isSelectable(Object var1) {
         return var1 instanceof IntegrationEntity && EntityTreeSelector.this.entityFilter.apply((IntegrationEntity)var1);
      }
   }
}
