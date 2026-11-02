package org.etlforge.integration.client.ui.de.global.tree;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.search.string.StringFilterField;
import org.etlforge.integration.client.ui.de.global.DataElementsWidget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.LeafDisplayNameFilter;
import org.etlforge.ui.widget.tree.PreOrderTraverser;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public final class DataElementsTreeWidget implements DataElementsWidget {
   private final KongaTree tree;
   private final TreeSelectionSource selectionSource;
   private final StringFilterField filterField;
   private GlobalDataElements deStore;
   private boolean includeBuiltInDataElements;
   private final Renderer renderer = new Renderer();

   public DataElementsTreeWidget() {
      this.tree = this.createTree();
      this.selectionSource = new TreeSelectionSource(this.tree);
      this.selectionSource.setNodeObjectConverter(new SelectionConverter(this.tree.getModel()));
      this.filterField = this.createFilterField();
   }

   private KongaTree createTree() {
      DefaultKongaTreeModel var1 = new DefaultKongaTreeModel(createRootNode());
      KongaTree var2 = new KongaTree(var1);
      var2.setDragEnabled(true);
      var2.setToolTipsEnabled(true);
      var2.setTransferHandler(new TransferHandlerImpl());
      var2.setCellRenderer(this.renderer);
      var2.setRootVisible(false);
      return var2;
   }

   private static DefaultKongaTreeNode createRootNode() {
      return new DefaultKongaTreeNode("root", true);
   }

   private StringFilterField createFilterField() {
      StringFilterField var1 = new StringFilterField();
      var1.setTrim(true);
      var1.setTextWhenEmpty("Filter");
      var1.setCallback(new Receiver<Predicate<String>>() {
         public void handle(Predicate<String> var1) {
            LeafDisplayNameFilter var2 = new LeafDisplayNameFilter(var1);
            DataElementsTreeWidget.this.tree.setFilter(var2);
         }
      });
      var1.addKeyBinding(new ArrowDownAction(this.tree));
      return var1;
   }

   @Override
   public void setDataElements(GlobalDataElements var1) {
      this.deStore = var1;
      this.update(false, true);
   }

   private void update(final boolean var1, final boolean var2) {
      final KongaTreeModel var3 = TreeModelBuilder.newModel(this.deStore, this.includeBuiltInDataElements);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Object var1x = DataElementsTreeWidget.this.getSelection().head();
            Object var2x = DataElementsTreeWidget.this.tree.getExpansionMemento(new KongaTree.DefaultNodeKeyExtractor(DataElementsTreeWidget.this.tree));
            DataElementsTreeWidget.this.tree.setModel(var3);
            DataElementsTreeWidget.this.tree.restoreExpansion(var2x);
            DataElementsTreeWidget.this.selectionSource.setNodeObjectConverter(new SelectionConverter(var3));
            if (var2) {
               DataElementsTreeWidget.this.tree.expandAll();
            }

            if (var1) {
               this.retainSelection(var1x);
            }
         }

         private void retainSelection(Object var1x) {
            DataElementsTreeWidget.this.selectionSource.setSelection(SelectionFactory.newOrderedSelection(var1x));
         }
      });
   }

   @Override
   public void update() {
      this.update(true, false);
   }

   @Override
   public void setActionProvider(Supplier<List<Action>> var1) {
      Preconditions.checkNotNull(var1);
      PopupMenuRequestHandlers.install(this.tree, new ContextMenuHandler(var1));
   }

   @Override
   public void setIncludeBuiltInDataElements(boolean var1) {
      if (this.includeBuiltInDataElements != var1) {
         this.includeBuiltInDataElements = var1;
         if (this.deStore != null) {
            this.update(false, false);
         }

         if (var1) {
            this.expandETLForgeVariables();
         }
      }
   }

   private void expandETLForgeVariables() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            for (Object var2 : new PreOrderTraverser(DataElementsTreeWidget.this.tree.getModel())) {
               if (var2 instanceof ETLForgeVariablesNode) {
                  DataElementsTreeWidget.this.tree.makeNodeExpanded(var2);
                  return;
               }
            }
         }
      });
   }

   @Override
   public void setProcess(IntegrationEntity var1) {
      this.renderer.setProcess(var1);
      this.tree.repaint();
   }

   public void setVisibleRowCount(int var1) {
      this.tree.setVisibleRowCount(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.center(this.tree.asUiProvider()).south(this.filterField);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
      if (this.tree.getSelectionCount() == 0) {
         this.selectFirst();
      }
   }

   @Override
   public boolean isEmpty() {
      return this.tree.getRowCount() > 0;
   }

   @Override
   public void selectFirst() {
      this.tree.selectFirstRow();
   }

   @Override
   public void setDefaultAction(SelectionAction var1) {
      this.selectionSource.setDefaultAction(var1);
   }

   public OrderedSelection getSelection() {
      return this.selectionSource.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSource.addSelectionListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionSource.removeSelectionListener(var1);
   }

   @Override
   public void setFilter(Predicate<String> var1) {
      LeafDisplayNameFilter var2 = new LeafDisplayNameFilter(var1);
      this.tree.setFilter(var2);
   }
}
