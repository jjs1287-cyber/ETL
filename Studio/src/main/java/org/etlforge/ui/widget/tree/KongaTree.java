package org.etlforge.ui.widget.tree;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.PopupMenuInvoker;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.KongaWidget;
import org.etlforge.ui.widget.tree.event.KongaTreeEvent;
import org.etlforge.ui.widget.tree.event.KongaTreeListener;

public class KongaTree extends JTree implements KongaWidget, PopupMenuInvoker {
   private KongaTree.MouseClickListener mouseClickListener;
   private final KongaTree.SelectionSender selectionSender;
   private MouseDragSelector mouseDragSelector;
   private MouseRenameListener mouseRenameListener;
   private boolean enableMouseRename;
   private RenameNodeHandler renameNodeHandler;
   private KongaTree.KongaExpansionListener expansionListener;
   private List<KongaTreeListener> treeListenerList;
   private boolean scrollableTracksViewportWidth;
   private boolean tooltipsEnabled;
   private ComponentPainter painter;
   private Function<Object, String> nodeToolTipGenerator;
   private final Insets autoscrollInsets = new Insets(20, 20, 20, 20);
   private TreeSelectionSource selectionSource;

   public KongaTree() {
      this(new DefaultKongaTreeModel(new DefaultKongaTreeNode("", true)));
   }

   public KongaTree(KongaTreeModel var1) {
      super(var1);
      this.addTreeSelectionListener(this.selectionSender = new KongaTree.SelectionSender());
      this.commonInitialization();
      if (SystemUtils.IS_OS_WINDOWS_VISTA && UIManager.getLookAndFeel().getClass().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
         this.setUI(new KongaTree.NoLineTreeUI());
      }
   }

   public KongaTreeModel getModel() {
      return (KongaTreeModel)super.getModel();
   }

   private void commonInitialization() {
      this.setLineStyle(KongaTree.LineStyle.ANGLED);
      this.setRowHeight(18);
      this.getSelectionModel().setSelectionMode(2);
      this.treeListenerList = new ArrayList<>();
      this.mouseDragSelector = new MouseDragSelector(this);
      this.mouseClickListener = new KongaTree.MouseClickListener();
      this.addMouseListener(this.mouseClickListener);
      this.mouseRenameListener = new MouseRenameListener(this);
      FocusListener[] var1 = this.getFocusListeners();

      for (int var2 = 0; var2 < var1.length; var2++) {
         if (var1[var2] instanceof BasicTreeUI.FocusHandler) {
            this.removeFocusListener(var1[var2]);
            break;
         }
      }

      this.addFocusListener(new KongaTree.FocusHandler());
      this.expansionListener = new KongaTree.KongaExpansionListener();
      this.addTreeWillExpandListener(this.expansionListener);
      this.renameNodeHandler = new RenameNodeHandler(this);
      this.setToolTipsEnabled(true);
   }

   @Override
   public void setModel(TreeModel var1) {
      if (this.mouseRenameListener != null) {
         this.mouseRenameListener.cancel();
      }

      super.setModel(var1);
   }

   public void setFilter(KongaTreeFilter var1) {
      KongaTree.SelectionMemento var2 = new KongaTree.SelectionMemento();
      Object var3 = this.getExpansionMemento(KongaTree.NodeKeyExtractor.TRIVIAL);
      this.getModel().setFilter(var1);
      this.restoreExpansion(var3);
      var2.restore(var1);
      this.repaint();
      if (var1 instanceof KongaTreeFilter2) {
         ((KongaTreeFilter2)var1).hasBeenApplied(this);
      }
   }

   public final void autoscroll(Point var1) {
      Insets var2 = this.autoscrollInsets;
      Rectangle var3 = this.getVisibleRect();
      Rectangle var4 = new Rectangle(var3.x + var2.left, var3.y + var2.top, var3.width - (var2.left + var2.right), var3.height - (var2.top + var2.bottom));
      if (!var4.contains(var1)) {
         Rectangle var5 = new Rectangle(var1.x - var2.left, var1.y - var2.top, var2.left + var2.right, var2.top + var2.bottom);
         this.scrollRectToVisible(var5);
      }
   }

   public void setToolTipsEnabled(boolean var1) {
      if (var1 != this.tooltipsEnabled) {
         if (var1) {
            ToolTipManager.sharedInstance().registerComponent(this);
         } else {
            ToolTipManager.sharedInstance().unregisterComponent(this);
         }

         this.tooltipsEnabled = var1;
      }
   }

   public boolean isToolTipsEnabled() {
      return this.tooltipsEnabled;
   }

