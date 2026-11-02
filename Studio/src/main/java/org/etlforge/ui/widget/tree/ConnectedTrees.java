package org.etlforge.ui.widget.tree;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.etlforge.collections.Collections2;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.collections.Pair;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionFactory;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.event.ChangeListeners;
import org.etlforge.ui.graphics.BezierCurve;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionListeners;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.DefaultActionHandler;
import org.etlforge.ui.widget.label.KongaLabel;

public final class ConnectedTrees implements UiProvider, SelectionSource {
   private final ConnectedTrees.Tree leftTree;
   private final ConnectedTrees.Tree rightTree;
   private final JComponent center = new ConnectedTrees.Center();
   private JComponent ui;
   private final ConnectedTrees.Mode mode;
   private final ConnectedTrees.SelectionDispatcher selectionDispatcher = new ConnectedTrees.SelectionDispatcher();
   private final ChangeListeners changeListeners = new ChangeListeners();

   public ConnectedTrees(KongaTreeModel var1, KongaTreeModel var2, ConnectedTrees.Mode var3) {
      this.mode = Preconditions.checkNotNull(var3);
      this.leftTree = new ConnectedTrees.Tree(var1);
      this.rightTree = new ConnectedTrees.Tree(var2);
      this.installActions();
      this.installDragAndDrop();
      this.listenToTrees();
   }

