package org.etlforge.integration.client.ui.crom.tree;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.awt.Component;
import java.awt.Font;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.ui.crom.xml.DerivedTypesSelector;
import org.etlforge.integration.client.ui.crom.xml.SubstitutionGroupsSelector;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.progress.ProgressMessage;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.ui.widget.tree.NodeStatus;
import org.etlforge.ui.widget.tree.TreeSelectionSource;

public final class CromTreeWidget implements UiProvider, SelectionSource {
   private final CromTreeModelBuilder modelBuilder = new CromTreeModelBuilder();
   private final KongaTree tree;
   private volatile CROM_Tree cromTree;
   private final ContextMenuService contextMenuService;
   private final TreeSelectionSource selectionSource;
   private final ConcurrentHashMap<CromTreeNode, Boolean> explodingNodes;
   private CromTreeWidget.WaitNode waitNode;

   public CromTreeWidget() {
      this.modelBuilder.setIncludeLeafNodes(true);
      this.tree = this.createEmptyTree();
      this.selectionSource = new TreeSelectionSource(this.tree);
      this.selectionSource.setNodeObjectConverter(new CromTreeWidget.NodeObjectConverterImpl(Predicates.alwaysFalse()));
      this.explodingNodes = new ConcurrentHashMap<>();
      this.contextMenuService = new ContextMenuService(this.tree, this.selectionSource);
   }

   public CromTreeWidget(CROM_Tree var1) {
      this();
      if (var1 != null) {
         this.setCromTree(var1);
      }
   }

   private KongaTree createEmptyTree() {
      KongaTree var1 = new KongaTree(new DefaultKongaTreeModel(new DefaultKongaTreeNode("root", true)));
      var1.setRootVisible(false);
      var1.addTreeWillExpandListener(new CromTreeWidget.NodeExpansionHandler());
      var1.setCellRenderer(new CromTreeWidget.Renderer());
      return var1;
   }

   public void setCromTree(CROM_Tree var1) {
      Preconditions.checkNotNull(var1, "cromTree");
      this.cromTree = var1;
      this.displayWaitNode();
      CromTreeWidget.BackgroundModelBuilder var2 = new CromTreeWidget.BackgroundModelBuilder(var1.getRoot());
      var2.execute();
   }

   public CROM_Tree getCromTree() {
      return this.cromTree;
   }

   public void displayWaitNode() {
      if (this.waitNode == null) {
         DefaultKongaTreeNode var1 = this.getRoot();
         var1.removeAllChildren();
         this.waitNode = new CromTreeWidget.WaitNode(this.tree);
         var1.addChild(this.waitNode);
         this.waitNode.start();
         this.fireNodeStructureChanged(var1);
      }
   }

   private DefaultKongaTreeNode getRoot() {
      return (DefaultKongaTreeNode)this.tree.getModel().getRoot();
   }

   private void fireNodeStructureChanged(DefaultKongaTreeNode var1) {
      DefaultKongaTreeModel var2 = (DefaultKongaTreeModel)this.tree.getModel();
      var2.fireTreeStructureChanged(this.tree.getModel(), var2.getPath(var1));
   }

   public void removeWaitNode() {
      if (this.waitNode != null) {
         if (this.tree.isPartOfTree(this.waitNode)) {
            this.waitNode.removeFromParent();
            this.fireNodeStructureChanged(this.getRoot());
         }

         this.waitNode.stop();
         this.waitNode = null;
      }
   }