   public void setNodeToolTipGenerator(Function<Object, String> var1) {
      this.nodeToolTipGenerator = var1;
   }

   @Override
   public String getToolTipText(MouseEvent var1) {
      if (this.nodeToolTipGenerator != null) {
         TreePath var2 = this.getPathForLocation(var1.getX(), var1.getY());
         if (var2 != null) {
            return this.nodeToolTipGenerator.apply(var2.getLastPathComponent());
         }
      }

      return super.getToolTipText(var1);
   }

   public void setLineStyle(KongaTree.LineStyle var1) {
      this.putClientProperty("JTree.lineStyle", var1.toString());
   }

   public void setMouseRenameEnabled(boolean var1) {
      if (var1 != this.enableMouseRename) {
         if (var1) {
            this.addMouseListener(this.mouseRenameListener);
         } else {
            this.removeMouseListener(this.mouseRenameListener);
         }
      }

      this.enableMouseRename = var1;
   }

   public boolean isMouseRenameEnabled() {
      return this.enableMouseRename;
   }

   public void setSelectionMode(KongaTree.SelectionMode var1) {
      this.getSelectionModel().setSelectionMode(var1.intValue());
   }

   public void addKongaTreeListener(KongaTreeListener var1) {
      if (var1 != null) {
         this.treeListenerList.add(var1);
      }
   }

   public void removeKongaTreeListener(KongaTreeListener var1) {
      this.treeListenerList.remove(var1);
   }

   public void removeAllKongaTreeListeners() {
      this.treeListenerList.clear();
   }

   public void makeNodeSelected(Object var1, boolean var2) {
      TreePath var3 = this.getPath(var1);
      this.makePathSelected(var3, var2);
      this.scrollPathToVisible(var3);
   }

   void makePathSelected(TreePath var1, boolean var2) {
      if (var1 != null) {
         if (var2) {
            this.getSelectionModel().addSelectionPath(var1);
         } else {
            this.getSelectionModel().setSelectionPath(var1);
         }
      }
   }

   public boolean isNodeSelected(KongaTreeNode var1) {
      return this.getSelectionModel().isPathSelected(this.getPath(var1));
   }

   public void clearSelectionSilently() {
      this.selectionSender.setBlocked(true);
      this.clearSelection();
      this.selectionSender.setBlocked(false);
   }

   public boolean isNodeExpanded(KongaTreeNode var1) {
      return super.isExpanded(this.getPath(var1));
   }

   public void makeNodeExpanded(Object var1) {
      super.expandPath(this.getModel().getPath(var1));
   }

   public List<KongaTreeNode> getSelectedNodes() {
      ArrayList var1 = Lists.newArrayList();
      TreePath[] var2 = this.getSelectionPaths();
      if (var2 != null) {
         for (TreePath var6 : var2) {
            Object var7 = var6.getLastPathComponent();
            if (var7 instanceof KongaTreeNode) {
               var1.add((KongaTreeNode)var7);
            }
         }
      }

      return var1;
   }

   public TreePath getPath(Object var1) {
      return this.getModel().getPath(var1);
   }

   protected void fireTreeSelectionChanged() {
      if (!this.treeListenerList.isEmpty()) {
         List var1 = this.getSelectedNodes();
         KongaTreeEvent var2 = new KongaTreeEvent(this, var1);

         for (KongaTreeListener var4 : this.treeListenerList) {
            var4.selectionChanged(var2);
         }
      }
   }

   protected void fireNodeCollapsing(KongaTreeNode var1) {
      if (var1 != null && !this.treeListenerList.isEmpty()) {
         KongaTreeEvent var2 = new KongaTreeEvent(this, var1);

         for (KongaTreeListener var4 : this.treeListenerList) {
            var4.nodeWillCollapse(var2);
         }
      }
   }

   protected void fireNodeExpanding(KongaTreeNode var1) {
      if (var1 != null && !this.treeListenerList.isEmpty()) {
         KongaTreeEvent var2 = new KongaTreeEvent(this, var1);

         for (KongaTreeListener var4 : this.treeListenerList) {
            var4.nodeWillExpand(var2);
         }
      }
   }

   protected void fireNodePropertyChanged(KongaTreeNode var1) {
      if (var1 != null && !this.treeListenerList.isEmpty()) {
         KongaTreeEvent var2 = new KongaTreeEvent(this, var1);

         for (KongaTreeListener var4 : this.treeListenerList) {
            var4.nodePropertyChanged(var2);
         }
      }
   }