   private void installActions() {
      Action var1 = ActionFactory.forKey(KeyUtils.ENTER, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ConnectedTrees.this.connectSelectedRows();
         }
      });
      ConnectedTrees.DeleteAction var2 = new ConnectedTrees.DeleteAction(this.leftTree, this.rightTree);
      ConnectedTrees.DeleteAction var3 = new ConnectedTrees.DeleteAction(this.rightTree, this.leftTree);
      this.leftTree.installActions(var1, var2);
      this.rightTree.installActions(var1, var3);
   }

   private void installDragAndDrop() {
      this.leftTree.installTransferHandler(new ConnectedTrees.LeftTreeTransferHandler(this.leftTree));
      this.rightTree.installTransferHandler(new ConnectedTrees.RightTreeTransferHandler(this.rightTree));
   }

   private void listenToTrees() {
      ConnectedTrees.TreeListener var1 = new ConnectedTrees.TreeListener();
      this.leftTree.addListener(var1);
      this.rightTree.addListener(var1);
   }

   public void setSelectionMode(KongaTree.SelectionMode var1) {
      this.leftTree.tree.setSelectionMode(var1);
      this.rightTree.tree.setSelectionMode(var1);
   }

   public List<Pair<Object, Object>> getConnections() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.leftTree.connections()) {
         var1.add(Pair.of(var3));
      }

      return var1;
   }

   public int getConnectionCount() {
      return this.leftTree.getConnectionCount();
   }

   public boolean areConnected(Object var1, Object var2) {
      return this.leftTree.areConnected(var1, var2);
   }

   public boolean isLeftObjectConnected(Object var1) {
      return this.leftTree.isConnected(var1);
   }

   public boolean isRightObjectConnected(Object var1) {
      return this.rightTree.isConnected(var1);
   }

   public boolean canConnect(Object var1, Object var2) {
      boolean var3 = this.leftTree.isConnectable(var1) && this.rightTree.isConnectable(var2);
      if (!var3) {
         return false;
      }

      switch (this.mode) {
         case ONE_TO_ONE:
            return !this.leftTree.isConnected(var1) && !this.rightTree.isConnected(var2);
         case ONE_TO_MANY:
            return !this.rightTree.isConnected(var2);
         case MANY_TO_ONE:
            return !this.leftTree.isConnected(var1);
         case MANY_TO_MANY:
            return true;
         default:
            throw new RuntimeException("Unexpected mode: " + this.mode);
      }
   }

   public void addConnection(Object var1, Object var2) {
      if (this.canConnect(var1, var2)) {
         this.leftTree.addConnection(var1, var2);
         this.rightTree.addConnection(var2, var1);
         this.onConectionChange();
      }
   }

   private void onConectionChange() {
      this.selectionDispatcher.startBulkChange();
      this.leftTree.clearSelection();
      this.rightTree.clearSelection();
      this.selectionDispatcher.endBulkChange();
      this.fireChange();
      this.repaint();
   }

   private void fireChange() {
      this.changeListeners.call().stateChanged(new ChangeEvent(this));
   }

   private void repaint() {
      if (this.ui != null) {
         this.ui.repaint();
      }
   }

   public void removeConnection(Object var1, Object var2) {
      if (this.leftTree.removeConnection(var1, var2)) {
         this.rightTree.removeConnection(var2, var1);
         this.onConectionChange();
      }
   }

   public void removeAllConnections() {
      boolean var1 = this.leftTree.removeAllConnections();
      boolean var2 = this.rightTree.removeAllConnections();
      if (var1 || var2) {
         this.onConectionChange();
      }
   }

   public void replaceModels(KongaTreeModel var1, KongaTreeModel var2) {
      this.leftTree.setTreeModel(var1);
      this.rightTree.setTreeModel(var2);
      this.fireChange();
      this.repaint();
   }

   public void setCellRenderers(TreeCellRenderer var1, TreeCellRenderer var2) {
      if (var1 != null) {
         this.leftTree.setRenderer(var1);
      }

      if (var2 != null) {
         this.rightTree.setRenderer(var2);
      }

      this.repaint();
   }

   public void setLeftLabel(String var1) {
      this.leftTree.setLabel(var1);
   }

   public void setRightLabel(String var1) {
      this.rightTree.setLabel(var1);
   }

   public void setLabels(String var1, String var2) {
      this.setLeftLabel(var1);
      this.setRightLabel(var2);
   }

   public void setLabelStyle(TextStyle var1) {
      this.leftTree.setLabelStyle(var1);
      this.rightTree.setLabelStyle(var1);
   }

   public void addLeftButtons(JButton... var1) {
      this.leftTree.addButtons(var1);
   }

   public void addRightButtons(JButton... var1) {
      this.rightTree.addButtons(var1);
   }

   public void installFilterFields(Function<String, KongaTreeFilter> var1, Function<String, KongaTreeFilter> var2) {
      this.leftTree.setFilterFunction(var1);
      this.rightTree.setFilterFunction(var2);
   }

   public void setShowRootHandles(boolean var1, boolean var2) {
      this.leftTree.setShowRootHandles(var1);
      this.rightTree.setShowRootHandles(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      ConnectedTrees.Layout var2 = new ConnectedTrees.Layout();
      this.ui = var2.getUi();
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.leftTree.tree.requestFocusInWindow();
   }

   public ConnectedTreesSelection getSelection() {
      return this.selectionDispatcher.getSelection();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.addListener(var1);
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.selectionDispatcher.removeListener(var1);
   }

   public void addChangeListener(ChangeListener var1) {
      this.changeListeners.add(var1);
   }

   public void removeChangeListener(ChangeListener var1) {
      this.changeListeners.remove(var1);
   }

   private void connectSelectedRows() {
      for (Object var2 : this.leftTree.getSelectedObjects()) {
         for (Object var4 : this.rightTree.getSelectedObjects()) {
            if (this.canConnect(var2, var4)) {
               this.addConnection(var2, var4);
            }
         }
      }
   }

   private static class Caption {
      private final KongaTree tree;
      private KongaLabel label;
      private TextStyle style;
      private final List<JButton> buttons = Lists.newArrayList();

      public Caption(KongaTree var1) {
         this.tree = var1;
      }

      public void setLabel(String var1) {
         if (this.label == null) {
            this.label = KongaLabel.withMnemonic(var1);
            if (this.style != null) {
               this.style.makeOver(this.label);
            }

            this.label.setLabelForTree(this.tree);
         } else {
            this.label.setMnemonicText(var1);
         }
      }

      public void setLabelStyle(TextStyle var1) {
         if (this.label != null) {
            var1.makeOver(this.label);
         } else {
            this.style = var1;
         }
      }

      public void addButtons(JButton... var1) {
         KongaListUtils.add(this.buttons, var1);
      }

      public boolean isEmpty() {
         return this.label == null && this.buttons.isEmpty();
      }

      public JComponent layout() {
         BoxBuilder var1 = BoxBuilder.horizontal();
         var1.add(this.label == null ? " " : this.label).glue();
         if (!this.buttons.isEmpty()) {
            var1.rigid(5).addAllWithSpace(5, this.buttons);
         }

         return var1.container();
      }
   }

   private class Center extends JComponent {
      public Center() {
         this.setPreferredSize(new Dimension(64, 32));
         this.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport var1) {
               return var1.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
         });
      }

      @Override
      public void paint(Graphics var1) {
         super.paint(var1);
         Graphics2D var2 = (Graphics2D)var1;
         Color var3 = var2.getColor();
         var2.setColor(Color.BLACK);
         RenderingHints var4 = var2.getRenderingHints();
         UiUtils.makeAntiAliased(var2);
         Stroke var5 = var2.getStroke();
         HashMap var6 = Maps.newHashMap();

         for (Entry var8 : ConnectedTrees.this.leftTree.connections()) {
            this.connect(var8, var2, var6);
         }

         var2.setStroke(var5);
         var2.setRenderingHints(var4);
         var2.setColor(var3);
      }

      private void connect(Entry<Object, Object> var1, Graphics2D var2, Map<Integer, Integer> var3) {
         int var4 = ConnectedTrees.this.leftTree.getY(var1.getKey());
         if (var4 != -1) {
            int var5 = ConnectedTrees.this.rightTree.getY(var1.getValue());
            if (var5 != -1) {
               if (!var3.containsKey(var4) || (Integer)var3.get(var4) != var5) {
                  int var6 = this.getLineWidth(var1.getKey(), var1.getValue());
                  BasicStroke var7 = new BasicStroke(var6);
                  var2.setStroke(var7);
                  this.drawLine(var2, var4, var5);
                  var3.put(var4, var5);
               }
            }
         }
      }

      private int getLineWidth(Object var1, Object var2) {
         return 1;
      }

      private void drawLine(Graphics2D var1, int var2, int var3) {
         BezierCurve var4 = new BezierCurve(new int[]{0, this.getWidth() / 2, this.getWidth() / 2, this.getWidth()}, new int[]{var2, var2, var3, var3});
         var4.paint(var1);
      }
   }

   public static final class Connection {
      private final ConnectedTrees trees;
      private final Object left;
      private final Object right;

      public Connection(ConnectedTrees var1, Object var2, Object var3) {
         this.trees = var1;
         this.left = var2;
         this.right = var3;
      }

      public void make() {
         this.trees.addConnection(this.left, this.right);
      }

      public void breakUp() {
         this.trees.removeConnection(this.left, this.right);
      }
   }

   private class DeleteAction extends AbstractAction {
      private final ConnectedTrees.Tree from;
      private final ConnectedTrees.Tree to;

      public DeleteAction(ConnectedTrees.Tree var2, ConnectedTrees.Tree var3) {
         this.from = var2;
         this.to = var3;
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      public final void actionPerformed(ActionEvent var1) {
         List var2 = this.from.getSelectedObjects();
         if (!var2.isEmpty()) {
            for (Object var4 : var2) {
               this.handleDelete(var4);
            }

            ConnectedTrees.this.onConectionChange();
         }
      }

      private void handleDelete(Object var1) {
         Collection var2 = this.from.removeAllConnections(var1);
         if (var2 != null) {
            for (Object var4 : var2) {
               this.to.removeConnection(var4, var1);
            }
         }
      }
   }

   private abstract class DragAndDropBase extends TransferHandler {
      private final ConnectedTrees.Tree tree;
      private List<ConnectedTrees.Connection> connections;

      public DragAndDropBase(ConnectedTrees.Tree var2) {
         this.tree = var2;
      }

      @Override
      public final int getSourceActions(JComponent var1) {
         return 1073741825;
      }

      @Override
      public final boolean canImport(TransferSupport var1) {
         if (var1.isDataFlavorSupported(ConnectedTrees.DraggedNodes.FLAVOR)) {
            try {
               Transferable var2 = var1.getTransferable();
               ConnectedTrees.DraggedNodes var3 = (ConnectedTrees.DraggedNodes)var2.getTransferData(ConnectedTrees.DraggedNodes.FLAVOR);
               if (var3.isSameSource(this.tree)) {
                  return false;
               }

               ImmutableList var4 = var3.getDraggedObjects();
               if (var4.size() > 1 && !this.canConnectMultipleObjects()) {
                  return false;
               }

               JTree.DropLocation var5 = (JTree.DropLocation)var1.getDropLocation();
               TreePath var6 = var5.getPath();
               if (var6 == null) {
                  return false;
               }

               KongaTreeNode var7 = (KongaTreeNode)var6.getLastPathComponent();
               Object var8 = var7.getUserObject();
               if (!this.tree.isConnectable(var8)) {
                  return false;
               }

               if (this.tree.isConnected(var8) && !this.isAvailableAsDropTarget(var8)) {
                  return false;
               }

               this.connections = this.getAvailableConnections(var8, var4);
               return !this.connections.isEmpty();
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }

         return false;
      }

      protected abstract boolean canConnectMultipleObjects();

      protected abstract boolean isAvailableAsDropTarget(Object var1);

      protected abstract List<ConnectedTrees.Connection> getAvailableConnections(Object var1, ImmutableList<Object> var2);

      @Override
      protected final Transferable createTransferable(JComponent var1) {
         KongaTree var2 = (KongaTree)var1;
         List var3 = var2.getSelectedNodes();
         return var3.isEmpty() ? null : new ConnectedTrees.DraggedNodes(this.tree, var3);
      }

      @Override
      public final boolean importData(TransferSupport var1) {
         if (var1.isDataFlavorSupported(ConnectedTrees.DraggedNodes.FLAVOR)) {
            try {
               for (ConnectedTrees.Connection var3 : this.connections) {
                  var3.make();
               }

               this.connections = null;
               return true;
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }

         return false;
      }
   }

   private static class DraggedNodes implements Transferable {
      public static final DataFlavor FLAVOR = createFlavor();
      private final Object source;
      private final ImmutableList<Object> objects;
      private final String stringRep;

      private static DataFlavor createFlavor() {
         try {
            String var0 = "application/x-java-jvm-local-objectref;class=" + ConnectedTrees.DraggedNodes.class.getName();
            return new DataFlavor(var0);
         } catch (ClassNotFoundException var1) {
            throw new RuntimeException();
         }
      }

      public DraggedNodes(Object var1, List<KongaTreeNode> var2) {
         this.source = var1;
         this.objects = FluentIterable.from(var2).<Object>transform(NodeFunctions.toUserObject()).toImmutableList();
         this.stringRep = Collections2.join(var2, Functions.toStringFunction(), "\n");
      }

      public boolean isSameSource(Object var1) {
         return var1 == this.source;
      }

      public ImmutableList<Object> getDraggedObjects() {
         return this.objects;
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return this.stringRep == null ? new DataFlavor[]{FLAVOR} : new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor var1) {
         return var1 == FLAVOR ? true : var1 == DataFlavor.stringFlavor && this.stringRep != null;
      }

      @Override
      public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException {
         if (var1 == FLAVOR) {
            return this;
         } else if (var1 == DataFlavor.stringFlavor && this.stringRep != null) {
            return this.stringRep;
         } else {
            throw new UnsupportedFlavorException(var1);
         }
      }
   }

   private class Layout {
      private final NColumnPanel grid = new NColumnPanel(3);

      public Layout() {
         this.grid.setXWeights(0.4, 0.2, 0.4);
         this.layoutCaptions();
         this.layoutLists();
         this.layoutFilterFields();
      }

      private void layoutCaptions() {
         if (!ConnectedTrees.this.leftTree.caption.isEmpty() || !ConnectedTrees.this.rightTree.caption.isEmpty()) {
            JComponent var1 = ConnectedTrees.this.leftTree.caption.layout();
            JComponent var2 = ConnectedTrees.this.rightTree.caption.layout();
            this.makeSameWidth(var1, var2);
            this.grid.setFills(GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
            this.grid.setInsets(0, 0, 2, 0);
            this.grid.addRow(var1, null, var2);
         }
      }

      private void makeSameWidth(JComponent var1, JComponent var2) {
         Dimension var3 = var1.getPreferredSize();
         Dimension var4 = var2.getPreferredSize();
         int var5 = Math.max(var3.width, var4.width);
         var3.width = var5;
         var1.setPreferredSize(var3);
         var4.width = var5;
         var2.setPreferredSize(var4);
      }

      private void layoutLists() {
         this.makeSameWidth(ConnectedTrees.this.leftTree.scroll, ConnectedTrees.this.rightTree.scroll);
         this.grid.setInsets(0, 0, 0, 0);
         this.grid.setYWeights(1.0, 1.0, 1.0);
         this.grid.setFills(GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.BOTH);
         this.grid.addRow(ConnectedTrees.this.leftTree.scroll, ConnectedTrees.this.center, ConnectedTrees.this.rightTree.scroll);
      }

      private void layoutFilterFields() {
         UiProvider var1 = ConnectedTrees.this.leftTree.getFilterField();
         UiProvider var2 = ConnectedTrees.this.rightTree.getFilterField();
         if (var1 != null || var2 != null) {
            this.grid.setYWeights(0.0, 0.0, 0.0);
            this.grid.setFills(GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL);
            this.grid.addRow(var1, null, var2);
         }
      }

      public JComponent getUi() {
         return this.grid.container();
      }
   }

   private class LeftTreeTransferHandler extends ConnectedTrees.DragAndDropBase {
      public LeftTreeTransferHandler(ConnectedTrees.Tree var2) {
         super(var2);
      }

      @Override
      protected boolean isAvailableAsDropTarget(Object var1) {
         return ConnectedTrees.this.mode == ConnectedTrees.Mode.MANY_TO_MANY || ConnectedTrees.this.mode == ConnectedTrees.Mode.MANY_TO_ONE;
      }

      @Override
      protected boolean canConnectMultipleObjects() {
         return ConnectedTrees.this.mode == ConnectedTrees.Mode.ONE_TO_MANY || ConnectedTrees.this.mode == ConnectedTrees.Mode.MANY_TO_MANY;
      }

      @Override
      protected List<ConnectedTrees.Connection> getAvailableConnections(Object var1, ImmutableList<Object> var2) {
         ArrayList var3 = Lists.newArrayList();

         for (Object var5 : var2) {
            if (ConnectedTrees.this.canConnect(var1, var5)) {
               var3.add(new ConnectedTrees.Connection(ConnectedTrees.this, var1, var5));
            }
         }

         return var3;
      }
   }

   public enum Mode {
      ONE_TO_ONE,
      ONE_TO_MANY,
      MANY_TO_ONE,
      MANY_TO_MANY;
   }

   private class RightTreeTransferHandler extends ConnectedTrees.DragAndDropBase {
      public RightTreeTransferHandler(ConnectedTrees.Tree var2) {
         super(var2);
      }

      @Override
      protected boolean isAvailableAsDropTarget(Object var1) {
         return ConnectedTrees.this.mode == ConnectedTrees.Mode.ONE_TO_MANY || ConnectedTrees.this.mode == ConnectedTrees.Mode.MANY_TO_MANY;
      }

      @Override
      protected boolean canConnectMultipleObjects() {
         return ConnectedTrees.this.mode == ConnectedTrees.Mode.MANY_TO_MANY || ConnectedTrees.this.mode == ConnectedTrees.Mode.MANY_TO_ONE;
      }

      @Override
      protected List<ConnectedTrees.Connection> getAvailableConnections(Object var1, ImmutableList<Object> var2) {
         ArrayList var3 = Lists.newArrayList();

         for (Object var5 : var2) {
            if (ConnectedTrees.this.canConnect(var5, var1)) {
               var3.add(new ConnectedTrees.Connection(ConnectedTrees.this, var5, var1));
            }
         }

         return var3;
      }
   }

   private static class SelectedPathDecorator implements ComponentPainter {
      private final ConnectedTrees.Tree tree;
      private final Color background = Color.LIGHT_GRAY;

      public SelectedPathDecorator(ConnectedTrees.Tree var1) {
         this.tree = var1;
      }

      @Override
      public void postRendering(Component var1, Graphics2D var2) {
         Color var3 = var2.getColor();
         var2.setColor(this.background);
         JTree var4 = (JTree)var1;
         int var5 = var4.getWidth() - 1;
         int var6 = 0;

         for (int var7 = var4.getRowCount(); var6 < var7; var6++) {
            TreePath var8 = var4.getPathForRow(var6);
            KongaTreeNode var9 = (KongaTreeNode)var8.getLastPathComponent();
            Object var10 = var9.getUserObject();
            boolean var11 = this.tree.isConnected(var10);
            if (var11) {
               Rectangle var12 = var4.getRowBounds(var6);
               var2.drawRect(0, var12.y, var5, var12.height);
            }
         }

         var2.setColor(var3);
      }

      @Override
      public boolean preRendering(Component var1, Graphics2D var2) {
         return false;
      }

      @Override
      public Dimension getPreferredSize(Dimension var1) {
         return var1;
      }
   }

   private final class SelectionDispatcher {
      private final SelectionListeners selectionListeners = new SelectionListeners();
      private boolean blocked;

      private SelectionDispatcher() {
      }

      public void startBulkChange() {
         this.blocked = true;
      }

      public void endBulkChange() {
         this.blocked = false;
         this.dispatchSelection();
      }

      public void addListener(SelectionListener var1) {
         this.selectionListeners.add(var1);
      }

      public void removeListener(SelectionListener var1) {
         this.selectionListeners.remove(var1);
      }

      public void dispatchSelection() {
         if (!this.blocked) {
            ConnectedTreesSelection var1 = this.getSelection();
            this.selectionListeners.call().selectionChanged(new SelectionChangedEvent(ConnectedTrees.this, var1));
         }
      }

      public ConnectedTreesSelection getSelection() {
         List var1 = ConnectedTrees.this.leftTree.getSelectedObjects();
         List var2 = ConnectedTrees.this.rightTree.getSelectedObjects();
         return new ConnectedTreesSelection(ConnectedTrees.this, var1, var2);
      }
   }

   private static class Tree {
      public final KongaTree tree;
      public final JScrollPane scroll;
      public final ConnectedTrees.Caption caption;
      private Function<String, KongaTreeFilter> filterFunction;
      private Map<Object, TreePath> objectToPath;
      private Multimap<Object, Object> connections = HashMultimap.create();

      public Tree(KongaTreeModel var1) {
         this.tree = new KongaTree(var1);
         this.tree.setRootVisible(false);
         this.tree.setVisibleRowCount(4);
         this.tree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Icon getLeafIcon() {
               return null;
            }
         });
         this.tree.setPainter(new ConnectedTrees.SelectedPathDecorator(this));
         boolean var2 = TreeUtils.hasFolders(var1);
         this.updateTreeAppearanceBasedOnModelProperties(var2);
         this.scroll = new JScrollPane(this.tree);
         this.caption = new ConnectedTrees.Caption(this.tree);
         this.storePaths(var1);
      }

      private void updateTreeAppearanceBasedOnModelProperties(boolean var1) {
         this.tree.setBorder(Empty.border(0, var1 ? 0 : 6, 0, 0));
         this.tree.setShowsRootHandles(var1);
         this.tree.expandAll();
      }

      private void storePaths(KongaTreeModel var1) {
         this.objectToPath = Maps.newHashMap();

         for (KongaTreeNode var3 : var1.getRoot().depthFirstEnumeration()) {
            TreePath var4 = var1.getPath(var3);
            this.objectToPath.put(var3.getUserObject(), var4);
         }
      }

      public void setTreeModel(KongaTreeModel var1) {
         KongaTreeModel var2 = this.tree.getModel();
         KongaTreeFilter var3 = var2.getFilter();
         if (var3 instanceof MemoizingKongaTreeFilter) {
            ((MemoizingKongaTreeFilter)var3).forgetState();
         }

         this.storePaths(var1);
         this.connections = HashMultimap.create();
         boolean var4 = TreeUtils.hasFolders(var1);
         var1.setFilter(var3);
         this.tree.setModel(var1);
         this.updateTreeAppearanceBasedOnModelProperties(var4);
      }

      public void setShowRootHandles(boolean var1) {
         this.tree.setShowsRootHandles(var1);
      }

      public void setRenderer(TreeCellRenderer var1) {
         this.tree.setCellRenderer(var1);
      }

      public void setLabel(String var1) {
         this.caption.setLabel(var1);
      }

      public void setLabelStyle(TextStyle var1) {
         this.caption.setLabelStyle(var1);
      }

      public void addButtons(JButton... var1) {
         this.caption.addButtons(var1);
      }

      public void setFilterFunction(Function<String, KongaTreeFilter> var1) {
         this.filterFunction = var1;
      }

      public UiProvider getFilterField() {
         return this.filterFunction == null ? null : this.createFilterField(this.tree, this.filterFunction);
      }

      private TreeFilterField createFilterField(KongaTree var1, Function<String, KongaTreeFilter> var2) {
         SimpleTreeFilterField var3 = new SimpleTreeFilterField(var1, var2);
         var3.setTextWhenEmpty("Search");
         return var3;
      }

      public void installActions(Action var1, Action... var2) {
         DefaultActionHandler var3 = new DefaultActionHandler(this.tree, var1);
         var3.install();

         for (Action var7 : var2) {
            ActionKeyBinding.install(var7, this.tree);
         }
      }

      public boolean isConnectable(Object var1) {
         TreePath var2 = this.getPath(var1);
         if (var2 == null) {
            return false;
         }

         Object var3 = var2.getLastPathComponent();
         return this.tree.getModel().isLeaf(var3);
      }

      public Collection<Entry<Object, Object>> connections() {
         return this.connections.entries();
      }

      public void addConnection(Object var1, Object var2) {
         this.connections.put(var1, var2);
      }

      public boolean removeConnection(Object var1, Object var2) {
         return this.connections.remove(var1, var2);
      }

      public boolean removeAllConnections() {
         if (this.connections.isEmpty()) {
            return false;
         }

         this.connections.clear();
         return true;
      }

      public Collection<Object> removeAllConnections(Object var1) {
         return this.connections.removeAll(var1);
      }

      public int getConnectionCount() {
         return this.connections.entries().size();
      }

      public boolean areConnected(Object var1, Object var2) {
         Collection var3 = this.connections.get(var1);
         return var3.contains(var2);
      }

      public boolean isConnected(Object var1) {
         return this.connections.containsKey(var1);
      }

      public Collection<Object> getOppositeObjects(Object var1) {
         return this.connections.get(var1);
      }

      public int getY(Object var1) {
         TreePath var2 = this.getPath(var1);
         return var2 == null ? -1 : this.getY(var2);
      }

      public TreePath getPath(Object var1) {
         return this.objectToPath.get(var1);
      }

      private int getY(TreePath var1) {
         Rectangle var2 = this.getBounds(var1);
         return var2 == null ? -1 : var2.y + var2.height / 2 - this.scroll.getViewport().getViewPosition().y;
      }

      private Rectangle getBounds(TreePath var1) {
         Rectangle var2 = null;

         for (TreePath var3 = var1; var2 == null && var3 != null; var3 = var3.getParentPath()) {
            var2 = this.tree.getPathBounds(var3);
         }

         return var2;
      }

      public List<Object> getSelectedObjects() {
         ArrayList var1 = Lists.newArrayList();

         for (KongaTreeNode var3 : this.tree.getSelectedNodes()) {
            var1.add(var3.getUserObject());
         }

         return var1;
      }

      public void installTransferHandler(TransferHandler var1) {
         this.tree.setDragEnabled(true);
         this.tree.setDropMode(DropMode.ON);
         this.tree.setTransferHandler(var1);
      }

      public void addListener(ConnectedTrees.TreeListener var1) {
         this.tree.addTreeSelectionListener(var1);
         this.tree.addTreeWillExpandListener(var1);
         this.scroll.getVerticalScrollBar().addAdjustmentListener(var1);
      }

      public void clearSelection() {
         this.tree.clearSelection();
      }
   }

   private class TreeListener implements TreeSelectionListener, TreeWillExpandListener, AdjustmentListener {
      private boolean blockSelectionEvents;

      private TreeListener() {
      }

      @Override
      public void valueChanged(TreeSelectionEvent var1) {
         if (!this.blockSelectionEvents) {
            this.checkOppositeSelection((KongaTree)var1.getSource());
            ConnectedTrees.this.selectionDispatcher.dispatchSelection();
         }
      }

      private void checkOppositeSelection(KongaTree var1) {
         this.blockSelectionEvents = true;
         ConnectedTrees.Tree var2 = var1 == ConnectedTrees.this.leftTree.tree ? ConnectedTrees.this.leftTree : ConnectedTrees.this.rightTree;
         ConnectedTrees.Tree var3 = var2 == ConnectedTrees.this.leftTree ? ConnectedTrees.this.rightTree : ConnectedTrees.this.leftTree;

         for (Object var5 : var2.getSelectedObjects()) {
            for (Object var7 : var2.getOppositeObjects(var5)) {
               TreePath var8 = var3.getPath(var7);
               var3.tree.makePathSelected(var8, true);
            }
         }

         this.blockSelectionEvents = false;
      }

      @Override
      public void adjustmentValueChanged(AdjustmentEvent var1) {
         ConnectedTrees.this.repaint();
      }

      @Override
      public void treeWillExpand(TreeExpansionEvent var1) throws ExpandVetoException {
         ConnectedTrees.this.repaint();
      }

      @Override
      public void treeWillCollapse(TreeExpansionEvent var1) throws ExpandVetoException {
         ConnectedTrees.this.repaint();
      }
   }
}