   public final void setIncludeLeafNodes(boolean var1) {
      this.modelBuilder.setIncludeLeafNodes(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.tree.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }

   public void expandRoot() {
      if (this.tree.getRowCount() > 0) {
         this.tree.expandRow(0);
      }
   }

   public void expandAll() {
      DefaultKongaTreeNode var1 = this.getRoot();
      if (var1 instanceof CromTreeNode) {
         this.expandAllNodesButDoNotExplode((CromTreeNode)var1);
      } else {
         this.tree.expandAll();
      }
   }

   private void expandAllNodesButDoNotExplode(CromTreeNode var1) {
      if (var1.isFullyExpanded()) {
         this.tree.makeNodeExpanded(var1);

         for (KongaTreeNode var3 : var1.children()) {
            this.expandAllNodesButDoNotExplode((CromTreeNode)var3);
         }
      }
   }

   public void setDisabledNodeCondition(Predicate<CROM> var1) {
      Predicate var2 = var1 == null ? Predicates.alwaysFalse() : var1;
      this.selectionSource.setNodeObjectConverter(new CromTreeWidget.NodeObjectConverterImpl(var2));
      this.tree.repaint();
   }

   public void setSelectionMode(KongaTree.SelectionMode var1) {
      this.tree.setSelectionMode(var1);
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

   public void setDefaultAction(SelectionAction var1) {
      this.selectionSource.setDefaultAction(var1);
   }

   public TreePanel inTreePanel(String var1) {
      TreePanel var2 = new TreePanel(var1, this.tree);
      BorderBuilder var3 = new BorderBuilder(StyleSheet.TreeTitleBorder).empty(2, 0, 2, 0);
      return var2.setRootVisible(true).setIncludeToolBar(true).setTitlePanelBorder(var3);
   }

   public void setRootVisible(boolean var1) {
      this.tree.setRootVisible(var1);
   }

   public void rebuildNode(CROM var1) {
      CromTreeNode var2 = this.findTreeNode(var1);
      if (var2 != null) {
         boolean var3 = this.tree.isNodeExpanded(var2);
         var2.removeAllChildren();
         this.modelBuilder.buildTree(var1, var2);
         this.fireNodeStructureChanged(var2);
         if (var3) {
            this.tree.makeNodeExpanded(var2);
         }
      }
   }

   private CromTreeNode findTreeNode(CROM var1) {
      DefaultKongaTreeNode var2 = this.getRoot();

      for (KongaTreeNode var4 : var2.depthFirstEnumeration()) {
         if (var4.getUserObject() == var1) {
            return (CromTreeNode)var4;
         }
      }

      return null;
   }

   public void scrollNodeToVerticalCenter(CROM var1) {
      CromTreeNode var2 = this.findTreeNode(var1);
      if (var2 != null) {
         this.tree.scrollNodeToVerticalCenter(var2);
      }
   }

   public void expandNode(CROM var1) {
      CromTreeNode var2 = this.findTreeNode(var1);
      if (var2 != null) {
         this.tree.makeNodeExpanded(var2);
      }
   }

   public void selectDerivedType(CROM var1) {
      Preconditions.checkArgument(var1.isDerivedTypeHolder(), "crom is not a derived type holder");
      var1.expandNode(this.cromTree);
      DerivedTypesSelector.open(this, var1, this.tree);
   }

   public void selectSubstitutionGroup(CROM var1) {
      Preconditions.checkArgument(var1.isSubstitutionGroupHead(), "crom is not a substitution group head");
      var1.expandNode(this.cromTree);
      SubstitutionGroupsSelector.open(this, var1, this.tree);
   }

   public void addContextMenuAction(SelectionAction var1) {
      this.contextMenuService.addAction(var1);
   }

   private class BackgroundModelBuilder extends KongaSwingWorker<DefaultKongaTreeModel, Void> {
      private final CROM root;

      public BackgroundModelBuilder(CROM var2) {
         this.root = var2;
      }

      protected DefaultKongaTreeModel doInBackground() throws Exception {
         if (!this.root.hasMoreThanNilChildren()) {
            this.root.expandNode(CromTreeWidget.this.cromTree);
         }

         return CromTreeWidget.this.modelBuilder.buildModel(this.root);
      }

      @Override
      protected void finished() {
         CromTreeWidget.this.waitNode.stop();
         CromTreeWidget.this.waitNode = null;
      }

      protected void onSuccess(DefaultKongaTreeModel var1) {
         CromTreeWidget.this.tree.setModel(var1);
         CromTreeWidget.this.tree.setShowsRootHandles(true);
      }

      @Override
      protected void onFailure(Throwable var1) {
         this.removeWaitNode();
      }

      private void removeWaitNode() {
         DefaultKongaTreeNode var1 = CromTreeWidget.this.getRoot();
         var1.removeAllChildren();
         CromTreeWidget.this.fireNodeStructureChanged(var1);
      }
   }

   private class Exploder extends KongaSwingWorker<Void, Void> {
      private final CromTreeNode node;

      public Exploder(CromTreeNode var2) {
         this.node = var2;
      }

      protected Void doInBackground() throws Exception {
         CROM var1 = this.node.getUserObject();
         var1.expandNode(CromTreeWidget.this.cromTree);
         return null;
      }

      protected void onSuccess(Void var1) {
         this.node.removeAllChildren();
         CromTreeWidget.this.modelBuilder.buildTree(this.node.getUserObject(), this.node);
         CromTreeWidget.this.fireNodeStructureChanged(this.node);
         if (this.node.isFullyExpanded()) {
            CromTreeWidget.this.tree.makeNodeExpanded(this.node);
         }
      }

      @Override
      protected void finished() {
         CromTreeWidget.this.explodingNodes.remove(this.node);
      }
   }

   private class NodeExpansionHandler implements TreeWillExpandListener {
      private NodeExpansionHandler() {
      }

      @Override
      public void treeWillExpand(TreeExpansionEvent var1) throws ExpandVetoException {
         TreePath var2 = var1.getPath();
         Object var3 = var2.getLastPathComponent();
         if (var3 instanceof CromTreeNode) {
            this.explodeNodeIfNecessary((CromTreeNode)var3);
         }
      }

      private void explodeNodeIfNecessary(CromTreeNode var1) {
         if (!var1.isFullyExpanded()) {
            CROM var2 = var1.getUserObject();
            if (var2.isEmptyDerivedTypeHolder()) {
               CromTreeWidget.this.selectDerivedType(var2);
            } else if (var2.isEmptySubstitutionGroupHead()) {
               CromTreeWidget.this.selectSubstitutionGroup(var2);
            } else if (!this.isBeingExploded(var1)) {
               CromTreeWidget.Exploder var3 = CromTreeWidget.this.new Exploder(var1);
               var3.execute();
            }
         }
      }

      private boolean isBeingExploded(CromTreeNode var1) {
         return CromTreeWidget.this.explodingNodes.putIfAbsent(var1, Boolean.TRUE) != null;
      }

      @Override
      public void treeWillCollapse(TreeExpansionEvent var1) throws ExpandVetoException {
      }
   }

   private static class NodeObjectConverterImpl implements NodeObjectConverter {
      private final Predicate<CROM> disabledNodes;

      public NodeObjectConverterImpl(Predicate<CROM> var1) {
         this.disabledNodes = var1;
      }

      @Override
      public Object toObject(TreeNode var1) {
         if (var1 instanceof CromTreeNode) {
            CROM var2 = ((CromTreeNode)var1).getUserObject();
            return this.disabledNodes.apply(var2) ? null : var2;
         } else {
            return null;
         }
      }

      @Override
      public TreeNode toNode(Object var1) {
         throw new NotImplementedYetException();
      }
   }

   private static class Renderer extends DefaultTreeCellRenderer {
      private final TextStyle folderStyle;
      private final TextStyle leafStyle = TextStyles.TreeLeafText;

      public Renderer() {
         this.folderStyle = TextStyle.newStyle(this.leafStyle, 1);
      }

      @Override
      public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         if (var2 instanceof CromTreeNode) {
            this.decorateCromNode((CromTreeNode)var2, var4);
         } else {
            this.setIcon(null);
         }

         return this;
      }

      private void decorateCromNode(CromTreeNode var1, boolean var2) {
         Font var3 = var1.isLeaf() ? this.leafStyle.getFont() : this.folderStyle.getFont();
         this.setFont(var3);
         this.setIcon(var1.getNodeIcon(var2 ? NodeStatus.OPENED : NodeStatus.CLOSED));
      }
   }

   private static final class WaitNode extends DefaultKongaTreeNode {
      private final KongaTree tree;
      private final ProgressMessage message;

      public WaitNode(KongaTree var1) {
         super("", false);
         this.tree = var1;
         this.message = new ProgressMessage("Please wait while the tree is constructed", new Receiver<String>() {
            public void handle(String var1) {
               WaitNode.this.updateNode();
            }
         });
      }

      private void updateNode() {
         this.tree.getModel().nodeChanged(this);
         this.tree.repaint();
      }

      public void start() {
         this.message.setUpdateAutomatically(200);
      }

      public void stop() {
         this.message.stopAutomaticUpdate();
      }

      @Override
      public String getDisplayName() {
         return this.message.getText();
      }
   }
}