   @Override
   public void showPopupMenu(JPopupMenu var1) {
      if (var1 != null) {
         var1.show(this, this.getPopupX(), this.getPopupY());
      }
   }

   protected int getPopupX() {
      return this.mouseClickListener.getX();
   }

   protected int getPopupY() {
      return this.mouseClickListener.getY();
   }

   protected void onPopupTriggered() {
      int var1 = this.getPopupX();
      int var2 = this.getPopupY();
      TreePath var3 = this.getPathForLocation(var1, var2);
      if (var3 != null) {
         if (this.getSelectionCount() < 2) {
            this.invokePopupMenu(var3, true);
         } else if ((this.mouseClickListener.getModifiers() & 64) != 0) {
            this.invokePopupMenu(var3, true);
         } else if ((this.mouseClickListener.getModifiers() & 128) != 0) {
            this.invokePopupMenu(var3, false);
         } else {
            KongaTreeNode var4 = (KongaTreeNode)var3.getLastPathComponent();
            if (var4 == null) {
               return;
            }

            if (this.isNodeSelected(var4)) {
               this.invokePopupMenu(var3, false);
            } else {
               this.invokePopupMenu(var3, true);
            }
         }
      }
   }

   private void invokePopupMenu(TreePath var1, boolean var2) {
      if (var2) {
         this.setSelectionPath(var1);
      }

      this.scrollPathToVisible(var1);
   }

   public void onRenameNode(KongaTreeNode var1) {
      this.renameNodeHandler.onRenameNodeRequested(var1);
   }

   public void setRenameNodeStrategy(RenameNodeStrategy var1) {
      this.renameNodeHandler.setRenameNodeStrategy(var1);
   }

   public void renameSelectedNode() {
      List var1 = this.getSelectedNodes();
      if (var1.size() == 1) {
         this.onRenameNode((KongaTreeNode)var1.get(0));
      }
   }

   public void setHorizontalScrollbarPolicy(int var1) {
      this.scrollableTracksViewportWidth = var1 == 31;
   }

   @Override
   public boolean getScrollableTracksViewportWidth() {
      return this.scrollableTracksViewportWidth ? true : super.getScrollableTracksViewportWidth();
   }

   public void expandAll() {
      this.expandCollapseAllImpl(new TreePath(this.getModel().getRoot()), true);
   }

   public final void expandAllUnder(KongaTreeNode var1) {
      this.expandCollapseAllImpl(this.getPath(var1), true);
   }

   public void collapseAll() {
      this.expandCollapseAllImpl(new TreePath(this.getModel().getRoot()), false);
   }

   private void expandCollapseAllImpl(TreePath var1, boolean var2) {
      KongaTreeNode var3 = (KongaTreeNode)var1.getLastPathComponent();
      int var4 = this.getModel().getChildCount(var3);

      for (int var5 = 0; var5 < var4; var5++) {
         KongaTreeNode var6 = (KongaTreeNode)this.getModel().getChild(var3, var5);
         if (var6.getAllowsChildren()) {
            TreePath var7 = var1.pathByAddingChild(var6);
            this.expandCollapseAllImpl(var7, var2);
         }
      }

      if (var2) {
         this.expandPath(var1);
      } else if (var1.getLastPathComponent() != this.getModel().getRoot()) {
         this.collapsePath(var1);
      }
   }

   @Override
   public UiProvider asUiProvider() {
      return new UiProviderAdapter(this);
   }

   @Override
   public void setPainter(ComponentPainter var1) {
      boolean var2 = var1 != this.painter;
      this.painter = var1;
      if (var2) {
         this.repaint();
      }
   }

   @Override
   protected void paintComponent(Graphics var1) {
      if (this.painter == null) {
         super.paintComponent(var1);
      } else {
         this.applyPainter((Graphics2D)var1);
      }
   }

   private void applyPainter(Graphics2D var1) {
      assert this.painter != null;
      if (!this.painter.preRendering(this, var1)) {
         super.paintComponent(var1);
         this.painter.postRendering(this, var1);
      }
   }

   @Override
   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      return this.painter != null ? this.painter.getPreferredSize(var1) : var1;
   }

   public void setDragSelectionEnabled(boolean var1) {
      this.mouseDragSelector.setEnabled(var1);
   }

   public void setNewModelWithExpansionIntact(KongaTreeModel var1, KongaTree.NodeKeyExtractor var2) {
      if (var1 != this.getModel()) {
         Object var3 = this.getExpansionMemento(var2);
         this.setModel(var1);
         this.restoreExpansion(var3);
      }
   }

   public Object getExpansionMemento(KongaTree.NodeKeyExtractor var1) {
      return new KongaTree.ExpansionMemento(this, var1);
   }

   public void restoreExpansion(Object var1) {
      if (var1 instanceof KongaTree.ExpansionMemento) {
         ((KongaTree.ExpansionMemento)var1).restore();
      } else {
         throw new IllegalArgumentException("Invalid memento: " + var1);
      }
   }

   public void expandAllNonEmptyFolderNodes() {
      KongaTreeNode var1 = this.getModel().getRoot();
      this.expandAllNonEmptyFolderNodes(var1);
   }

   private void expandAllNonEmptyFolderNodes(Object var1) {
      KongaTreeModel var2 = this.getModel();
      int var3 = var2.getChildCount(var1);
      if (var3 != 0) {
         this.makeNodeExpanded(var1);

         for (int var4 = 0; var4 < var3; var4++) {
            this.expandAllNonEmptyFolderNodes(var2.getChild(var1, var4));
         }
      }
   }

   public final void selectNextRow() {
      if (this.getRowCount() != 0) {
         int var1 = -1;
         switch (this.getSelectionCount()) {
            case 0:
               var1 = 0;
               break;
            case 1:
               int var2 = this.getSelectionRows()[0];
               var1 = var2 + 1;
               break;
            default:
               return;
         }

         if (var1 >= this.getRowCount()) {
            var1 = 0;
         }

         this.setSelectionRow(var1);
      }
   }

   public final void selectLastRow() {
      int var1 = this.getRowCount() - 1;
      if (var1 >= 0) {
         this.setSelectionRow(var1);
      }
   }

   public final void selectFirstRow() {
      if (this.getRowCount() > 0) {
         this.setSelectionRow(0);
      }
   }

   public final void scrollToFirstRow() {
      if (this.getRowCount() > 0) {
         this.scrollRowToVisible(0);
      }
   }

   public final void scrollNodeToVerticalCenter(Object var1) {
      TreePath var2 = this.getPath(var1);
      int var3 = this.getRowForPath(var2);
      if (var3 != -1) {
         int var4 = Math.min(this.getRowCount() - 1, var3 + this.getVisibleRowCount() / 2);
         this.scrollRowToVisible(var4);
      }
   }

   public final TreeSelectionSource asSelectionSource() {
      if (this.selectionSource == null) {
         this.selectionSource = new TreeSelectionSource(this);
      }

      return this.selectionSource;
   }

   public final boolean isPartOfTree(KongaTreeNode var1) {
      KongaTreeNode var2 = this.getModel().getRoot();

      for (KongaTreeNode var3 = var1; var3 != null; var3 = var3.getParent()) {
         if (var3 == var2) {
            return true;
         }
      }

      return false;
   }

   public static class DefaultNodeKeyExtractor implements KongaTree.NodeKeyExtractor {
      private final KongaTree tree;

      public DefaultNodeKeyExtractor(KongaTree var1) {
         this.tree = var1;
      }

      @Override
      public Object getKey(Object var1) {
         return this.tree.getModel().getPath(var1).toString();
      }
   }

   private static final class ExpansionMemento {
      private final KongaTree tree;
      private final KongaTree.NodeKeyExtractor keyExtractor;
      private final Set<Object> expandedNodes;

      public ExpansionMemento(KongaTree var1, KongaTree.NodeKeyExtractor var2) {
         this.tree = var1;
         this.keyExtractor = var2 != null ? var2 : KongaTree.NodeKeyExtractor.TRIVIAL;
         this.expandedNodes = new HashSet<>();
         this.build();
      }

      public void restore() {
         this.tree.collapseAll();

         for (Object var2 : new BreadthFirstTraverser(this.tree.getModel())) {
            Object var3 = this.keyExtractor.getKey(var2);
            if (this.expandedNodes.contains(var3)) {
               this.tree.makeNodeExpanded(var2);
            }
         }
      }

      private void build() {
         KongaTreeModel var1 = this.tree.getModel();

         for (Object var3 : new BreadthFirstTraverser(var1)) {
            if (!var1.isLeaf(var3) && this.tree.isExpanded(var1.getPath(var3))) {
               this.expandedNodes.add(this.keyExtractor.getKey(var3));
            }
         }
      }
   }

   private final class FocusHandler implements FocusListener {
      private FocusHandler() {
      }

      @Override
      public void focusGained(FocusEvent var1) {
         KongaTree.this.repaint();
      }

      @Override
      public void focusLost(FocusEvent var1) {
         KongaTree.this.mouseRenameListener.cancel();
         KongaTree.this.repaint();
      }
   }

   private final class KongaExpansionListener implements TreeWillExpandListener {
      private KongaExpansionListener() {
      }

      @Override
      public void treeWillCollapse(TreeExpansionEvent var1) {
         TreePath var2 = var1.getPath();
         if (var2 != null) {
            Object var3 = var2.getLastPathComponent();
            if (var3 instanceof KongaTreeNode) {
               KongaTree.this.fireNodeCollapsing((KongaTreeNode)var3);
            }
         }
      }

      @Override
      public void treeWillExpand(TreeExpansionEvent var1) {
         TreePath var2 = var1.getPath();
         if (var2 != null) {
            Object var3 = var2.getLastPathComponent();
            if (var3 instanceof KongaTreeNode) {
               KongaTree.this.fireNodeExpanding((KongaTreeNode)var3);
            }
         }
      }
   }

   public static final class LineStyle {
      public static final KongaTree.LineStyle NONE = new KongaTree.LineStyle(UiResourceBundle.getString("KongaTree.LineStyle.None"));
      public static final KongaTree.LineStyle ANGLED = new KongaTree.LineStyle(UiResourceBundle.getString("KongaTree.LineStyle.Angled"));
      public static final KongaTree.LineStyle HORIZONTAL = new KongaTree.LineStyle(UiResourceBundle.getString("KongaTree.LineStyle.Horizontal"));
      private final String name;

      private LineStyle(String var1) {
         this.name = var1;
      }

      @Override
      public String toString() {
         return this.name;
      }
   }

   private final class MouseClickListener extends MouseActionListener {
      private int x;
      private int y;
      private int modifiers;

      private MouseClickListener() {
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         this.x = var1.getX();
         this.y = var1.getY();
         this.modifiers = var1.getModifiersEx();
         KongaTree.this.onPopupTriggered();
      }

      public int getModifiers() {
         return this.modifiers;
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }
   }

   private static final class NoLineTreeUI extends WindowsTreeUI {
      private NoLineTreeUI() {
      }

      @Override
      protected void paintHorizontalLine(Graphics var1, JComponent var2, int var3, int var4, int var5) {
      }

      @Override
      protected void paintVerticalLine(Graphics var1, JComponent var2, int var3, int var4, int var5) {
      }
   }

   public interface NodeKeyExtractor {
      KongaTree.NodeKeyExtractor TRIVIAL = new KongaTree.NodeKeyExtractor() {
         @Override
         public Object getKey(Object var1) {
            return var1;
         }
      };

      Object getKey(Object var1);
   }

   private final class SelectionMemento {
      private final List<KongaTreeNode> nodes = KongaTree.this.getSelectedNodes();

      public SelectionMemento() {
      }

      public void restore(KongaTreeFilter var1) {
         if (var1 != null) {
            Iterables.removeIf(this.nodes, Predicates.not(var1));
         }

         this.setSelectedNodes();
      }

      private void setSelectedNodes() {
         ArrayList var1 = Lists.newArrayList();

         for (KongaTreeNode var3 : this.nodes) {
            var1.add(KongaTree.this.getModel().getPath(var3));
         }

         KongaTree.this.setSelectionPaths((TreePath[]) var1.toArray(new TreePath[var1.size()]));
      }
   }

   public static final class SelectionMode {
      public static final KongaTree.SelectionMode CONTIGUOUS_TREE_SELECTION = new KongaTree.SelectionMode(
         2, UiResourceBundle.getString("KongaTree.SelectionMode.Contiguous")
      );
      public static final KongaTree.SelectionMode DISCONTIGUOUS_TREE_SELECTION = new KongaTree.SelectionMode(
         4, UiResourceBundle.getString("KongaTree.SelectionMode.Discontiguous")
      );
      public static final KongaTree.SelectionMode SINGLE_TREE_SELECTION = new KongaTree.SelectionMode(
         1, UiResourceBundle.getString("KongaTree.SelectionMode.Single")
      );
      private final int intValue;
      private final String stringValue;

      private SelectionMode(int var1, String var2) {
         this.intValue = var1;
         this.stringValue = var2;
      }

      private int intValue() {
         return this.intValue;
      }

      @Override
      public String toString() {
         return this.stringValue;
      }
   }

   private final class SelectionSender implements TreeSelectionListener {
      private boolean blocked;

      private SelectionSender() {
      }

      @Override
      public void valueChanged(TreeSelectionEvent var1) {
         if (!this.blocked) {
            KongaTree.this.fireTreeSelectionChanged();
         }
      }

      public void setBlocked(boolean var1) {
         this.blocked = var1;
      }
   }
}
